package com.alura.ChallengeLiteratura.repositorio;

import com.alura.ChallengeLiteratura.model.Autor;
import com.alura.ChallengeLiteratura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    List<Libro> findByIdiomaEquals(String idioma);


}
