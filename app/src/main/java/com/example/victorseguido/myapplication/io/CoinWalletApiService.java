package com.example.victorseguido.myapplication.io;

import com.example.victorseguido.myapplication.model.CoinInfo;
import com.example.victorseguido.myapplication.model.CoinInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by victorseguido on 30/1/18.
 */

public interface CoinWalletApiService {

  //@GET("data/all/coinlist")
  @GET("ticker/")
  Call<ArrayList<CoinInfo>> getCoinlist();

  @GET("ticker/bitcoin/")
  Call<CoinInfo> getBtc();

}
