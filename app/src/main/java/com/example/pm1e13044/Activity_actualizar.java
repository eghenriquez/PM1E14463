package com.example.pm1e13044;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e13044.Class.Paises;
import com.example.pm1e13044.Class.Transacciones;
import com.example.pm1e13044.Conexion.SQLiteConexion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_actualizar extends AppCompatActivity {

    int codigoPaisSeleccionado;
    EditText txtnombreact,txttelefonoact,txtnotaact,txtcodigocontacto;
    Spinner spinnerpaisact;
    Button btnact,btnregresaact;
    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
    ArrayList<String> lista_paises;
    ArrayList<Paises> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        txtcodigocontacto = (EditText) findViewById(R.id.txtcodigocontacto);
        txtnombreact=(EditText) findViewById(R.id.txtnombreact);
        txttelefonoact=(EditText) findViewById(R.id.txttelefonoact);
        txtnotaact=(EditText) findViewById(R.id.txtnotaact);
        btnregresaact=(Button) findViewById(R.id.btnregresaact);
        btnact=(Button) findViewById(R.id.btnact);
        spinnerpaisact=(Spinner) findViewById(R.id.spinnerpaisact);

        txtcodigocontacto.setText(getIntent().getStringExtra("codigo"));
        txtnombreact.setText(getIntent().getStringExtra("nombre"));
        txttelefonoact.setText(getIntent().getStringExtra("telefono"));
        txtnotaact.setText(getIntent().getStringExtra("nota"));
        ObtenerListaPaises();
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,lista_paises);
        spinnerpaisact.setAdapter(adp);
        spinnerpaisact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cadena = adapterView.getSelectedItem().toString();
                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnregresaact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_lista.class);
                startActivity(intent);
            }
        });

        btnact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarContacto();
            }
        });
    }


    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }

    private void ObtenerListaPaises() {
        Paises pais = null;
        lista = new ArrayList<Paises>();
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TbPaises,null);
        while (cursor.moveToNext())
        {
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
        for (int i=0; i<lista.size();i++) {
            lista_paises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }

    private void EditarContacto() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String ObjCodigo = txtcodigocontacto.getText().toString();
        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, txtnombreact.getText().toString());
        valores.put(Transacciones.telefono, txttelefonoact.getText().toString());
        valores.put(Transacciones.nota, txtnotaact.getText().toString());
        valores.put(Transacciones.pais, codigoPaisSeleccionado);

        try {
            db.update(Transacciones.TbContactos,valores, Transacciones.id +" = "+ ObjCodigo, null);
            db.close();
            Toast.makeText(getApplicationContext(),"Registro Actualizado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Activity_lista.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"No se actualizo", Toast.LENGTH_SHORT).show();
        }
    }
}