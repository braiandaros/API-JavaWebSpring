package br.com.criandoapi.projeto.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.criandoapi.projeto.dto.UsuarioDto;
import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.repository.IUsuario;
import br.com.criandoapi.projeto.security.Token;
import br.com.criandoapi.projeto.security.TokenUtil;
import jakarta.validation.Valid;

@Service
public class UsuarioService {
	private IUsuario repository;
	private PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	
	public UsuarioService(IUsuario repository) {
		this.repository = repository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public List<Usuario> ListarUsuario(){
		logger.info("Usuario: " + getLogado() + " Listando Usuarios");
		List<Usuario> lista = repository.findAll();
		return lista;
	}
	
	public Usuario CriarUsuario(Usuario usuario) {
		String encoder = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encoder);
		Usuario usuarioNovo = repository.save(usuario);
		logger.info("Usuario: " + getLogado() + " Criando Usuarios");
		return usuarioNovo;
	}
	
	public Usuario EditarUsuario(Usuario usuario) {
		String encoder = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encoder);
		Usuario usuarioNovo = repository.save(usuario);
		logger.info("Usuario: " + getLogado() + " Id: " + getLogadoId() + " Editando Usuario: " + usuario.getNome() + " Id: " + usuario.getId() + " Email: " + usuario.getEmail());
		return usuarioNovo;
	}
	
	public Boolean DeletarUsuario(Integer id) {
		repository.deleteById(id);
		logger.info("Usuario: " + getLogado() + " Deletando Usuarios");
		return true;
	}

	public Boolean validarSenha(Usuario usuario) {
		String senha = repository.findById(usuario.getId()).get().getSenha();
		boolean valid = passwordEncoder.matches(usuario.getSenha(), senha);
		return valid;
	}

	public Token gerarToken(@Valid UsuarioDto usuario) {
		Usuario user = repository.findBynomeOrEmail(usuario.getNome(), usuario.getEmail());
		if(user != null) {
			Boolean valid = passwordEncoder.matches(usuario.getSenha(), user.getSenha());
			if(valid) {
				return new Token(TokenUtil.createToken(user));
			}
		}
		return null;
	}
	
	private String getLogado() {
		Authentication userLogado = SecurityContextHolder.getContext().getAuthentication();
		if(!(userLogado instanceof AnonymousAuthenticationToken)) {
			return userLogado.getName();
		}
		return "null";
	}
	
	private String getLogadoId() {
		Authentication userLogado = SecurityContextHolder.getContext().getAuthentication();
		if (!(userLogado instanceof AnonymousAuthenticationToken)) {
	        String email = userLogado.getName(); 
	        Optional<Usuario> usuarioOpt = repository.findByEmail(email); 
	        if (usuarioOpt.isPresent()) {
	            return String.valueOf(usuarioOpt.get().getId());
	        }
	    }
		return "null";
	}
}
