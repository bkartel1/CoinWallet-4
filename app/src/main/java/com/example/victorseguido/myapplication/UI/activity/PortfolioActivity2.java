package com.example.victorseguido.myapplication.UI.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Transformation;
import com.example.victorseguido.myapplication.R;
import com.example.victorseguido.myapplication.UI.Classes.Utilities;
import com.example.victorseguido.myapplication.UI.Fragments.AddPairFragment;
import com.example.victorseguido.myapplication.UI.Fragments.ContenedorFragment;
import com.example.victorseguido.myapplication.UI.Fragments.ConversorFragment;
import com.example.victorseguido.myapplication.UI.Fragments.PortfolioFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

public class PortfolioActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ContenedorFragment.OnFragmentInteractionListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio2);
        /*------*/
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("id");
        /*------*/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);


        /*Cargar imagen y nombre en el navigation drawer*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {

            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();
            String mail = user.getEmail();

            ((TextView) header.findViewById(R.id.UserName)).setText(name);
            ((TextView) header.findViewById(R.id.UserEmail)).setText(mail);

            if(photoUrl == null) {
                photoUrl = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.imagen2)
                        + '/' + getResources().getResourceTypeName(R.drawable.imagen2)
                        + '/' + getResources().getResourceEntryName(R.drawable.imagen2));
            }

            // para que se vea la imagen en circulo
            Picasso.with(this).
                    load(photoUrl).
                    resize(150,150).
                    transform(new CircleTransform()).
                    into((ImageView) header.findViewById(R.id.UserPhoto));

            Log.e("Usuario", "userName: "+ user.getDisplayName() + " email :" +user.getEmail() +" foto: "+ photoUrl.toString());
        }



        /*Añado el fragment por defecto o lanza el del otro activity*/
        if(Utilities.validaPantalla==true){
            setInicialFragment(value);
            Utilities.validaPantalla=false;
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.portfolio_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Boolean fragmentSeleccionado = false;
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_portfolio) {
            fragment = new PortfolioFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_addpair) {

            fragment = new AddPairFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_market) {
          /*  Intent m = new Intent(PortfolioActivity2.this,MarketActivity.class);
            startActivity(m);
            fragmentSeleccionado=false;*/
          fragment = new ContenedorFragment();
            fragmentSeleccionado=true;
        } else if (id == R.id.nav_conversor) {
            fragment= new ConversorFragment();
            fragmentSeleccionado=true;
        }
        if(fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setInicialFragment(int idFragment){

        Fragment fragment = null;

         if (idFragment == R.id.nav_addpair) {

            fragment = new AddPairFragment();

        } else if (idFragment == R.id.nav_conversor) {
            fragment= new ConversorFragment();

        }else {
             fragment = new PortfolioFragment();
        }
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,fragment).commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
