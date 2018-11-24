package com.upc.cwa.carwash.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.upc.cwa.carwash.Entities.Empresa;
import com.upc.cwa.carwash.Entities.Reserva;
import com.upc.cwa.carwash.Entities.Servicio;
import com.upc.cwa.carwash.Entities.Vehiculo;
import com.upc.cwa.carwash.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ReservaFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;


    public ReservaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReservaFragment newInstance(int columnCount) {
        ReservaFragment fragment = new ReservaFragment();
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
        final View view = inflater.inflate(R.layout.fragment_reserva_list, container, false);

        SharedPreferences prefs = getActivity().
                getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        int storedUserID = prefs.getInt("UserID", 0);

        AndroidNetworking.get("http://192.168.1.2:8090/api/reserva/cliente/{userID}")
                .addPathParameter("userID", Long.toString(storedUserID))
                .setTag("reservas")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Reserva> reservas = new ArrayList<Reserva>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Reserva objReserva = new Reserva();
                                objReserva.id = jsonObject.getInt("id");
                                objReserva.fecha = jsonObject.getString("fecha");
                                objReserva.hora = jsonObject.getString("hora");
                                Gson g = new Gson();
                                objReserva.vehiculo = g.fromJson(jsonObject.getString("vehiculo"), Vehiculo.class);
                                objReserva.servicio = g.fromJson(jsonObject.getString("servicio"), Servicio.class);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("servicio");
                                objReserva.servicio.empresa =
                                        g.fromJson(jsonObject1.getString("empresa"), Empresa.class);
                                objReserva.estado = jsonObject.getString("estado");
                                reservas.add(objReserva);
                            }
                            if (view instanceof RecyclerView) {
                                Context context = view.getContext();
                                RecyclerView recyclerView = (RecyclerView) view;
                                if (mColumnCount <= 1) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                } else {
                                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                }
                                recyclerView.setAdapter(new MyReservaRecyclerViewAdapter(reservas, mListener));
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
        void onListFragmentInteraction(Reserva item);
    }
}
