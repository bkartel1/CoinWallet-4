package com.example.victorseguido.myapplication.UI.Fragments;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.victorseguido.myapplication.DbReferences.FirebaseReferences;
import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.UI.Adapter.SpinnerCoinAdapter;
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

/**
 * Created by victorseguido on 4/3/18.
 */

public class AddPairFragment extends Fragment implements Callback<ArrayList<CoinInfo>> {

    View v;
    Button btnAddPair;
    EditText etQuantity,etPrice;
    ArrayList<CoinInfo> spinnerCoinList;
    Spinner mSpinnerCoin;
    private SpinnerCoinAdapter spinnerCoinAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set title bar
        ((PortfolioActivity2) getActivity())
                .setActionBarTitle("Añadir Par");

        return inflater.inflate(R.layout.fragment_addpair,container,false);
    }

    @Override
    public void  onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        this.v=view;

        this.spinnerCoinList = new ArrayList<CoinInfo>();
        btnAddPair = (Button) v.findViewById(R.id.btnGuardar);
        etQuantity = (EditText) v.findViewById(R.id.etQuantity);
        etPrice = (EditText) v.findViewById(R.id.etPrice);
        mSpinnerCoin = (Spinner) v.findViewById(R.id.spinnerCoin);

        //Asociamos un adapter al spinner
        spinnerCoinAdapter = new SpinnerCoinAdapter(getActivity());
        mSpinnerCoin.setAdapter(spinnerCoinAdapter);

        Call<ArrayList<CoinInfo>> call= CoinWalletApiAdapter.getApiService().getCoinlist();
        call.enqueue(this);


        //Obtengo el usuario logueado y la instancia a la base de datos.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database =FirebaseDatabase.getInstance();

        //Obtengo la referencia a la lista de pares del usuario.
        final DatabaseReference myRef = database.getReference(FirebaseReferences.LIST_PAIRS_REFERENCE).child(user.getUid()).child(FirebaseReferences.PAIR_REFERENCE);


        btnAddPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int origen = mSpinnerCoin.getSelectedItemPosition();

                if(Float.valueOf(etQuantity.getText().toString())>0 && Float.valueOf(etPrice.getText().toString())>0 ) {
                    Pair pair = new Pair(
                            Float.valueOf(etQuantity.getText().toString()),
                            Float.valueOf(etPrice.getText().toString()),
                            spinnerCoinList.get(origen).getSymbol());
                    myRef.push().setValue(pair);
                }else {
                    Toast.makeText(getActivity(),"Valor Cantidad o Precio no validos",Toast.LENGTH_SHORT).show();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,new PortfolioFragment()).commit();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=1;

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    Pair pair = snapshot.getValue(Pair.class);
                    Log.i("PAIR"+i,"MONEDA: "+pair.getCoin());
                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onResponse(Call<ArrayList<CoinInfo>> call, Response<ArrayList<CoinInfo>> response) {
        if(response.isSuccessful()){
            ArrayList<CoinInfo> CoinList = response.body();
            spinnerCoinList=CoinList;
            //  printCoinInfo(coinInfos);
            // Log.d("onResponse coinInfos", "Tamaño del array==>" + coinInfos.size());
            spinnerCoinAdapter.setCoin2List(CoinList);

        }
    }

    @Override
    public void onFailure(Call<ArrayList<CoinInfo>> call, Throwable t) {

    }
}
