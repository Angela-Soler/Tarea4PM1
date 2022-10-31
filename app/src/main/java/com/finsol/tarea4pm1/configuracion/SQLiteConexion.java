package com.finsol.tarea4pm1.configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.finsol.tarea4pm1.tablas.Transacciones;


public class SQLiteConexion extends SQLiteOpenHelper{

    //Constructor SQLite
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){

        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creacion de Objetos de la BD
        sqLiteDatabase.execSQL(Transacciones.createTableImagenes);//Creando tabla de contactos
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Eliminamos la data y las tablas de la aplicacion
        sqLiteDatabase.execSQL(Transacciones.DropTableImagenes);
        onCreate(sqLiteDatabase);
    }

}
