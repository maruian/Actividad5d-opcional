package com.example.a2dam.actividad5a;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.a2dam.actividad5a.model.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MostrarUsuarios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MostrarUsuarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostrarTodosProductos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Producto> listadoProductos = new ArrayList<>();
    ArrayList<String> listadoUsuarios = new ArrayList<>();
    RecyclerView recyclerView;
    AdaptadorTodosProductos adaptadorTodosProductos;
    FragmentManager fm;

    private Spinner spCategoria, spUsuarios;
    private ImageView filtrarCategoria, filtrarUsuario;
    private TextView quitarFiltros;



    public MostrarTodosProductos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MostrarUsuarios.
     */
    // TODO: Rename and change types and number of parameters
    public static MostrarTodosProductos newInstance(String param1, String param2) {
        MostrarTodosProductos fragment = new MostrarTodosProductos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mostrar_todos_productos, container, false);

        fm = getFragmentManager();

        spUsuarios = v.findViewById(R.id.spUsuarios);
        spCategoria = v.findViewById(R.id.spCategoria);
        filtrarCategoria = v.findViewById(R.id.filtrarCategoria);
        filtrarUsuario = v.findViewById(R.id.filtrarUsuario);
        quitarFiltros = v.findViewById(R.id.quitarFiltros);

        recyclerView = v.findViewById(R.id.listaProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        //Obtenim el llistat de tots els usuaris
        DatabaseReference dbUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        dbUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    String usuario = datasnapshot.child("usuario").getValue().toString();
                    listadoUsuarios.add(usuario);
                }
                spUsuarios.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listadoUsuarios));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Afegim un listener a la imatge filtrar per categoria
        filtrarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference dbProductosXCategoria = FirebaseDatabase.getInstance().getReference("ProductosXCategoria/"+spCategoria.getSelectedItem().toString());
                //primer neteje el llistat de productes perque sino cada volta que fem click al listener
                //s'afegixen repetits al llistat
                listadoProductos.clear();
                dbProductosXCategoria.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                            Producto producto = datasnapshot.getValue(Producto.class);
                            listadoProductos.add(producto);
                        }
                        adaptadorTodosProductos = new AdaptadorTodosProductos(listadoProductos, fm);
                        recyclerView.setAdapter(adaptadorTodosProductos);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Afegim un listener a la imatge de filtrar usuari
        filtrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference dbProductosXUsuario = FirebaseDatabase.getInstance().getReference("ProductosXUsuario/"+spUsuarios.getSelectedItem().toString());
                listadoProductos.clear();
                dbProductosXUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                            Producto producto = datasnapshot.getValue(Producto.class);
                            listadoProductos.add(producto);
                        }
                        adaptadorTodosProductos = new AdaptadorTodosProductos(listadoProductos, fm);
                        recyclerView.setAdapter(adaptadorTodosProductos);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        // Mostrem el llistat inicial de productes
        DatabaseReference dbProductos = FirebaseDatabase.getInstance().getReference("Productos");
        dbProductos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()) {
                    listadoProductos.add(datasnapshot.getValue(Producto.class));
                }
                adaptadorTodosProductos = new AdaptadorTodosProductos(listadoProductos, fm);
                recyclerView.setAdapter(adaptadorTodosProductos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        quitarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listadoUsuarios.clear();
                fm.beginTransaction().detach(MostrarTodosProductos.this).attach(MostrarTodosProductos.this).commit();
            }
        });

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
