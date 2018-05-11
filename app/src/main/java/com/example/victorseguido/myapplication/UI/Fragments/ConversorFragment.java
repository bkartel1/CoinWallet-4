package com.example.victorseguido.myapplication.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.UI.Adapter.SpinnerCoinAdapter;
import com.example.victorseguido.myapplication.UI.activity.PortfolioActivity2;
import com.example.victorseguido.myapplication.io.CoinWalletApiAdapter;
import com.example.victorseguido.myapplication.model.CoinInfo;



import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by victorseguido on 6/3/18.
 */

public class ConversorFragment extends Fragment implements Callback<ArrayList<CoinInfo>> {

    View v;
    ArrayList<CoinInfo> spinnerCoinList;
    Spinner mSpinnerCoinOrigin;
    Spinner mSpinnerCoinFinal;
    EditText etValueOrigin;
    TextView tvValueFinal;
    TextView tvMensaje;
    private SpinnerCoinAdapter spinnerCoinAdapter1;
    private SpinnerCoinAdapter spinnerCoinAdapter2;
    private boolean isUserInteracting;

    public ConversorFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set title bar
        ((PortfolioActivity2) getActivity())
                .setActionBarTitle("Conversor");
        return inflater.inflate(R.layout.fragment_conversor,container,false);

    }


    @Override
    public void  onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        this.v=view;
        this.spinnerCoinList = new ArrayList<CoinInfo>();

        /*Asignamos los spinner a los objetos del layout*/
        mSpinnerCoinOrigin = (Spinner) v.findViewById(R.id.spinner1);
        mSpinnerCoinFinal = (Spinner) v.findViewById(R.id.spinner2);
        etValueOrigin = (EditText) v.findViewById(R.id.etCoinQuantity);
        tvValueFinal = (TextView) v.findViewById(R.id.tvValueFinal);
        tvMensaje = (TextView) v.findViewById(R.id.tvConversor);
        //Inicializo el textview

        // Asociamos un adapter
        spinnerCoinAdapter1 = new SpinnerCoinAdapter(getActivity());
        mSpinnerCoinOrigin.setAdapter(spinnerCoinAdapter1);

        spinnerCoinAdapter2 = new SpinnerCoinAdapter(getActivity());
        mSpinnerCoinFinal.setAdapter(spinnerCoinAdapter2);


        Call<ArrayList<CoinInfo>> call= CoinWalletApiAdapter.getApiService().getCoinlist();
        call.enqueue(this);


        mSpinnerCoinOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                UpdateValue();
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });


        mSpinnerCoinFinal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {


                UpdateValue();


            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });

        etValueOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    UpdateValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void UpdateValue(){

        int origen = mSpinnerCoinOrigin.getSelectedItemPosition();
        int destino = mSpinnerCoinFinal.getSelectedItemPosition();
        float quantity;
        if(etValueOrigin.getText().length()>0){
            quantity = (float) Float.parseFloat(etValueOrigin.getText().toString());
        }else {
            quantity=0;
        }

        float factor = (float) (Float.parseFloat(spinnerCoinList.get(origen).getPriceUsd())/Float.parseFloat(spinnerCoinList.get(destino).getPriceUsd()));
        tvValueFinal.setText(" "+factor*quantity);

        tvMensaje.setText("Por "+quantity+" "+spinnerCoinList.get(origen).getSymbol().toString()+" recibes a cambio "+(factor*quantity)+" "+ spinnerCoinList.get(destino).getSymbol().toString());

    }


    @Override
    public void onResponse(Call<ArrayList<CoinInfo>> call, Response<ArrayList<CoinInfo>> response) {
        if(response.isSuccessful()){
            ArrayList<CoinInfo> CoinList = response.body();
            spinnerCoinList=CoinList;
            //  printCoinInfo(coinInfos);
            // Log.d("onResponse coinInfos", "Tamaño del array==>" + coinInfos.size());
            spinnerCoinAdapter1.setCoin2List(CoinList);
            spinnerCoinAdapter2.setCoin2List(CoinList);
        }
    }

    @Override
    public void onFailure(Call<ArrayList<CoinInfo>> call, Throwable t) {

    }



}
