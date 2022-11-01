package com.finsol.tarea4pm1.tablas;

import java.sql.Blob;

public class Imagenes {
    public Integer id;
    public String image;

    //Constructor de la clase
    public Imagenes(){
        //Todo
    }

    public Imagenes(Integer id, String image) {
        this.id = id;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
