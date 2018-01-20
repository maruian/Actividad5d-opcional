package com.example.a2dam.actividad5a;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a2dam.actividad5a.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//Esta clase no la estic utilitzant


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AltaUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AltaUsuario extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button guardar;
    private EditText text_usuario, text_correo, text_nombre, text_apellidos, text_direccion;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AltaUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AltaUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static AltaUsuario newInstance(String param1, String param2) {
        AltaUsuario fragment = new AltaUsuario();
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
        View v = inflater.inflate(R.layout.alta_usuario, container, false);


        text_usuario = v.findViewById(R.id.etNombreUsuario);
        text_correo = v.findViewById(R.id.etCorreoElectronico);
        text_nombre = v.findViewById(R.id.etNombre);
        text_apellidos = v.findViewById(R.id.etApellidos);
        text_direccion = v.findViewById(R.id.etDireccion);
        guardar = v.findViewById(R.id.guardar);



        guardar.setOnClickListener(this);
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

                final DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("Usuarios");

                String usuario = text_usuario.getText().toString();
                String nombre = text_nombre.getText().toString();
                String correo = text_correo.getText().toString();
                String apellidos = text_apellidos.getText().toString();
                String direccion = text_direccion.getText().toString();


                if (!TextUtils.isEmpty(usuario) &&
                        !TextUtils.isEmpty(nombre) &&
                        !TextUtils.isEmpty(apellidos) &&
                        !TextUtils.isEmpty(correo) &&
                        !TextUtils.isEmpty(direccion)) {
                    final Usuario u = new Usuario(usuario, correo, nombre, apellidos, direccion,"","");
                    //comprovem que no existeix eixe usuari
                    //i si no existeix el donem d'alta
                    final String clave = bbdd.push().getKey();
                    Query q = bbdd.orderByChild("usuario").equalTo(u.getUsuario());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        int cont=0;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                cont++;
                            }
                            if (cont>0){
                                Toast.makeText(getContext(), "Este usuario ya existe, elige otro usuario", Toast.LENGTH_SHORT).show();
                            } else {
                                bbdd.child(clave).setValue(u);
                                Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Debes introducir datos correctos", Toast.LENGTH_SHORT).show();
                }
                break;
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
