package com.alura.ChallengeLiteratura.model;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private LocalDate fechaNacimiento;
    private LocalDate fechaMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}
    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaMuerte = datosAutor.anoMuerte();
        this.fechaNacimiento = datosAutor.anoNacimiento();
    }

    @Override
    public String toString() {
        return "Autor{" +
                "nombre = '" + nombre + '\'' +
                ", fecha de nacimiento = " + fechaNacimiento +
                ", fecha de muerte = " + fechaMuerte +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(LocalDate fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Libro libro) {
        this.libros.add(libro);
    }
}
