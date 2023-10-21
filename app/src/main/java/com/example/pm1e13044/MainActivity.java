package com.example.pm1e13044;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /*Creacion de variables de los botones del main */
    Button btnpais, btncontacto,btnlista,btnsalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Asignacion de variables a botones del layout */
        btnpais=(Button) findViewById(R.id.btnpais);
        btncontacto=(Button) findViewById(R.id.btcontacto);
        btnlista=(Button) findViewById(R.id.btnlista);
        btnsalir=(Button) findViewById(R.id.btnsalir);

        /*creacion de eventos para los botones */
        btnpais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de agregar pais*/
                Intent intentpais = new Intent(getApplicationContext(), Activity_pais.class);
                startActivity(intentpais);
            }
        });

        btncontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de agregar contacto*/
                Intent intentcontacto = new Intent(getApplicationContext(), Activity_contactos.class);
                startActivity(intentcontacto);
            }
        });

        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de visualizar lista*/
                Intent intentlista = new Intent(getApplicationContext(), Activity_lista.class);
                startActivity(intentlista);
            }
        });

        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de salir del programa*/
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}