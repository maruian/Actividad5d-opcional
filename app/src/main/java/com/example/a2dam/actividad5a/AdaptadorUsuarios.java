package com.example.a2dam.actividad5a;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;



import com.example.a2dam.actividad5a.model.Usuario;


import java.util.ArrayList;

/**
 * Created by admin on 24/12/2017.
 */

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolderAdaptador> {

    private ArrayList<Usuario>listadoUsuarios;
    private FragmentManager fm;

    public AdaptadorUsuarios(ArrayList<Usuario> listadoUsuarios, FragmentManager fm){
       this.listadoUsuarios=listadoUsuarios;
       this.fm = fm;
    }

    @Override
    public ViewHolderAdaptador onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_usuario,null,false);
        return new ViewHolderAdaptador(view);
    }



    @Override
    public void onBindViewHolder(ViewHolderAdaptador holder, int position) {
        holder.asignarDatos(listadoUsuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return listadoUsuarios.size();
    }

    public class ViewHolderAdaptador extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView text_usuario, text_correo, text_nombre, text_apellidos, text_direccion;

        public ViewHolderAdaptador(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            text_usuario = (TextView)itemView.findViewById(R.id.usuario);
            text_correo = (TextView)itemView.findViewById(R.id.correo);
            text_nombre = (TextView)itemView.findViewById(R.id.nombre);
            text_apellidos = (TextView)itemView.findViewById(R.id.apellidos);
            text_direccion = (TextView)itemView.findViewById(R.id.direccion);
        }

        public void asignarDatos(Usuario usuario) {
            text_usuario.setText(usuario.getUsuario());
            text_correo.setText(usuario.getCorreo());
            text_nombre.setText(usuario.getNombre());
            text_apellidos.setText(usuario.getApellidos());
            text_direccion.setText(usuario.getDireccion());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            int position = getAdapterPosition();


            Usuario u = listadoUsuarios.get(position);
            EditarUsuari editarUsuari = EditarUsuari.newInstance(u);

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_dinamic, editarUsuari);
            ft.commit();
            ft.addToBackStack(null);

        }

    }
}
