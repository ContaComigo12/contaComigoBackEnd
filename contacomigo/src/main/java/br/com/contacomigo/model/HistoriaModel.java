package br.com.contacomigo.model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class HistoriaModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private long id;

    private String titulo;

    private String categoria;

    private String subCategoria;

    @Lob
    private ArrayList<String> imgsHistoria = new ArrayList<String>(100);

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(String subCategoria) {
        this.subCategoria = subCategoria;
    }

    public ArrayList<String> getImgsHistoria() {
        return imgsHistoria;
    }

    public void setImgsHistoria(String imgsHistoria, int i) {
        this.imgsHistoria.add(i, (String.valueOf(imgsHistoria)));
    }


}
