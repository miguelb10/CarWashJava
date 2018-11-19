package com.upc.cwa.carwash.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.upc.cwa.carwash.Entities.Empresa;
import com.upc.cwa.carwash.Entities.Reserva;
import com.upc.cwa.carwash.Entities.Servicio;
import com.upc.cwa.carwash.Entities.Vehiculo;
import com.upc.cwa.carwash.R;
import com.upc.cwa.carwash.SpinnerAdapters.EmpresaSpinnerAdapter;
import com.upc.cwa.carwash.SpinnerAdapters.ServicioSpinnerAdapter;
import com.upc.cwa.carwash.SpinnerAdapters.VehiculoSpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrearReservaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrearReservaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearReservaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CrearReservaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearReservaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearReservaFragment newInstance(String param1, String param2) {
        CrearReservaFragment fragment = new CrearReservaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_crear_reserva, container, false);
        SharedPreferences prefs = getActivity().
                getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        int storedUserID = prefs.getInt("UserID", 0);
        AndroidNetworking.initialize(getActivity().getApplicationContext());

        final Spinner spnrVehiculo = view.findViewById(R.id.spnrVehiculo);
        AndroidNetworking.get("http://192.168.1.109:8090/api/vehiculo/cliente/{userId}")
                .addPathParameter("userId", Integer.toString(storedUserID))
                .setTag("vehiculos")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Vehiculo> vehiculos = new ArrayList<Vehiculo>();
                            Vehiculo defaultValue = new Vehiculo();
                            defaultValue.id = 0;
                            defaultValue.marca = "Selecione vehículo..";
                            vehiculos.add(defaultValue);
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                Vehiculo objVehiculo = new Vehiculo();
                                objVehiculo.id = jsonObject.getInt("id");
                                objVehiculo.marca = jsonObject.getString("marca");
                                vehiculos.add(objVehiculo);
                                VehiculoSpinnerAdapter vehiculoSpinnerAdapter =
                                        new VehiculoSpinnerAdapter(view.getContext(),
                                                android.R.layout.simple_spinner_item,
                                                vehiculos);
                                spnrVehiculo.setAdapter(vehiculoSpinnerAdapter);
                            }
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

        final Spinner spnrEmpresa = view.findViewById(R.id.spnrEmpresa);
        AndroidNetworking.get("http://192.168.1.109:8090/api/empresas")
                .setTag("empresas")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Empresa> empresas = new ArrayList<Empresa>();
                            Empresa defaultValue = new Empresa();
                            defaultValue.id = 0;
                            defaultValue.nombre = "Selecione empresa..";
                            empresas.add(defaultValue);
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                Empresa objEmpresa = new Empresa();
                                objEmpresa.id = jsonObject.getInt("id");
                                objEmpresa.nombre = jsonObject.getString("nombre");
                                empresas.add(objEmpresa);
                                EmpresaSpinnerAdapter empresaSpinnerAdapter =
                                        new EmpresaSpinnerAdapter(view.getContext(),
                                                android.R.layout.simple_spinner_item,
                                                empresas);
                                spnrEmpresa.setAdapter(empresaSpinnerAdapter);
                            }
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

        final Spinner spnrServicio = view.findViewById(R.id.spnrServicio);
        spnrEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, final View view, int position, long empresaID) {
                AndroidNetworking.get("http://192.168.1.109:8090/api/servicios/empresa/{id}")
                        .addPathParameter("id", Long.toString(empresaID))
                        .setTag("servicios")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    ArrayList<Servicio> servicios = new ArrayList<Servicio>();
                                    Servicio defaultValue = new Servicio();
                                    defaultValue.id = 0;
                                    defaultValue.nombre = "Selecione servicio..";
                                    servicios.add(defaultValue);
                                    for (int i = 0; i < response.length(); i++){
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        Servicio objServicio = new Servicio();
                                        objServicio.id = jsonObject.getInt("id");
                                        objServicio.nombre = jsonObject.getString("nombre");
                                        servicios.add(objServicio);
                                        ServicioSpinnerAdapter servicioSpinnerAdapter =
                                                new ServicioSpinnerAdapter(view.getContext(),
                                                        android.R.layout.simple_spinner_item,
                                                        servicios);
                                        spnrServicio.setAdapter(servicioSpinnerAdapter);
                                    }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Button btnReservar = view.findViewById(R.id.btnReservar);
        final CalendarView cvFecha = view.findViewById(R.id.cvFecha);
        final EditText txtHora = view.findViewById(R.id.txtHora);
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reserva objReserva = new Reserva();

                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
                String fechaform = DATE_FORMAT.format(cvFecha.getDate());

                objReserva.fecha = fechaform;
                objReserva.hora = txtHora.getText().toString();
                objReserva.idservicio = spnrServicio.getSelectedItemId();
                objReserva.idvehiculo = spnrVehiculo.getSelectedItemId();
                objReserva.calificacion = 0.0;

                AndroidNetworking.post("http://192.168.1.109:8090/api/reserva/{idvehiculo}/{idservicio}/save")
                        .addPathParameter("idvehiculo", Long.toString(objReserva.idvehiculo))
                        .addPathParameter("idservicio", Long.toString(objReserva.idservicio))
                        .addApplicationJsonBody(objReserva)
                        .setTag("reserva")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.println(Log.ERROR, "Success", "It worked!!");
                                Toast.makeText(getContext(),
                                        "Reserva guardada!",
                                        Toast.LENGTH_SHORT).show();
                                txtHora.getText().clear();
                            }

                            @Override
                            public void onError(ANError error) {
                                Log.println(Log.ASSERT, "Error", "Some error" + error.getErrorBody()
                                        + " - " + error.getErrorDetail() + " - " + error.getResponse());
                                Toast.makeText(getContext(),
                                        "Error! Contacte al administrador.",
                                        Toast.LENGTH_SHORT).show();
                                txtHora.getText().clear();
                            }
                        });
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
