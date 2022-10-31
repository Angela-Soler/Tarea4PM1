package com.finsol.tarea4pm1.tablas;

import java.sql.Blob;

public class Transacciones {
    //Nombre de la Base de Datos
    public static final String NameDatabase = "PMT4DB1";

    /*Creacion de la base de Datos*/
    public static final String TablaImagenes = "imagenes";

    /*Creación de la tabla Imagenes*/
    public static final String id = "id";
    public static final String image = "image";

    //DDL
    public static final String createTableImagenes = "CREATE TABLE "+Transacciones.TablaImagenes+
            " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "image BLOB)";

    public static final String GetImagenes = "SELECT * FROM "+Transacciones.TablaImagenes;

    public static final String DropTableImagenes = "DROP TABLE IF EXISTS Imagenes";

    public static final String DeleteRegistro = "DELETE FROM "+TablaImagenes;

}

