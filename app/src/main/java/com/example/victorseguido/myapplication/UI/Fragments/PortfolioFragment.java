package com.example.victorseguido.myapplication.UI.Fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victorseguido.myapplication.DbReferences.FirebaseReferences;
import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.UI.Adapter.PortFolioAdapter;
import com.example.victorseguido.myapplication.UI.activity.PortfolioActivity2;
import com.example.victorseguido.myapplication.io.CoinWalletApiAdapter;
import com.example.victorseguido.myapplication.model.CoinInfo;
import com.example.victorseguido.myapplication.model.Pair;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

/**
 * Created by victorseguido on 4/3/18.
 */

public class PortfolioFragment extends Fragment {

    View v;
    private RecyclerView rv;
    private ArrayList<Pair> pairs;
    private ArrayList<Pair> pairsOrdered;
    private PortFolioAdapter pairsAdapter;

    private Float balance;
    private Float changeBalance;

    public TextView tvBalanceUsd;
    public TextView tvBalanceBtc;
    public TextView tvChangeBalance;
    private CoinInfo btcInfo;
    private FloatingActionButton fab;

    public PortfolioFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set title bar
        ((PortfolioActivity2) getActivity())
                .setActionBarTitle("Portfolio");
/*
        Call<CoinInfo> call= CoinWalletApiAdapter.getApiService().getBtc();
        call.enqueue(this); */
        //getActivity().getActionBar().hide();
        return inflater.inflate(R.layout.fragment_portfolio,container,false);


    }

    private void loaddata(){

        //Obtengo el usuario logueado y la instancia a la base de datos.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database =FirebaseDatabase.getInstance();

        //Obtengo la referencia a la lista de pares del usuario.
        final DatabaseReference myRef = database.getReference(FirebaseReferences.LIST_PAIRS_REFERENCE).child(user.getUid()).child(FirebaseReferences.PAIR_REFERENCE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pairs.removeAll(pairs);

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    Pair pair = snapshot.getValue(Pair.class);
                    pairs.add(pair);
                }
                groupbyPairs(pairs);
                pairsAdapter.setPairs2List(pairsOrdered);

                btcInfo=pairsAdapter.getBtcInfo();
                if(btcInfo != null){
                    getBalance();
                    Log.e("Balance", "Balance Cargado");
                }else {
                    Log.e("Balance", "Sin balance");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void  onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        this.v=view;

        tvBalanceUsd =  (TextView) v.findViewById(R.id.tvBalanceUsd);
        tvBalanceBtc = (TextView) v.findViewById(R.id.tvBalanceBtc);
        tvChangeBalance = (TextView) v.findViewById(R.id.tvchangeBalance);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_add) ;

       fab.setOnClickListener(new View.OnClickListener() {

         @Override
            public void onClick(View view) {
                // Click action
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,new AddPairFragment()).commit();
            }
        });

        //Inicializacion lista de pares y el rv

        rv = (RecyclerView) v.findViewById(R.id.rvPairList);
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);


        // Asociamos un adapter
        pairsAdapter = new PortFolioAdapter(getActivity());
        rv.setAdapter(pairsAdapter);

        pairs = new ArrayList<>();
        pairsOrdered = new ArrayList<>();

        loaddata();



    }



    public void groupbyPairs(ArrayList<Pair> pairs){
        ArrayList<String> Pares = new ArrayList<>();
        Pair par ;

        //Bucle para generar el listado de pares de monedas
        for(int i=0; i<pairs.size(); i++){
            if (! existePar(Pares,pairs.get(i).getCoin())){
                String s = pairs.get(i).getCoin();
                Pares.add(s);

               // Log.e("Pares", "Par("+ i + "):" +pairs.get(i).getCoin());
            }
        }

        //Bucle para crear los pares
        for(int y=0; y<Pares.size(); y++){
            //por cada moneda buscamos todos lo pares
            float avgPrecio=0;
            float totalCoste=0;
            float Cantidad=0;
            for(int j=0;j<pairs.size();j++){
                if(Pares.get(y).equals(pairs.get(j).getCoin())) {
                    Cantidad = Cantidad + pairs.get(j).getQuantity();
                    totalCoste = totalCoste + ((Float) pairs.get(j).getPrice() * pairs.get(j).getQuantity());
                    avgPrecio = totalCoste / Cantidad;
                }
            }

            //Crear par y añadirlo al array
            par= new Pair(Cantidad,avgPrecio,Pares.get(y));
            pairsOrdered.add(par);

            Log.e("Pares", "Par("+ y + ")" +Pares.get(y)+": precio medio" +avgPrecio + " cantidad:"+Cantidad+" total coste:"+totalCoste);


        }

    }

    public boolean existePar(ArrayList<String> Pares, String par){

        if(Pares==null){return false;}
        for(int i=0; i<Pares.size();i++){
            if(par.equals(Pares.get(i).toString())){
                return true;
            }

        }
            return false;
    }


    public void getBalance(){
        Float precioIni= 0.0f;
        Float precioFin= 0.0f;
        balance=0.0f;
        changeBalance=0.0f;


        for(int j=0;j<pairsOrdered.size();j++){
        precioIni=precioIni+pairsOrdered.get(j).getPrice()*pairsOrdered.get(j).getQuantity();
        precioFin=precioFin+pairsAdapter.getCoinPrice(pairsOrdered.get(j).getCoin())*pairsOrdered.get(j).getQuantity();
        }

        balance=precioFin* Float.parseFloat(btcInfo.getPriceUsd());

        changeBalance=((precioFin/precioIni)-1)*100;

        tvBalanceUsd.setText(balance.toString()+" $");
        tvBalanceBtc.setText(precioFin.toString()+" Btc");
        tvChangeBalance.setText(changeBalance.toString()+"%");

        if(changeBalance>=1){
            tvChangeBalance.setTextColor(Color.parseColor("#1A8011"));
        }else {
            tvChangeBalance.setTextColor(Color.parseColor("#f40404"));
        }
    }


}
