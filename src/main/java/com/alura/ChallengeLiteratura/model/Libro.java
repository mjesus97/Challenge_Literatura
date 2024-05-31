package com.alura.ChallengeLiteratura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Libros")
public class Libro {
    @Id
    private Long Id;
    private String titulo;
    private String idioma;
    @ManyToOne
    private Autor autor;
    public Libro(){}
    public Libro(DatosLibros datosLibros, Autor autor) {
        this.titulo = datosLibros.titulo();
        this.idioma = datosLibros.idioma().get(0);
        this.Id = datosLibros.Id();
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                ", titulo = '" + titulo + '\'' +
                ", autor = '" + autor.getNombre() + '\'' +
                ", idioma = " + idioma +
                '}';
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
