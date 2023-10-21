package com.example.pm1e13044.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pm1e13044.Activity_pais;
import com.example.pm1e13044.Class.Transacciones;

public class SQLiteConexion extends SQLiteOpenHelper {
    public SQLiteConexion(Context context,
                          String dbname,
                          SQLiteDatabase.CursorFactory factory,
                          int version){

        super(context,dbname,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Transacciones.CreateTablePaises);
        db.execSQL(Transacciones.CreateTableContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(Transacciones.DropTablePaises);
        db.execSQL(Transacciones.DropTableContactos);
        onCreate(db);
    }

}
