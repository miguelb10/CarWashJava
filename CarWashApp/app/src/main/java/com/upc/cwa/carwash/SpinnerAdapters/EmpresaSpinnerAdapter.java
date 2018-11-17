package com.upc.cwa.carwash.SpinnerAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.upc.cwa.carwash.Entities.Empresa;
import com.upc.cwa.carwash.Entities.Servicio;

import java.util.List;

public class EmpresaSpinnerAdapter extends ArrayAdapter<Empresa>{
    private Context context;
    List<Empresa> data = null;

    public EmpresaSpinnerAdapter(@NonNull Context context, int resource, List<Empresa> data2) {
        super(context, resource);
        this.context=context;
        this.data=data2;
    }

    @Nullable
    @Override
    public Empresa getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView)super.getView(position,convertView,parent);
        label.setTextColor(Color.BLACK);
        label.setText(data.get(position).nombre);
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView)super.getDropDownView(position,convertView,parent);
        label.setTextColor(Color.BLACK);
        label.setText(data.get(position).nombre);
        label.setTextSize(40);
        return label;
    }
}
