package com.upc.cwa.carwash.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.upc.cwa.carwash.R;

import org.json.JSONException;
import org.json.JSONObject;


public class DetalleReservaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private long storedReservaID;

    private OnFragmentInteractionListener mListener;

    public DetalleReservaFragment() {

    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            storedReservaID = bundle.getLong("id");
        }
    }

    // TODO: Rename and change types and number of parameters
    public static DetalleReservaFragment newInstance(Long reservaID) {
        DetalleReservaFragment fragment = new DetalleReservaFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", reservaID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =
                inflater.inflate(R.layout.fragment_detalle_reserva, container, false);

        final TextView txtVehiculo = view.findViewById(R.id.txtVehiculo);
        final TextView txtEmpresa = view.findViewById(R.id.txtEmpresa);
        final TextView txtServicio = view.findViewById(R.id.txtServicio);
        final TextView txtFecha = view.findViewById(R.id.txtFecha);
        final TextView txtHora = view.findViewById(R.id.txtHora);
        final TextView txtEstado = view.findViewById(R.id.txtEstado);
        final Button btnEliminar = view.findViewById(R.id.btnEliminarReserva);
        readBundle(getArguments());

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        AndroidNetworking.get("http://192.168.1.2:8090/api/reserva/{id}")
                .addPathParameter("id", Long.toString(storedReservaID))
                .setTag("reserva")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonVehiculo = response.getJSONObject("vehiculo");
                            JSONObject jsonServicio = response.getJSONObject("servicio");
                            JSONObject jsonEmpresa = jsonServicio.getJSONObject("empresa");
                            txtVehiculo.setText(jsonVehiculo.getString("marca"));
                            txtEmpresa.setText(jsonEmpresa.getString("nombre"));
                            txtServicio.setText(jsonServicio.getString("nombre"));
                            txtFecha.setText(response.getString("fecha"));
                            txtHora.setText(response.getString("hora"));
                            txtEstado.setText(response.getString("estado"));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.println(Log.ASSERT, "Error", "Some error" + error.getErrorBody()
                                + " - " + error.getErrorDetail() + " - " + error.getResponse());
                    }
                });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Deseas eliminar esta reserva?");
                builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener(){

                    public void onClick(final DialogInterface dialog, int which) {
                        AndroidNetworking.delete("http://192.168.1.2:8090/api/reserva/{id}/delete")
                                .addPathParameter("id", Long.toString(storedReservaID))
                                .setTag("reserva")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.println(Log.ERROR, "Success", "It worked!!");
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        Log.println(Log.ASSERT, "Error", "Some error" + error.getErrorBody()
                                                + " - " + error.getErrorDetail() + " - " + error.getResponse());
                                        Toast.makeText(getContext(),
                                                "Error!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.dismiss();
                        if (mListener != null) {
                            mListener.onEliminarReservaPressed();
                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onEliminarReservaPressed();
    }
}
