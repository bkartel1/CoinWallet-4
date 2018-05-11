package com.example.victorseguido.myapplication.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.model.CoinInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class CoinListAdapter extends RecyclerView.Adapter<CoinListAdapter.ViewHolder> {

    private ArrayList<CoinInfo> mCoinDataSet;
    private Context context;

    // Obtener referencias de los componentes visuales para cada elemento
    // Es decir, referencias de los EditText, TextViews, Buttons
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView ivCoin;
        public TextView tvPriceUsd;
        public TextView tvPriceBtc;
        public TextView tvchange24h;


        public ViewHolder(View v) {
            super(v);
            tvName =  (TextView) v.findViewById(R.id.tvCoinName);
            ivCoin = (ImageView) v.findViewById(R.id.ivCoin);
            tvPriceBtc = (TextView) v.findViewById(R.id.tvpriceBtc);
            tvPriceUsd = (TextView) v.findViewById(R.id.tvpriceUsd);
            tvchange24h = (TextView) v.findViewById(R.id.tvchange24h);
        }
    }


    public CoinListAdapter(Context context) {
        this.context=context;
        mCoinDataSet = new ArrayList<>();
    }

    public void setCoin2List (ArrayList<CoinInfo> dataSet, int i){

        mCoinDataSet.clear();
       /* mCoinDataSet= sortTrendHigh(dataSet);
        mCoinDataSet= sortTrendLow(dataSet);*/

        switch (i) {
            case 0:
                mCoinDataSet=dataSet;
                break;
            case 1:
                mCoinDataSet= sortTrendHigh(dataSet);
                break;
            case 2:
                mCoinDataSet= sortTrendLow(dataSet);
                break;
        }



        notifyDataSetChanged(); //asi indicamos los cambios que ha habido en el dataSet.
    }


    @Override
    public CoinListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // Creamos una nueva vista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coinlistview, parent, false);

        return new ViewHolder(v);

    }

    //Enlaza los datos con el ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        // - obtenemos un elemento del dataset según su posición
        // - reemplazamos el contenido de los views según tales datos


        CoinInfo coinInfo = mCoinDataSet.get(i);

        holder.tvName.setText(coinInfo.getName());
        Picasso.with(context).
                load("https://chasing-coins.com/api/v1/std/logo/"+coinInfo.getSymbol()).
                into(holder.ivCoin);
        holder.tvPriceUsd.setText(coinInfo.getPriceUsd());
        holder.tvPriceBtc.setText(coinInfo.getPriceBtc());
        holder.tvchange24h.setText(coinInfo.getPercentChange24h());


 /*Si la variacion de las ultimas 24h es positiva se pinta en verde, sino en rojo */
        if(Float.parseFloat(coinInfo.getPercentChange24h())< 0){
            holder.tvchange24h.setTextColor(Color.parseColor("#f40404"));
        }else {
            holder.tvchange24h.setTextColor(Color.parseColor("#1A8011"));
        }


    }


    @Override
    public int getItemCount() {
        return mCoinDataSet.size();
    }


    /*Ordena el array por porcentaje de cambio */
    public ArrayList<CoinInfo> sortTrendLow(ArrayList<CoinInfo> listCoin){
        Collections.sort(listCoin);
        return listCoin ;

    }

    /*Ordena el array por porcentaje de cambio */
    public ArrayList<CoinInfo> sortTrendHigh(ArrayList<CoinInfo> listCoin){
        ArrayList<CoinInfo> tempElements = sortTrendLow(listCoin);
        Collections.reverse(tempElements);
        return  tempElements;

    }
}

