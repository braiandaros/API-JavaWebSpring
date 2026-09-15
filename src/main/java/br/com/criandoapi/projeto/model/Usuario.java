package br.com.criandoapi.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

// @Data //Tem get set e construtores

@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Size(min = 3, message = "O nome deve ter no minimo e caracteres")
	@NotBlank(message = "O nome é obrigatorio")
	@Column(name = "nome_completo", length = 200, nullable = false)
	private String nome;
	
	@Email(message = "Insira um Email valido")
	@NotBlank(message = "O email é obrigatorio")
	@Column(name = "email", length = 50, nullable = false)
	private String email;
	
	@NotBlank(message = "A senha é obrigatorio")
	@Column(name = "senha", columnDefinition = "TEXT", nullable = false)
	private String senha;
	
	@NotBlank(message = "O telefone é obrigatorio")
	@Column(name = "telefone", length = 15, nullable = false)
	private String telefone;
	
	/* public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	} */
	
}

