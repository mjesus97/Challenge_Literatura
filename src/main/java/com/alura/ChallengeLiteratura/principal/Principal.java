package com.alura.ChallengeLiteratura.principal;

import com.alura.ChallengeLiteratura.model.*;
import com.alura.ChallengeLiteratura.repositorio.AutorRepository;
import com.alura.ChallengeLiteratura.repositorio.LibroRepository;
import com.alura.ChallengeLiteratura.service.ConsumoAPI;
import com.alura.ChallengeLiteratura.service.ConvierteDatos;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal{
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/?";
    private ConvierteDatos conversor = new ConvierteDatos();

    private JSONParser jsonParser = new JSONParser() ;
    private LibroRepository repositorioLibros;
    private AutorRepository repositorioAutores;
    private Optional<Autor> autor;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Principal(LibroRepository repositorioLibros, AutorRepository repositorioAutores) {
        this.repositorioLibros = repositorioLibros;
        this.repositorioAutores = repositorioAutores;
    }

    public void menuaplication() throws ParseException, java.text.ParseException {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    **************************************************
                                        
                    1 - Añadir libro a la base de datos
                    2 - Mostrar todos los libros de la base de datos
                    3 - Mostrar los autores en la base de datos
                    4 - Mostrar los autores vivos en un año
                    5 - Mostrar los libros de un idioma
                                                      
                    0 - Salir
                                        
                    *************************************************
                    """;
            System.out.println(menu);
            try{
                opcion = teclado.nextInt();
            }catch(InputMismatchException e){
                //opcion no valida
            }
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarlibro();
                    break;
                case 2:
                    getLibros();
                    break;
                case 3:
                    getAutores();
                    break;
                case 4:
                    getautoresVivos();
                    break;
                case 5:
                    getIdioma();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void getIdioma() {
        imprimirIdiomasdisponibles();

        System.out.println("Ingrese el idioma");
        String idioma = teclado.nextLine();
        List<Libro> libros = repositorioLibros.findByIdiomaEquals(idioma);
        System.out.println("Libros encontrados: ");
        libros.stream().forEach(System.out::println);
        System.out.println("\n");
    }
    private void imprimirIdiomasdisponibles(){
        List<String> listaIdiomas= repositorioLibros.findAll().stream()
                .map(e->e.getIdioma())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Los idiomas disponibles son:");
        for (String elem : listaIdiomas){
            switch (elem){
                case "es":
                    System.out.println("es: Español");
                    break;
                case "en":
                    System.out.println("en: Ingles");
                    break;
                default:
                    System.out.println(elem);
            }
        }
        System.out.println("\n");
    }

    private void getautoresVivos() throws java.text.ParseException {
        System.out.println("A continuación ingrese el perido que quiere analizar");
        try {
            System.out.println("Ingrese el año");
            Integer anoInicial = teclado.nextInt();

            List<Autor> autoresVivos =  repositorioAutores.findByFechaMuerteGreaterThanEqualAndFechaNacimientoLessThan(anoInicial, anoInicial);
            autoresVivos.stream().forEach(System.out::println);
        }catch (InputMismatchException e){
            System.out.println("Año no válido");
        }
        System.out.println("\n");

    }

    private void getLibros(){
        List<Libro> libros = repositorioLibros.findAll();
        libros.stream().forEach(System.out::println);
    }
    private void getAutores(){
        List<Autor> autores = repositorioAutores.findAll();
        autores.stream().forEach(System.out::println);
    }
    private void buscarlibro() throws ParseException {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        //pedimos el libro a la api
        String json = consumoAPI.obtenerDatos(URL_BASE +"search="+ nombreLibro.replace(" ","+"));
        // la respuesta trae varios valores que separamos
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        // verificamos que hayan libros en la respuesta
        Long cantidadLibrosencontrados =(Long) jsonObject.get("count");

        if (cantidadLibrosencontrados < 1){
            // si n hay libros no devuelve nada
            System.out.println(cantidadLibrosencontrados + " libros encontrados, se mas especifico en tu busqueda");
        }else{
            // si hay libros guarda el primero en la base de datos
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");
            JSONObject jsonLibro = (JSONObject) jsonArray.get(0);
            JSONObject jsonAutor = (JSONObject) ((JSONArray) jsonLibro.get("authors")).get(0) ;
            Autor autor = obtenerAutor(jsonAutor);
            DatosLibros datos = conversor.obtenerDatos(jsonLibro.toString(), DatosLibros.class);
            Libro libro = new Libro(datos, autor);
            repositorioLibros.save(libro);
            System.out.println("Se ha añadido el libro '" + libro.getTitulo() + "' a la base de datos");
        }

    }
    private Autor obtenerAutor(JSONObject autor){
        String nombre = autor.get("name").toString();
        Optional<Autor> autorencontrado = Optional.ofNullable(null);
        try {
            autorencontrado = repositorioAutores.findByNombreContainsIgnoreCase(nombre);
        }catch(NullPointerException e){
            System.out.println("El repositorio de autores esta vacio");
        }
        if (autorencontrado.isPresent()){
            return autorencontrado.get();
        }else{
            DatosAutor datosAutor = conversor.obtenerDatos(autor.toString(), DatosAutor.class);
            Autor autornuevo = new Autor(datosAutor);
            repositorioAutores.save(autornuevo);
            System.out.println("Se ha añadido el autor " + autornuevo + " a la base de datos");
            return autornuevo;
        }

    }



}
