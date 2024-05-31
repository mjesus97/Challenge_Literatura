package com.alura.ChallengeLiteratura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(@JsonAlias("title") String titulo,
                         // @JsonAlias("authors") String autor,
                          @JsonAlias("languages") List<String> idioma,
                          @JsonAlias("id") Long Id) {
}
