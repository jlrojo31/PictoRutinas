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

import java.util.ArrayList;

public class AdaptadorTareas extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Tarea> tareas;
    int p;
    Tarea t;

    public AdaptadorTareas(Activity activity, ArrayList<Tarea> tareas){
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
        final ViewHolderTarea holder;
        View vi = convertView;

        if(vi == null) {
            holder = new ViewHolderTarea();
        }else {
            holder = (ViewHolderTarea) vi.getTag();
        }

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.item_tarea, null);

        holder.deleteButton = (Button) vi.findViewById(R.id.idBtnBTarea);
        holder.deleteButton.setTag(position);
        holder.editButton = (Button) vi.findViewById(R.id.idBtnETarea);
        holder.editButton.setTag(position);
        holder.textView = (TextView) vi.findViewById(R.id.idNomTarea);
        holder.textView.setTag(position);
        holder.imageView = (ImageView) vi.findViewById(R.id.fotoTarea);
        holder.imageView.setTag(position);
        //holder.iresource failed to call closemage.setImageResource(); //todo revisar
        vi.setTag(holder);

        int tag_DeleteButton_position=(Integer) holder.deleteButton.getTag();
        holder.deleteButton.setId(tag_DeleteButton_position);
        int tag_EditButton_position=(Integer) holder.editButton.getTag();
        holder.editButton.setId(tag_EditButton_position);
        int tag_ImageView_position=(Integer) holder.imageView.getTag();
        holder.imageView.setId(tag_ImageView_position);
        int tag_TextView_position=(Integer) holder.textView.getTag();
        holder.textView.setId(tag_TextView_position);


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.editButton.getContext(), TareaDef.class);
                i.putExtra("idTarea", holder.textView.getId()); // Todo habra que dejar un id de bbdd para pasar a la siguiente pantalla
                holder.imageView.getContext().startActivity(i);
            }
        });

        return vi;
    }
}

class ViewHolderTarea {
    Button deleteButton;
    Button editButton;
    ImageView imageView;
    TextView textView;
}

