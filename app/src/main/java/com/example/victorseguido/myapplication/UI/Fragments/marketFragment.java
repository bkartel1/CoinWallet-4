package com.example.victorseguido.myapplication.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.UI.Adapter.CoinListAdapter;
import com.example.victorseguido.myapplication.UI.activity.PortfolioActivity2;
import com.example.victorseguido.myapplication.io.CoinWalletApiAdapter;
import com.example.victorseguido.myapplication.model.CoinInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

;

public class marketFragment extends Fragment implements Callback<ArrayList<CoinInfo>> {

    private CoinListAdapter coinListAdapter;

    public marketFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_market, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewCoinList);
        mRecyclerView.setHasFixedSize(true);

        // Set title bar
        ((PortfolioActivity2) getActivity()).setActionBarTitle("Market");

        // Asociamos un adapter
        coinListAdapter = new CoinListAdapter(getContext());
        mRecyclerView.setAdapter(coinListAdapter);


        // Nuestro RecyclerView usará un linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);



        Call<ArrayList<CoinInfo>> call= CoinWalletApiAdapter.getApiService().getCoinlist();
        call.enqueue(this);

        return rootView;
    }

    @Override
    public void onResponse(Call<ArrayList<CoinInfo>> call, Response<ArrayList<CoinInfo>> response) {
        if(response.isSuccessful()){
            ArrayList<CoinInfo> coinInfos = response.body();
            //  printCoinInfo(coinInfos);
            // Log.d("onResponse coinInfos", "Tamaño del array==>" + coinInfos.size());
            coinListAdapter.setCoin2List(coinInfos,0);
        }
    }

    @Override
    public void onFailure(Call<ArrayList<CoinInfo>> call, Throwable t) {

    }

}

