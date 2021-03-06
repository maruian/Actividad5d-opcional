package com.example.a2dam.actividad5a;


import com.example.a2dam.actividad5a.model.Producto;
import com.example.a2dam.actividad5a.model.Usuario;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AltaUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarProducto extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLAVE = "PRODUCTO";


    private Button guardar, eliminar, cambiarImagen;
    private Spinner spinner_categoria;
    private EditText text_nombre, text_descripcion, text_precio;

    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 2;

    //Definim les referencies a la base de dades
    DatabaseReference dbProductos;
    DatabaseReference dbProductosXUsuario;
    DatabaseReference dbProductosXCategoria;

    // TODO: Rename and change types of parameters
    private Producto p;

    private OnFragmentInteractionListener mListener;

    public ModificarProducto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AltaUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static ModificarProducto newInstance(Producto producto) {
        ModificarProducto fragment = new ModificarProducto();
        Bundle args = new Bundle();
        args.putParcelable(CLAVE, producto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            p = getArguments().getParcelable(CLAVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.modificar_producto, container, false);

        text_nombre = v.findViewById(R.id.nombreProducto);
        text_descripcion = v.findViewById(R.id.descripcionProducto);
        text_precio = v.findViewById(R.id.precioProducto);
        spinner_categoria = v.findViewById(R.id.categoriaProducto);
        cambiarImagen = v.findViewById(R.id.btnCambiarImagen);

        text_nombre.setText(p.getNombre());
        text_descripcion.setText(p.getDescripcion());
        text_precio.setText(p.getPrecio());

        switch (p.getCategoria()){
            case "Tecnologia": spinner_categoria.setSelection(0);
            break;
            case "Coches": spinner_categoria.setSelection(1);
            break;
            case "Hogar": spinner_categoria.setSelection(2);
            break;
        }

        guardar = v.findViewById(R.id.guardar);
        eliminar = v.findViewById(R.id.eliminar);

        cambiarImagen.setOnClickListener(this);
        guardar.setOnClickListener(this);
        eliminar.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        //Instanciem les referencies a la base de dades
        dbProductos = FirebaseDatabase.getInstance().getReference("Productos/"+p.getKey());
        dbProductosXCategoria = FirebaseDatabase.getInstance().getReference("ProductosXCategoria/"+p.getCategoria()+"/"+p.getKey());
        dbProductosXUsuario = FirebaseDatabase.getInstance().getReference("ProductosXUsuario/"+MainActivity.usuarioSesion.getUsuario()+"/"+p.getKey());

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guardar:
                guardarProducto();
                break;
            case R.id.eliminar:
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(getContext());
                adBuilder.setMessage("Estas a punto de eliminar este producto. ¿Seguro que quieres continuar?");
                adBuilder.setTitle("Advertencia!");
                adBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarProducto();
                    }
                });
                adBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = adBuilder.create();
                alertDialog.show();
                break;
            case R.id.btnCambiarImagen:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            final StorageReference filepath = storageReference.child(MainActivity.usuarioSesion.getUsuario()+"/"+p.getKey());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"Imagen subida",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void guardarProducto(){
        String nombre = text_nombre.getText().toString();
        String descripcion = text_descripcion.getText().toString();
        String categoria = spinner_categoria.getSelectedItem().toString();
        String precio = text_precio.getText().toString();
        if (!TextUtils.isEmpty(nombre) &&
                !TextUtils.isEmpty(descripcion) &&
                !TextUtils.isEmpty(precio)) {
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setCategoria(categoria);
            p.setPrecio(precio);

            dbProductos.setValue(p);
            dbProductosXUsuario.setValue(p);
            dbProductosXCategoria.removeValue();
            dbProductosXCategoria = FirebaseDatabase.getInstance().getReference("ProductosXCategoria/"+p.getCategoria()+"/"+p.getKey());
            dbProductosXCategoria.setValue(p);

            Toast.makeText(getContext(),"Producto modificado",Toast.LENGTH_SHORT).show();
            // Fem desapareixer el fragment
            getFragmentManager().beginTransaction().remove(ModificarProducto.this).commit();
        } else {
            Toast.makeText(getContext(),"Introduce datos validos",Toast.LENGTH_SHORT).show();
        }

    }

    private void eliminarProducto() {
        StorageReference filepath = storageReference.child(MainActivity.usuarioSesion.getUsuario()+"/"+p.getKey());
        filepath.delete();
        dbProductos.removeValue();
        dbProductosXUsuario.removeValue();
        dbProductosXCategoria.removeValue();
        getFragmentManager().beginTransaction().remove(ModificarProducto.this).commit();
    }
}