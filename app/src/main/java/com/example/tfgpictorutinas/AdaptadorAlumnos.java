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

public class AdaptadorAlumnos extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Usuario> usuario;
    int p;
    Usuario u;

    public AdaptadorAlumnos(Activity activity, ArrayList<Usuario> usuario){
        this.activity = activity;
        this.usuario = usuario;
    }

    public void setTarea(int i, Usuario u) {usuario.set(i,u);}

    @Override
    public int getCount() {
        return usuario.size();
    }

    @Override
    public Usuario getItem(int i) {
        return usuario.get(i);
    }

    @Override
    public long getItemId(int i) {
       return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderUsuario holder;
        View vi = convertView;

        if(vi == null) {
            holder = new ViewHolderUsuario();
        }else {
            holder = (ViewHolderUsuario) vi.getTag();
        }

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.item_alumno, null);


        holder.textView = (TextView) vi.findViewById(R.id.idnomAlumnoET);
        holder.textView.setText(usuario.get(position).getNombre());
        holder.textView.setTag(position);
        holder.imageView = (ImageView) vi.findViewById(R.id.fotoAlumnoIV);
        holder.imageView.setTag(position);
        //holder.iresource failed to call closemage.setImageResource(); //todo revisar
        vi.setTag(holder);

        int tag_ImageView_position=(Integer) holder.imageView.getTag();
        holder.imageView.setId(tag_ImageView_position);
        int tag_TextView_position=(Integer) holder.textView.getTag();
        holder.textView.setId(tag_TextView_position);

        return vi;
    }
}

class ViewHolderUsuario {
    ImageView imageView;
    TextView textView;
}

