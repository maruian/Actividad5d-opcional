package com.example.a2dam.actividad5a;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a2dam.actividad5a.model.Producto;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 06/01/2018.
 */

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolderAdaptador> {

    private ArrayList<Producto> listadoProductos;
    private FragmentManager fm;

    public AdaptadorProductos (ArrayList<Producto> listadoProductos, FragmentManager fm) {
        this.listadoProductos = listadoProductos;
        this.fm = fm;
    }

    @Override
    public ViewHolderAdaptador onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_producto,parent,false);
        return new ViewHolderAdaptador(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderAdaptador holder, int position) {
        holder.asignarDatos(listadoProductos.get(position));
    }

    @Override
    public int getItemCount() {
        return listadoProductos.size();
    }


    public class ViewHolderAdaptador extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView nombre, descripcion, categoria, precio, usuario;

        public ViewHolderAdaptador(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre = (TextView)itemView.findViewById(R.id.nombreProducto);
            descripcion = (TextView)itemView.findViewById(R.id.descripcionProducto);
            categoria = (TextView)itemView.findViewById(R.id.categoriaProducto);
            precio = (TextView)itemView.findViewById(R.id.precioProducto);
            usuario = (TextView)itemView.findViewById(R.id.usuarioProducto);
        }

        public void asignarDatos(Producto p){
            nombre.setText(p.getNombre());
            descripcion.setText(p.getDescripcion());
            categoria.setText(p.getCategoria());
            precio.setText(p.getPrecio());
            usuario.setText(p.getUsuario());
        }

        public void onClick(View view) {
            Context context = view.getContext();
            int position = getAdapterPosition();


            Producto p = listadoProductos.get(position);
            ModificarProducto modificarProducto = ModificarProducto.newInstance(p);

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_dinamic, modificarProducto);
            ft.commit();
            ft.addToBackStack(null);

        }
    }
}
