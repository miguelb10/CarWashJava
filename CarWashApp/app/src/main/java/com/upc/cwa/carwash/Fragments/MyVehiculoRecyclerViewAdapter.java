package com.upc.cwa.carwash.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upc.cwa.carwash.Entities.Vehiculo;
import com.upc.cwa.carwash.Fragments.VehiculoFragment.OnListFragmentInteractionListener;
import com.upc.cwa.carwash.Fragments.dummy.DummyContent.DummyItem;
import com.upc.cwa.carwash.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVehiculoRecyclerViewAdapter extends RecyclerView.Adapter<MyVehiculoRecyclerViewAdapter.ViewHolder> {

    private final List<Vehiculo> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyVehiculoRecyclerViewAdapter(List<Vehiculo> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_vehiculo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Long.toString(mValues.get(position).id));
        holder.mMarcaView.setText(mValues.get(position).marca);
        holder.mTipoView.setText(mValues.get(position).tipo_vehiculo);
        holder.mPlacaView.setText(mValues.get(position).placa);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
        public final TextView mMarcaView;
        public final TextView mTipoView;
        public final TextView mPlacaView;
        public Vehiculo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mMarcaView = (TextView) view.findViewById(R.id.marca_vehiculo);
            mTipoView = (TextView) view.findViewById(R.id.tipo_vehiculo);
            mPlacaView = (TextView) view.findViewById(R.id.placa_vehiculo);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMarcaView.getText() + "'";
        }
    }
}
