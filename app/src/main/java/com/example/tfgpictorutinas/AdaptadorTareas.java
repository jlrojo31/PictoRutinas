package com.example.tfgpictorutinas;

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

import java.util.ArrayList;

public class AdaptadorTareas extends FirebaseRecyclerAdapter<Tarea,AdaptadorTareas.myViewHolder> {
   /* protected Activity activity;
    protected ArrayList<Tarea> tareas;
    int p;
    Tarea t;*/

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdaptadorTareas(@NonNull FirebaseRecyclerOptions<Tarea> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Tarea model) {
        holder.hora_ini.setText(model.getHora_ini());
        holder.hora_end.setText(model.getHora_end());
        holder.descripcion.setText(model.getNombreTarea());
        byte[] imageAsBytes = Base64.decode(model.getFotoTarea(), Base64.DEFAULT);
        holder.picto.setImageBitmap((BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.editButton.getContext(), TareaDef.class);

                intent.putExtra("is_old",1);

                intent.putExtra("idTarea", model.getIdTarea());
                intent.putExtra("idRutina", model.getRutina_id());

                intent.putExtra("tarea_picto", model.getFotoTarea());
                intent.putExtra("tarea_descripcion", model.getNombreTarea());
                intent.putExtra("tarea_hora_ini", model.getHora_ini());
                intent.putExtra("tarea_hora_end", model.getHora_end());


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
    /*
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
}*/

