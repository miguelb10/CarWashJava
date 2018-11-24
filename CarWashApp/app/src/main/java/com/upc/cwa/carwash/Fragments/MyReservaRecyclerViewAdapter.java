package com.upc.cwa.carwash.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upc.cwa.carwash.Entities.Reserva;
import com.upc.cwa.carwash.Fragments.ReservaFragment.OnListFragmentInteractionListener;

import com.upc.cwa.carwash.R;

import java.util.List;


public class MyReservaRecyclerViewAdapter extends RecyclerView.Adapter<MyReservaRecyclerViewAdapter.ViewHolder> {

    private final List<Reserva> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyReservaRecyclerViewAdapter(List<Reserva> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Long.toString(mValues.get(position).id));
        holder.mVehiculoView.setText(mValues.get(position).vehiculo.marca);
        holder.mEmpresaView.setText(mValues.get(position).servicio.empresa.nombre);
        holder.mServicioView.setText(mValues.get(position).servicio.nombre);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mVehiculoView;
        public final TextView mServicioView;
        public final TextView mEmpresaView;

        public Reserva mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mVehiculoView = (TextView) view.findViewById(R.id.vehiculo_reserva);
            mServicioView = (TextView) view.findViewById(R.id.servicio_reserva);
            mEmpresaView = (TextView) view.findViewById(R.id.empresa_reserva);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mVehiculoView.getText() + "'";
        }
    }
}
