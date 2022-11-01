package com.finsol.tarea4pm1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.finsol.tarea4pm1.configuracion.SQLiteConexion;
import com.finsol.tarea4pm1.tablas.Transacciones;

import java.io.ByteArrayInputStream;
import java.io.File;

public class Activity_Imagen extends AppCompatActivity {
    //Blob
    byte[] accImage;
    ImageView imagen;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        imagen = findViewById(R.id.image);
        //Verificando si enviaron datos
        Bundle datos = getIntent().getExtras();
         String id = ""+datos.getString("id");
        getCurrentAccount(id);
    }

    public void getCurrentAccount(String id) {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        String sql = "SELECT * FROM "+Transacciones.TablaImagenes+ " where id = "+id;

        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            path = cursor.getString(1);
            Log.i("FOTO ACTUALIZA", path);
        }
        
       db.close();
        try{
            File foto = new File(path);
            imagen.setImageURI(Uri.fromFile(foto));
        }catch (Exception e){
            Toast.makeText(Activity_Imagen.this, "Error al cargar imagen...", Toast.LENGTH_SHORT ).show();
        }


    }
}