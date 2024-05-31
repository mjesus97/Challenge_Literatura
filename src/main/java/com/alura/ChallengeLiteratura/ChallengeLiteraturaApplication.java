package com.alura.ChallengeLiteratura;

import com.alura.ChallengeLiteratura.principal.Principal;
import com.alura.ChallengeLiteratura.repositorio.AutorRepository;
import com.alura.ChallengeLiteratura.repositorio.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraturaApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository repositoryLibros;
	@Autowired
	private AutorRepository repositoryAutores;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal menu = new Principal(repositoryLibros,repositoryAutores);
		menu.menuaplication();
	}
}
