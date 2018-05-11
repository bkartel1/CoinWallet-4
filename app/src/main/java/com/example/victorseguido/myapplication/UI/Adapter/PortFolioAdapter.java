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
import com.example.victorseguido.myapplication.io.CoinWalletApiAdapter;
import com.example.victorseguido.myapplication.model.CoinInfo;
import com.example.victorseguido.myapplication.model.Pair;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by victorseguido on 3/3/18.
 */

public class PortFolioAdapter extends RecyclerView.Adapter<PortFolioAdapter.ViewHolder> implements Callback<ArrayList<CoinInfo>> {

    private ArrayList<Pair> mPairsList;
    private Context context;
    private ArrayList<CoinInfo> coinInfos;

    // Obtener referencias de los componentes visuales para cada elemento
    // Es decir, referencias de los EditText, TextViews, Buttons
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView ivCoin;
        public TextView tvQuantity;
        public TextView tvPriceUsd;
        public TextView tvPriceBtc;
        public TextView tvchangePair;


        public ViewHolder(View v) {
            super(v);
            tvName =  (TextView) v.findViewById(R.id.tvPairName);
            ivCoin = (ImageView) v.findViewById(R.id.ivCoin);
            tvQuantity = (TextView) v.findViewById(R.id.tvQuantity);
          //  tvPriceUsd = (TextView) v.findViewById(R.id.tvpriceUsd);
            tvPriceBtc = (TextView) v.findViewById(R.id.tvpriceBtc);
            tvchangePair = (TextView) v.findViewById(R.id.tvchangePair);
        }
    }


    public PortFolioAdapter(Context context) {
        this.context=context;
        mPairsList = new ArrayList<>();
        Call<ArrayList<CoinInfo>> call= CoinWalletApiAdapter.getApiService().getCoinlist();
        call.enqueue(this);
    }

    public void setPairs2List (ArrayList<Pair> dataSet){

        mPairsList=dataSet;
        notifyDataSetChanged(); //asi indicamos los cambios que ha habido en el dataSet.
    }


    @Override
    public PortFolioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Creamos una nueva vista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paircardview, parent, false);



        return new ViewHolder(v);

    }

    //Enlaza los datos con el ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        // - obtenemos un elemento del dataset según su posición
        // - reemplazamos el contenido de los views según tales datos

        CoinInfo coinInfo=null;
        Pair pair = mPairsList.get(i);

        for(int j=0; j<coinInfos.size();j++){
            if(pair.getCoin().equals(coinInfos.get(j).getSymbol())){
                coinInfo=coinInfos.get(j);
                break;
            }
        }

        holder.tvName.setText(pair.getCoin());
        Picasso.with(context).
                load("https://chasing-coins.com/api/v1/std/logo/"+pair.getCoin()).
                into(holder.ivCoin);
//        holder.tvPriceUsd.setText("1000$");
        holder.tvPriceBtc.setText(String.valueOf(pair.getPrice())+" Btc");
        holder.tvQuantity.setText(pair.getQuantity().toString());
        float percentage = ( (Float.parseFloat(coinInfo.getPriceBtc())-pair.getPrice())/pair.getPrice() )*100;

       if(percentage>=1){
            holder.tvchangePair.setTextColor(Color.parseColor("#1A8011"));
        }else {
            holder.tvchangePair.setTextColor(Color.parseColor("#f40404"));
        }
        holder.tvchangePair.setText(percentage+"%");


 /*Si la variacion de las ultimas 24h es positiva se pinta en verde, sino en rojo
        if(Float.parseFloat(pair.getPercentChange24h())< 0){
            holder.tvchange24h.setTextColor(Color.parseColor("#f40404"));
        }else {
            holder.tvchange24h.setTextColor(Color.parseColor("#1A8011"));
        }

*/
    }


    @Override
    public int getItemCount() {
        return mPairsList.size();
    }


    @Override
    public void onResponse(Call<ArrayList<CoinInfo>> call, Response<ArrayList<CoinInfo>> response) {
        if(response.isSuccessful()){
            coinInfos = response.body();

        }
    }

    @Override
    public void onFailure(Call<ArrayList<CoinInfo>> call, Throwable t) {

    }

    public CoinInfo getBtcInfo(){
        return coinInfos.get(0);
    }

    public Float getCoinPrice(String Symbol){

        for(int y=0; y<coinInfos.size();y++){
            if(coinInfos.get(y).getSymbol().equals(Symbol)){
                return Float.parseFloat(coinInfos.get(y).getPriceBtc());
            }
        }
        return 0.0f;
    }

}
