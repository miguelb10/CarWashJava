package com.upc.cwa.carwash.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.upc.cwa.carwash.Entities.Vehiculo;
import com.upc.cwa.carwash.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class VehiculoFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;


    public VehiculoFragment() {
    }


    @SuppressWarnings("unused")
    public static VehiculoFragment newInstance(int columnCount) {
        VehiculoFragment fragment = new VehiculoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vehiculo_list, container, false);

        SharedPreferences prefs = getActivity().
                getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        int storedUserID = prefs.getInt("UserID", 0);

        AndroidNetworking.get("http://192.168.1.2:8090/api/vehiculo/cliente/{userID}")
                .addPathParameter("userID", Long.toString(storedUserID))
                .setTag("vehiculos")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Vehiculo> vehiculos = new ArrayList<Vehiculo>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Vehiculo objVehiculo = new Vehiculo();
                                objVehiculo.id = jsonObject.getInt("id");
                                objVehiculo.tipo_vehiculo = jsonObject.getString("tipo_vehiculo");
                                objVehiculo.marca = jsonObject.getString("marca");
                                objVehiculo.placa = jsonObject.getString("placa");
                                vehiculos.add(objVehiculo);
                            }
                            if (view instanceof RecyclerView) {
                                Context context = view.getContext();
                                RecyclerView recyclerView = (RecyclerView) view;
                                if (mColumnCount <= 1) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                } else {
                                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                }
                                recyclerView.setAdapter(new MyVehiculoRecyclerViewAdapter(vehiculos, mListener));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.println(Log.ASSERT, "Error", "Some error" + error.getErrorBody()
                                + " - " + error.getErrorDetail() + " - " + error.getResponse());
                    }
                });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Vehiculo item);

    }
}
