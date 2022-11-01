package com.finsol.tarea4pm1;

import static java.io.File.createTempFile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.finsol.tarea4pm1.configuracion.SQLiteConexion;
import com.finsol.tarea4pm1.tablas.Transacciones;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Declaracion de variables
    Blob imagen;
    Button btnSalvar, btnContactos;
    SQLiteConexion conexion;
    Intent intentList;

    //Declaracion de variables para la foto
    static final int peticion_captura_imagen = 100;
    static final int peticion_acceso_camara = 201;
    static final int peticion_acceso_foto = 200;
    ImageView objetoImagen;
    Button btntakepotho;
    String pathImage = "", imagenString="";
    Uri fotoUri;
    Button btnFoto;
    String id = "";
    Boolean extras = false;
    byte[] logoImage;

    static Bitmap global_Bitman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnContactos = findViewById(R.id.btnContactos);

        //Foto
        objetoImagen = findViewById(R.id.imgFoto);
        btnFoto = findViewById(R.id.btnFoto);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaciones()){
                    guardarRegistro();
                }
            }
        });

        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityList.class);
                startActivity(intent);
            }
        });

    }

    private void permisos() {
        //Validar si el permiso está otorgado para tomar foto
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //Otorgar el permiso si no se tiene
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, peticion_acceso_camara);
        }
        else{
            //tomarFoto();
            TakePhotoDir();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == peticion_captura_imagen)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            objetoImagen.setImageBitmap(imagen);

        }*/
        if (requestCode == peticion_captura_imagen && resultCode == RESULT_OK)
        {
            File foto = new File(pathImage);
            objetoImagen.setImageURI(Uri.fromFile(foto));

        }

    }

    private void TakePhotoDir()
    {
        Intent Intenttakephoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Intenttakephoto.resolveActivity(getPackageManager())!= null)
        {
            File foto  = null;

            try
            {
                foto = createImageFile();
            }catch (Exception ex)
            {
                ex.toString();
            }
            if(foto != null)
            {
                fotoUri = FileProvider.getUriForFile(this, "com.finsol.tarea4pm1.fileprovider",foto);
                Intenttakephoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(Intenttakephoto, peticion_captura_imagen);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */);
        // Save a file: path for use with ACTION_VIEW intents
        pathImage = image.getAbsolutePath();
        Log.i("FOTO", pathImage.toString());

        logoImage = getLogoImage(pathImage);
        Log.i("IMAGEN: ",imagenString);

        return image;
    }

    private void guardarRegistro() {

            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            //imagenString = convertirImgPngString(pathImage);
            ContentValues values = new ContentValues();
            values.put(Transacciones.image,pathImage);
        Log.i("FOTO ACTUALIZA", pathImage.toString());

            try {
                Long resultado = db.insert(Transacciones.TablaImagenes, Transacciones.id, values);
                Toast.makeText(getApplicationContext(), "Imagen Ingresado Correctamente. "+resultado.toString(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error al insertar. "+e.toString(), Toast.LENGTH_SHORT).show();
            }

            db.close();
            ClearScreem();

    }

    private void ClearScreem() {
        pathImage="";
        objetoImagen.setImageResource(R.drawable.contacto);
        btnSalvar.setText(R.string.btn_guardar);
    }


    private byte[] getLogoImage(String url)
    {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    private boolean validaciones() {
        Boolean validacion = true;
        if (pathImage.toString().length() <= 0) {
            mostrarAlerta("Debe agregar una foto");
            validacion = false;
        }
        return validacion;
    }

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(mensaje)
                .setTitle("Error")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}