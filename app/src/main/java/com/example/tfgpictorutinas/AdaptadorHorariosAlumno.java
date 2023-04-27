package com.example.tfgpictorutinas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgpictorutinas.firebaseRDB.Usuario;

import java.util.ArrayList;
import java.util.HashMap;

public class AdaptadorHorariosAlumno extends BaseAdapter {
    protected Activity activity;
    ArrayList<String> rutina;
    ArrayList<String> dias;
    int p;
    Usuario u;

    public AdaptadorHorariosAlumno(Activity activity, ArrayList<String> rutina, ArrayList<String> dias){
        this.activity = activity;
        this.rutina = rutina;
        this.dias = dias;
    }



    @Override
    public int getCount() {
        return rutina.size();
    }

    @Override
    public String getItem(int i) {
        return "";
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderDias holder;
        View vi = convertView;

        if(vi == null) {
            holder = new ViewHolderDias();
        }else {
            holder = (ViewHolderDias) vi.getTag();
        }

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.item_horario_alumno, null);


        holder.textViewL = (TextView) vi.findViewById(R.id.idLunes);
        holder.textViewM = (TextView) vi.findViewById(R.id.idMartes);
        holder.textViewX = (TextView) vi.findViewById(R.id.idMiercoles);
        holder.textViewJ = (TextView) vi.findViewById(R.id.idJueves);
        holder.textViewV = (TextView) vi.findViewById(R.id.idViernes);
        holder.textViewS = (TextView) vi.findViewById(R.id.idSabado);
        holder.textViewD = (TextView) vi.findViewById(R.id.idDomingo);
        if (dias.get(position).contains("L")){
            holder.textViewL.setText(rutina.get(position));
            holder.textViewL.setTag(position);
            int tag_TextViewL_position=(Integer) holder.textViewL.getTag();
            holder.textViewL.setId(tag_TextViewL_position);
        }
        if (dias.get(position).contains("M")){
            holder.textViewM.setText(rutina.get(position));
            holder.textViewM.setTag(position);
            int tag_TextViewM_position=(Integer) holder.textViewM.getTag();
            holder.textViewM.setId(tag_TextViewM_position);
        }
        if (dias.get(position).contains("X")){
            holder.textViewX.setText(rutina.get(position));
            holder.textViewX.setTag(position);
            int tag_TextViewX_position=(Integer) holder.textViewX.getTag();
            holder.textViewX.setId(tag_TextViewX_position);
        }
        if (dias.get(position).contains("J")){
            holder.textViewJ.setText(rutina.get(position));
            holder.textViewJ.setTag(position);
            int tag_TextViewJ_position=(Integer) holder.textViewJ.getTag();
            holder.textViewJ.setId(tag_TextViewJ_position);
        }
        if (dias.get(position).contains("V")){
            holder.textViewV.setText(rutina.get(position));
            holder.textViewV.setTag(position);
            int tag_TextViewV_position=(Integer) holder.textViewV.getTag();
            holder.textViewV.setId(tag_TextViewV_position);
        }
        if (dias.get(position).contains("S")){
            holder.textViewS.setText(rutina.get(position));
            holder.textViewS.setTag(position);
            int tag_TextViewS_position=(Integer) holder.textViewS.getTag();
            holder.textViewS.setId(tag_TextViewS_position);
        }
        if (dias.get(position).contains("D")){
            holder.textViewD.setText(rutina.get(position));
            holder.textViewD.setTag(position);
            int tag_TextViewD_position=(Integer) holder.textViewD.getTag();
            holder.textViewD.setId(tag_TextViewD_position);
        }

        vi.setTag(holder);

        return vi;
    }
}

class ViewHolderDias {

    TextView textViewL;
    TextView textViewM;
    TextView textViewX;
    TextView textViewJ;
    TextView textViewV;
    TextView textViewS;
    TextView textViewD;
}
