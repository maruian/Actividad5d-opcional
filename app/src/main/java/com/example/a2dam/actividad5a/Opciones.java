package com.example.a2dam.actividad5a;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Opciones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Opciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Opciones extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView alta, mostrar, mostrarTodos, salir;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private OnFragmentInteractionListener mListener;

    public Opciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Opciones.
     */
    // TODO: Rename and change types and number of parameters
    public static Opciones newInstance(String param1, String param2) {
        Opciones fragment = new Opciones();
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
        View v = inflater.inflate(R.layout.opciones, container, false);

        fm = getFragmentManager();

        alta = v.findViewById(R.id.btnAlta);
        alta.setOnClickListener(this);

        mostrar = v.findViewById(R.id.btnMostrar);
        mostrar.setOnClickListener(this);

        mostrarTodos = v.findViewById(R.id.btnMostrarTodos);
        mostrarTodos.setOnClickListener(this);

        salir = v.findViewById(R.id.btnSalir);
        salir.setOnClickListener(this);

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
        switch (view.getId()){
            case R.id.btnAlta:
                ft = fm.beginTransaction();
                if (mListener.estaFragmentDinamic()){
                    ft.replace(R.id.fragment_dinamic,AltaProducto.newInstance("",""));
                    ft.addToBackStack(null);
                } else {
                    ft.add(R.id.fragment_dinamic,AltaProducto.newInstance("",""));
                    ft.addToBackStack(null);
                }
                ft.commit();
                break;
            case R.id.btnMostrar:
                ft = fm.beginTransaction();
                if (mListener.estaFragmentDinamic()){
                    ft.replace(R.id.fragment_dinamic,MostrarProductos.newInstance("",""));
                    ft.addToBackStack(null);
                } else {
                    ft.add(R.id.fragment_dinamic,MostrarProductos.newInstance("",""));
                    ft.addToBackStack(null);
                }
                ft.commit();
                break;
            case R.id.btnMostrarTodos:
                ft = fm.beginTransaction();
                if (mListener.estaFragmentDinamic()){
                    ft.replace(R.id.fragment_dinamic,MostrarTodosProductos.newInstance("",""));
                    ft.addToBackStack(null);
                } else {
                    ft.add(R.id.fragment_dinamic,MostrarTodosProductos.newInstance("",""));
                    ft.addToBackStack(null);
                }
                ft.commit();
                break;

            case R.id.btnSalir:
                LoginActivity.firebaseAuth.signOut();
                getActivity().finish();
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
        boolean estaFragmentDinamic();
    }
}
