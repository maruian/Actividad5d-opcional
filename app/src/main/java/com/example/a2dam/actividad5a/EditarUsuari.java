package com.example.a2dam.actividad5a;


import com.example.a2dam.actividad5a.model.Usuario;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AltaUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarUsuari extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLAVE = "USUARIO";


    private Button guardar, eliminar;
    private EditText text_nombre, text_apellidos, text_direccion;
    private FirebaseAuth mAuth;


    // TODO: Rename and change types of parameters
    private Usuario u;

    private OnFragmentInteractionListener mListener;

    public EditarUsuari() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AltaUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarUsuari newInstance(Usuario usuari) {
        EditarUsuari fragment = new EditarUsuari();
        Bundle args = new Bundle();
        args.putParcelable(CLAVE,usuari);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            u = getArguments().getParcelable(CLAVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.editar_usuari, container, false);

        text_nombre = v.findViewById(R.id.etNombre);
        text_apellidos = v.findViewById(R.id.etApellidos);
        text_direccion = v.findViewById(R.id.etDireccion);

        text_nombre.setText(u.getNombre());
        text_apellidos.setText(u.getApellidos());
        text_direccion.setText(u.getDireccion());

        guardar = v.findViewById(R.id.guardar);
        eliminar = v.findViewById(R.id.eliminar);

        mAuth = FirebaseAuth.getInstance();

        guardar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
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

        final DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("Usuarios");
        final String nombre = text_nombre.getText().toString();
        final String apellidos = text_apellidos.getText().toString();
        final String direccion = text_direccion.getText().toString();

        switch (view.getId()) {
            case R.id.guardar:

                if (!TextUtils.isEmpty(nombre) &&
                        !TextUtils.isEmpty(apellidos) &&
                        !TextUtils.isEmpty(direccion)) {

                    Query q = bbdd.orderByChild("usuario").equalTo(u.getUsuario());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                String clave = datasnapshot.getKey();
                                bbdd.child(clave).child("nombre").setValue(nombre);
                                bbdd.child(clave).child("apellidos").setValue(apellidos);
                                bbdd.child(clave).child("direccion").setValue(direccion);
                            }
                            MainActivity.usuarioSesion.setNombre(nombre);
                            MainActivity.usuarioSesion.setApellidos(apellidos);
                            MainActivity.usuarioSesion.setDireccion(direccion);
                            Toast.makeText(getContext(),"Datos modificados con exito",Toast.LENGTH_SHORT).show();
                            text_nombre.setEnabled(false);
                            text_apellidos.setEnabled(false);
                            text_direccion.setEnabled(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Debes introducir datos correctos", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.eliminar:

                //advertim al usuari

                AlertDialog.Builder adBuilder1 = new AlertDialog.Builder(getContext());
                adBuilder1.setMessage("Si eliminas tu cuenta todos los productos asociados desapareceran. ¿Seguro que quieres continuar?");
                adBuilder1.setTitle("Advertencia!");
                adBuilder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query q = bbdd.orderByChild("usuario").equalTo(u.getUsuario());
                        q.addListenerForSingleValueEvent(new ValueEventListener() {

                            //borrem el usuari de la base de dades
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                    String clave = datasnapshot.getKey();
                                    DatabaseReference ref = bbdd.child(clave);
                                    ref.removeValue();
                                }
                                String uid = mAuth.getUid();

                                //borrem els productes del usuari
                                final DatabaseReference bbddProductos = FirebaseDatabase.getInstance().getReference("Productos");
                                Query q = bbddProductos.orderByChild("uid").equalTo(uid);
                                q.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                            String clave = datasnapshot.getKey();
                                            DatabaseReference ref = bbddProductos.child(clave);
                                            ref.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference bbddProductosXCategoriaHogar = FirebaseDatabase.getInstance().getReference("ProductosXCategoria/Hogar");
                                Query q1 = bbddProductosXCategoriaHogar.orderByChild("uid").equalTo(uid);
                                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                            String clave = datasnapshot.getKey();
                                            DatabaseReference ref = bbddProductosXCategoriaHogar.child(clave);
                                            ref.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference bbddProductosXCategoriaTecnologia = FirebaseDatabase.getInstance().getReference("ProductosXCategoria/Tecnologia");
                                Query q2 = bbddProductosXCategoriaTecnologia.orderByChild("uid").equalTo(uid);
                                q2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                            String clave = datasnapshot.getKey();
                                            DatabaseReference ref = bbddProductosXCategoriaTecnologia.child(clave);
                                            ref.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference bbddProductosXCategoriaCoches = FirebaseDatabase.getInstance().getReference("ProductosXCategoria/Coches");
                                Query q3 = bbddProductosXCategoriaCoches.orderByChild("uid").equalTo(uid);
                                q3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                            String clave = datasnapshot.getKey();
                                            DatabaseReference ref = bbddProductosXCategoriaCoches.child(clave);
                                            ref.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference bbddProductosXUsuario = FirebaseDatabase.getInstance().getReference("ProductosXUsuario/" + MainActivity.usuarioSesion.getUsuario());
                                bbddProductosXUsuario.removeValue();


                                //borrem el usuari de firebase
                                mAuth.getCurrentUser().delete();


                                Toast.makeText(getContext(), "Usuario eliminado, esperemos que regreses pronto", Toast.LENGTH_SHORT).show();
                                getActivity().finish();


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                adBuilder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = adBuilder1.create();
                alertDialog.show();


            default:
                break;
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
}

