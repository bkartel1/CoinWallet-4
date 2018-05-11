package com.example.victorseguido.myapplication.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.model.CoinInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by victorseguido on 7/3/18.
 */

public class SpinnerCoinAdapter extends ArrayAdapter<CoinInfo> {

    Context mContext;

    public SpinnerCoinAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_item_coin);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder mViewHolder = new ViewHolder();

        if(convertView==null){
            LayoutInflater mInflater = (LayoutInflater) parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_item_coin, parent, false);
            mViewHolder.ivSpinnerCoin =(ImageView) convertView.findViewById(R.id.ivSpinnerCoin);
            mViewHolder.tvSpinnerCoin = (TextView) convertView.findViewById(R.id.tvSpinnerCoin);
            convertView.setTag(mViewHolder);

        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        CoinInfo coinInfo = getItem(position);

        mViewHolder.tvSpinnerCoin.setText(coinInfo.getSymbol());
        Picasso.with(mContext).
                load("https://chasing-coins.com/api/v1/std/logo/"+ coinInfo.getSymbol()).
                into(mViewHolder.ivSpinnerCoin);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public void setCoin2List(ArrayList<CoinInfo> coinInfos) {

        clear();
        addAll(coinInfos);
        notifyDataSetChanged();

    }


    private static class ViewHolder{
        ImageView ivSpinnerCoin;
        TextView tvSpinnerCoin;
    }



}