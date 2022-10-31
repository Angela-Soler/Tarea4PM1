package com.finsol.tarea4pm1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.finsol.tarea4pm1.configuracion.SQLiteConexion;
import com.finsol.tarea4pm1.tablas.Transacciones;

import java.io.ByteArrayInputStream;

public class Activity_Imagen extends AppCompatActivity {
    //Blob
    byte[] accImage;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        imagen = findViewById(R.id.image);
        getCurrentAccount();
    }

    public void getCurrentAccount() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        String sql = "SELECT * FROM "+Transacciones.TablaImagenes+ " where id = 1";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            accImage = cursor.getBlob(1);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();

        ByteArrayInputStream imageStream = new ByteArrayInputStream(accImage);
        Bitmap theImage= BitmapFactory.decodeStream(imageStream);

        imagen.setImageBitmap(theImage);
        Log.i("IMAGEN: ",imagen.toString());

    }
}