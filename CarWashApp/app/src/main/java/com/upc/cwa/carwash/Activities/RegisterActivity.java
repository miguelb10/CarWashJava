package com.upc.cwa.carwash.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.upc.cwa.carwash.Entities.Cliente;
import com.upc.cwa.carwash.R;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_register);

        final EditText txtNombre = findViewById(R.id.txtNombre);
        final EditText txtApellido = findViewById(R.id.txtApellido);
        final EditText txtContrasenia = findViewById(R.id.txtContrasenia);
        final EditText txtDepartamento = findViewById(R.id.txtDepartamento);
        final EditText txtCorreo = findViewById(R.id.txtCorreo);
        final EditText txtTipoDoc = findViewById(R.id.txtTipoDoc);
        final EditText txtNumDoc = findViewById(R.id.txtNumDoc);

        Button btnRegistrar = findViewById(R.id.btnSaveRegistro);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cliente objCliente = new Cliente();
                objCliente.nombre = txtNombre.getText().toString();
                objCliente.apellido = txtApellido.getText().toString();
                objCliente.contrasenia = txtContrasenia.getText().toString();
                objCliente.departamento = txtDepartamento.getText().toString();
                objCliente.email = txtCorreo.getText().toString();
                objCliente.tipo_documento = txtTipoDoc.getText().toString();
                objCliente.num_documento = txtNumDoc.getText().toString();

                AndroidNetworking.post("http://192.168.1.4:8090/api/cliente/register")
                        .addApplicationJsonBody(objCliente) // posting java object
                        .setTag("cliente")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.println(Log.ASSERT, "Success", "It worked!!!");
                                Intent intent =
                                        new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.println(Log.ERROR, "Error",
                                        "Some error!!" + error.getErrorBody()
                                                + " - " + error.getErrorDetail()
                                                + " - " + error.getResponse());
                            }
                        });

            }
        });
    }
}
