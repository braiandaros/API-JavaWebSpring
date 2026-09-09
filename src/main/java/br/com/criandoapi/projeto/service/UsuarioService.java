package br.com.criandoapi.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.repository.IUsuario;

@Service
public class UsuarioService {
	private IUsuario repository;
	
	public UsuarioService(IUsuario repository) {
		this.repository = repository;
	}
	
	public List<Usuario> ListarUsuario(){
		List<Usuario> lista = repository.findAll();
		return lista;
	}
	
	public Usuario CriarUsuario(Usuario usuario) {
		Usuario usuarioNovo = repository.save(usuario);
		return usuarioNovo;
	}
	
	public Usuario EditarUsuario(Usuario usuario) {
		Usuario usuarioNovo = repository.save(usuario);
		return usuarioNovo;
	}
	
	public Boolean DeletarUsuario(Integer id) {
		repository.deleteById(id);
		return true;
	}
}
