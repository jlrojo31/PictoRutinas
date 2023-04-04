package com.example.tfgpictorutinas;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Rutina> rutinas;
    int p;
    Rutina r;

    public Adaptador(Activity activity, ArrayList<Rutina> rutinas){
        this.activity = activity;
        this.rutinas = rutinas;
    }

    public void setRutina(int i, Rutina r) {rutinas.set(i,r);}

    @Override
    public int getCount() {
        return rutinas.size();
    }

    @Override
    public Rutina getItem(int i) {
        return rutinas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rutinas.get(i).idRutina;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View vi = convertView;
        p=position;
        if(vi == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.item_rutina, null);

            holder.caption = (EditText) vi
                    .findViewById(R.id.idnom);
            holder.caption.setTag(position);
            holder.caption.setText(rutinas.get(position).getNombre().toString());
            vi.setTag(holder);
        }else {
            holder = (ViewHolder) vi.getTag();
        }

        int tag_position=(Integer) holder.caption.getTag();
        holder.caption.setId(tag_position);

        holder.caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holder.caption.getId();
                final EditText Caption = (EditText) holder.caption;
                if(Caption.getText().toString().length()>0){
                    Rutina aux=rutinas.get(position2);
                    rutinas.get(position2).setNombre(Caption.getText().toString());
                }else{

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        return vi;
    }
}

class ViewHolder {
    EditText caption;
}
