package com.upc.cwa.carwash.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.upc.cwa.carwash.Fragments.CrearReservaFragment;
import com.upc.cwa.carwash.Fragments.CrearVehiculoFragment;
import com.upc.cwa.carwash.Fragments.ReservaFragment;
import com.upc.cwa.carwash.Fragments.VehiculoFragment;
import com.upc.cwa.carwash.Fragments.dummy.DummyContent;
import com.upc.cwa.carwash.R;

public class MainActivity extends AppCompatActivity
implements CrearReservaFragment.OnFragmentInteractionListener,
        CrearVehiculoFragment.OnFragmentInteractionListener,
        VehiculoFragment.OnListFragmentInteractionListener,
        ReservaFragment.OnListFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_vehicles:
                    CrearVehiculoFragment crearVehiculoFragment = new CrearVehiculoFragment();
                    replaceFragment(crearVehiculoFragment);
                    /*VehiculoFragment vehiculoFragment = new VehiculoFragment();
                    replaceFragment(vehiculoFragment);*/
                    return true;
                case R.id.navigation_reservations:
                    CrearReservaFragment crearReservaFragment = new CrearReservaFragment();
                    replaceFragment(crearReservaFragment);
                    /*ReservaFragment reservaFragment = new ReservaFragment();
                    replaceFragment(reservaFragment);*/
                    return true;
                case R.id.navigation_news:
                    mTextMessage.setText("3");
                    return true;
                case R.id.navigation_questions:
                    mTextMessage.setText("4");
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText("5");
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment myFragment){
        FragmentManager fragmentManager =
                getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, myFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CrearVehiculoFragment crearVehiculoFragment = new CrearVehiculoFragment();
        replaceFragment(crearVehiculoFragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
