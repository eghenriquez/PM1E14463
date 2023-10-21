package com.example.pm1e13044;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e13044.Class.Paises;
import com.example.pm1e13044.Class.Transacciones;
import com.example.pm1e13044.Conexion.SQLiteConexion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_contactos extends AppCompatActivity {


    EditText txtnombre,txttelefono,txtnota;
    Button btnguardarc,btnregresac,btnimagen;
    Spinner spinnerpais;
    ImageView imagenview;

    static final int PETICION_CAMARA = 100;
    static final int TAKE_PIC_REQUEST = 101;
    Bitmap imagen;
    int PaisSelect;

    ArrayList<Paises> lista;
    ArrayList<String> lista_paises;
    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        txtnombre=(EditText) findViewById(R.id.txtnombre);
        txttelefono=(EditText) findViewById(R.id.txttelefono);
        txtnota=(EditText) findViewById(R.id.txtnota);
        spinnerpais=(Spinner) findViewById(R.id.spinnerpais);
        btnimagen=(Button) findViewById(R.id.btnimagen);
        btnguardarc=(Button) findViewById(R.id.btnguardarc);
        imagenview=(ImageView) findViewById(R.id.imagenview);
        btnregresac=(Button) findViewById(R.id.btnregresac);

        btnimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permisos();
            }
        });

        listaPaises();
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,lista_paises);
        spinnerpais.setAdapter(adp);
        spinnerpais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cadena = adapterView.getSelectedItem().toString();
                PaisSelect=Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnguardarc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validarDatos();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Debe de tomarse la foto.",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnregresac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para regresar a la actividad principal*/
                Intent intentregresa = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentregresa);
            }
        });


    }

    //Metodo que valida el ingreso de datos
    private void validarDatos() {
        if (lista_paises.size() == 0){
            Toast.makeText(getApplicationContext(), "Debe de ingresar un Pais" ,Toast.LENGTH_LONG).show();
        }else  if (txtnombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (txttelefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (txtnota.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir una nota" ,Toast.LENGTH_LONG).show();
        }else{
            guardarContacto(imagen);
        }
    }

    //Metodo que pide permisos para tomar fotos
    private void Permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_CAMARA);
        }else{
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(takepic.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takepic,TAKE_PIC_REQUEST);
            }
        }
    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }
    @Override
    protected void onActivityResult(int requescode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requescode, resultCode, data);

        if(requescode == TAKE_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            imagenview.setImageBitmap(imagen);
        }else if (resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            imagenview.setImageURI(imageUri);
        }
    }

    /*Metodo que guarda los contactos*/
    private void guardarContacto(Bitmap bitmap) {
        db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(String.valueOf(Transacciones.foto),ArrayFoto);
        valores.put(Transacciones.pais, PaisSelect);
        valores.put(Transacciones.nombre, txtnombre.getText().toString());
        valores.put(Transacciones.telefono, txttelefono.getText().toString());
        valores.put(Transacciones.nota, txtnota.getText().toString());


        Long resultado = db.insert(Transacciones.TbContactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro #" + resultado.toString() + "Ingresado"
                ,Toast.LENGTH_LONG).show();

        db.close();


        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    private void listaPaises() {
        Paises pais = null;
        lista = new ArrayList<Paises>();
        db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TbPaises,null);

        while (cursor.moveToNext()) {
            pais = new Paises();
            pais.setCodigo(cursor.getString(0));
            pais.setNombrePais(cursor.getString(1));
            lista.add(pais);
        }
        cursor.close();
        fillCombo();
    }

    private void fillCombo() {
        lista_paises = new ArrayList<String>();
        for (int i=0; i < lista.size();i++) {
            lista_paises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }

}