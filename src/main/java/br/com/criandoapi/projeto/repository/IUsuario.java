package br.com.criandoapi.projeto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.criandoapi.projeto.model.Usuario;

public interface IUsuario extends JpaRepository<Usuario, Integer>{

	Usuario findBynomeOrEmail(String nome, String email);

	Optional<Usuario> findByEmail(String email);
	
}
