package com.alura.ChallengeLiteratura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ObtenerDatos(@JsonAlias("id") String id,@JsonAlias ("results") List<DatosLibros> resultado) {
}
