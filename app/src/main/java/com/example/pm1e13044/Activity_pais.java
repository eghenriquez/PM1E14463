package com.example.pm1e13044;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm1e13044.Class.Transacciones;
import com.example.pm1e13044.Conexion.SQLiteConexion;

public class Activity_pais extends AppCompatActivity {
    EditText txtpais,txtcodigo;

    Button btnguardar,btnregresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);

        txtpais=(EditText) findViewById(R.id.txtpais);
        txtcodigo=(EditText) findViewById(R.id.txtcodigo);
        btnguardar=(Button) findViewById(R.id.btnguardar);
        btnregresa=(Button) findViewById(R.id.btnregresa);

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarpais();

            }
        });

        btnregresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para regresar a la actividad principal*/
                Intent intentregresa = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentregresa);
            }
        });

    }

    private void guardarpais(){
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.codigo,txtcodigo.getText().toString());
        valores.put(Transacciones.nombre_pais,txtpais.getText().toString());

        Long resultado = db.insert(Transacciones.TbPaises,Transacciones.codigo,valores);

        Toast.makeText(getApplicationContext(),"Registro #"+resultado.toString()+" Ingresado satisfactorio",Toast.LENGTH_LONG).show();
        db.close();

        clearPantalla();
    }

    private void clearPantalla() {
        txtpais.setText("");
        txtcodigo.setText("");
    }
}