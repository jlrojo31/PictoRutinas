package com.example.tfgpictorutinas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

public class AdaptadorTareasVisionAlumno extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Tarea> tareas;

    public AdaptadorTareasVisionAlumno(Activity activity, ArrayList<Tarea> tareas){
        this.activity = activity;
        this.tareas = tareas;
    }

    public void setTarea(int i, Tarea t) {tareas.set(i,t);}

    @Override
    public int getCount() {
        return tareas.size();
    }

    @Override
    public Tarea getItem(int i) {
        return tareas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tareas.get(i).idTarea;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderTareaVisionAlumno holder;
        View vi = convertView;

        if(vi == null) {
            holder = new ViewHolderTareaVisionAlumno();
        }else {
            holder = (ViewHolderTareaVisionAlumno) vi.getTag();
        }

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.item_tarea_vision_alumno, null);

        holder.textView = (TextView) vi.findViewById(R.id.idNomTareaVisionAlumno);
        holder.textView.setText(tareas.get(position).nombreTarea.toString());
        holder.textView.setTag(position);
        holder.imageView = (ImageView) vi.findViewById(R.id.fotoTareaVisionAlumno);
        byte[] decodedString = Base64.decode(tareas.get(position).fotoTarea.toString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageView.setImageBitmap(decodedByte);
        holder.imageView.setTag(position);
        holder.hora_ini = (TextView) vi.findViewById(R.id.tiempoTare_iniVisionAlumno);
        holder.hora_ini.setText(tareas.get(position).hora_ini.toString());
        holder.hora_ini.setTag(position);
        holder.hora_fin = (TextView) vi.findViewById(R.id.tiempoTare_endVisionAlumno);
        holder.hora_fin.setText(tareas.get(position).hora_end.toString());
        holder.hora_fin.setTag(position);

        vi.setTag(holder);

        int tag_ImageView_position=(Integer) holder.imageView.getTag();
        holder.imageView.setId(tag_ImageView_position);
        int tag_TextView_position=(Integer) holder.textView.getTag();
        holder.textView.setId(tag_TextView_position);
        int tag_hora_ini_position=(Integer) holder.hora_ini.getTag();
        holder.hora_ini.setId(tag_hora_ini_position);
        int tag_hora_fin_position=(Integer) holder.hora_fin.getTag();
        holder.hora_fin.setId(tag_hora_fin_position);

        return vi;
    }
}


class ViewHolderTareaVisionAlumno {
    ImageView imageView;
    TextView textView;
    TextView hora_ini;
    TextView hora_fin;
}


