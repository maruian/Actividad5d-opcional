package com.example.a2dam.actividad5a;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a2dam.actividad5a.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RegistrarUsuario extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    DatabaseReference bbdd;

    EditText usuario, correo, nombre, apellidos, direccion, password, passwordRepe;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);

        usuario = findViewById(R.id.etNombreUsuario);
        correo = findViewById(R.id.etCorreoElectronico);
        nombre = findViewById(R.id.etNombre);
        apellidos = findViewById(R.id.etApellidos);
        direccion = findViewById(R.id.etDireccion);
        password = findViewById(R.id.etPassword);
        passwordRepe = findViewById(R.id.etRepetirPassword);
        bbdd = FirebaseDatabase.getInstance().getReference("Usuarios");

        registrar = findViewById(R.id.btnRegistrar);
        registrar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        //ens quedem amb els valors dels camps per a passarlos als listeners
        final String txt_password = password.getText().toString();
        final String txt_passwordRepe = passwordRepe.getText().toString();
        final String txt_correo = correo.getText().toString();
        final String txt_nombre = nombre.getText().toString();
        final String txt_apellidos = apellidos.getText().toString();
        final String txt_direccion = direccion.getText().toString();
        final String txt_usuario = usuario.getText().toString();

        // Si algun camp esta vuit ho notificarem
        if ((!TextUtils.isEmpty(txt_usuario)) &&
                (!TextUtils.isEmpty(txt_correo)) &&
                (!TextUtils.isEmpty(txt_nombre)) &&
                (!TextUtils.isEmpty(txt_apellidos)) &&
                (!TextUtils.isEmpty(txt_direccion)) &&
                (!TextUtils.isEmpty(txt_password) && (!TextUtils.isEmpty(txt_passwordRepe)))) {

            //Si les dos contrasenyes no coincideixen
            //També ho notificarem
            if (txt_password.equals(txt_passwordRepe)) {

                //comprovem que no existeix eixe usuari
                //i si no existeix el donem d'alta
                Query q = bbdd.orderByChild("usuario").equalTo(txt_usuario);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    int cont = 0;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            cont++;
                        }
                        if (cont > 0) {
                            Toast.makeText(RegistrarUsuario.this, "Este usuario ya existe, elige otro usuario", Toast.LENGTH_SHORT).show();
                        } else {
                            // si no existeix el donem d'alta
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.createUserWithEmailAndPassword(txt_correo, txt_password).
                                    addOnCompleteListener(RegistrarUsuario.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String clave = bbdd.push().getKey();
                                                String uid = firebaseAuth.getCurrentUser().getUid();
                                                Usuario u = new Usuario(txt_usuario, txt_correo, txt_nombre, txt_apellidos, txt_direccion, uid, clave);
                                                bbdd.child(clave).setValue(u);
                                                Toast.makeText(getApplicationContext(), "Registro completo", Toast.LENGTH_SHORT).show();

                                                //Tanquem la finestra de registre
                                                finish();
                                            } else {
                                                //Si la tasca no s'ha completat correctament, mostrem els missatges corresponents
                                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(getApplicationContext(), "La direccion de correo electronico ya existe",
                                                            Toast.LENGTH_SHORT).show();
                                                } else if (task.getException() instanceof FirebaseAuthWeakPasswordException){
                                                    Toast.makeText(getApplicationContext(), "La contraseña es debil", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error en el registro: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show();

    }

}

