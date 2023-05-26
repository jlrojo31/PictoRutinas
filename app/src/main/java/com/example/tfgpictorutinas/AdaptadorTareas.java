package com.example.tfgpictorutinas;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.text.TextWatcher;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;

public class AdaptadorTareas extends FirebaseRecyclerAdapter<Tarea,AdaptadorTareas.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context mContext;
    public AdaptadorTareas(@NonNull FirebaseRecyclerOptions<Tarea> options, Context context) {
        super(options);
        this.mContext =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Tarea model) {
        holder.hora_ini.setText(model.getHora_ini());
        holder.hora_end.setText(model.getHora_end());
        holder.descripcion.setText(model.getNombreTarea());
        byte[] imageAsBytes = Base64.decode(model.getFotoTarea(), Base64.DEFAULT);
        holder.picto.setImageBitmap((BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));

       /* if (mContext instanceof EditorTareas) {
            if(position== 0) {
                ((EditorTareas) mContext).setHora();
                try {
                    {
                        ((EditorTareas) mContext).actualizarHoras();
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        }*/
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(holder.editButton.getContext(), TareaDef.class);
                String ant_key;
                if (position>1){
                    intent.putExtra("antkey",String.valueOf((getRef(position-1).getKey())));
                }
                String name_tar="";

                if (mContext instanceof EditorTareas) {
                    name_tar = ((EditorTareas)mContext).get_name();
                }
                intent.putExtra("idTarea", model.getIdTarea());
                intent.putExtra("idRutina", model.getRutina_id());
                intent.putExtra("tarea_picto", model.getFotoTarea());
                intent.putExtra("tarea_descripcion", model.getNombreTarea());
                intent.putExtra("tarea_hora_ini", model.getHora_ini());
                intent.putExtra("tarea_hora_end", model.getHora_end());
                intent.putExtra("nombre", name_tar);
                intent.putExtra("key", String.valueOf((getRef(position).getKey())));
                holder.editButton.getContext().startActivity(intent);

            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.descripcion.getContext());
                builder.setTitle("Estas seguro de quere borrar la tarea");
                builder.setMessage("Una vez borrada la tarea se eliminara para siempre");
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("pictorutinas")
                                .child("tareas").child(getRef(position).getKey()).removeValue();
                        /*if (mContext instanceof EditorTareas) {
                            try {
                                ((EditorTareas)mContext).actualizarHoras();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }*/
                        Toast.makeText(holder.descripcion.getContext(),"La tarea"+model.getNombreTarea()+" se ha eliminado",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.descripcion.getContext(),"La tarea no se a eliminado",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea,parent,false);
        return new myViewHolder(vista);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        Button deleteButton;
        Button editButton;
        ImageView picto;
        TextView hora_ini;
        TextView hora_end;
        TextView descripcion;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            picto = itemView.findViewById(R.id.fotoTarea);
            hora_ini = itemView.findViewById(R.id.tiempoTare_ini);
            hora_end = itemView.findViewById(R.id.tiempoTare_end);
            descripcion = itemView.findViewById(R.id.idNomTarea);
            editButton = itemView.findViewById(R.id.idBtnETarea);
            deleteButton= itemView.findViewById(R.id.idBtnBTarea);

        }
    }
}

