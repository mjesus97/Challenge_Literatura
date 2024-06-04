package com.alura.ChallengeLiteratura.repositorio;

import com.alura.ChallengeLiteratura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    Optional<Autor> findByNombreContainsIgnoreCase(String nombreAutor);

    List<Autor> findByFechaMuerteGreaterThanEqualAndFechaNacimientoLessThan(Integer anoMuerte, Integer anoNacimiento);
}
