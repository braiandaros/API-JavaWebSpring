# Projeto API Web — Gestão de Usuários com Autenticação JWT

Aplicação web Full Stack desenvolvida com arquitetura em camadas e segurança baseada em tokens (Stateless)[cite: 8, 9]. O projeto integra uma interface de acesso restrito no front-end[cite: 3, 5] com uma API REST em Java responsável por validar dados, criptografar senhas e proteger as rotas do servidor[cite: 8, 9].

---

## Tecnologias e Dependências (`pom.xml`)

No ecossistema Java, as bibliotecas do projeto são gerenciadas pelo arquivo `pom.xml` através do Maven[cite: 8]:

* **Spring Boot:** Atua como o "chassi" da aplicação, configurando automaticamente o servidor web (Tomcat) e gerenciando os componentes do projeto[cite: 8].
* **Spring Data JPA:** Funciona como o tradutor de banco de dados, permitindo salvar e buscar usuários sem a necessidade de escrever código SQL[cite: 8].
* **Spring Security:** Serve como o escudo da API, responsável por trancar as rotas e gerenciar o contexto de quem está logado[cite: 8].
* **JJWT (Java JWT):** Biblioteca responsável por fabricar, assinar e decodificar os tokens de segurança[cite: 8].
* **Jakarta Validation:** Atua como o fiscal de dados, utilizando anotações (como `@NotBlank` e `@Email`) para impedir que dados vazios ou inválidos cheguem ao banco[cite: 8].
* **Lombok:** Otimizador de código que escreve os métodos *getters*, *setters* e construtores nos bastidores através das anotações `@Getter` e `@Setter`, deixando as classes limpas[cite: 8].

---

## Arquitetura em Camadas (O Fluxo da Informação)

O projeto utiliza o padrão de camadas, onde cada pacote possui uma função única e estrita[cite: 8]:

1. **Model (`Usuario.java`) — A Planta Baixa:** Representa a tabela no banco de dados através da anotação `@Entity`[cite: 8]. Aplica regras do Jakarta Validation, como `@Size(min=3)`, garantindo que ninguém crie um usuário com um nome muito curto antes mesmo do código principal executar[cite: 8].
2. **Repository (`IUsuario.java`) — O Almoxarifado:** Interface que herda o `JpaRepository` e se comunica diretamente com o banco de dados[cite: 8]. Ao declarar métodos como `findByEmail(String email)`, o Spring Data JPA escreve a consulta SQL internamente e devolve o usuário pronto[cite: 8].
3. **Service (`UsuarioService.java`) — O Cérebro:** Concentra a regra de negócio e toma as decisões da API[cite: 8]. Antes de mandar o Repository salvar um novo usuário, o Service criptografa a senha digitada usando o `BCryptPasswordEncoder`, garantindo que a senha real não fique exposta no banco de dados[cite: 8].
4. **Controller (`UsuarioController.java`) — A Recepção:** Porta de entrada da API que mapeia as URLs (como `@RequestMapping("/usuarios")`) e define quais ações (`GET`, `POST`, `PUT`, `DELETE`) acionam quais métodos do Service[cite: 8]. Ele recebe a requisição HTTP da internet e devolve uma resposta (Status 200, 201, 204)[cite: 9].

---

## Sistema de Segurança (Stateless e JWT)

A API não guarda a sessão do usuário na memória, tornando a aplicação leve e escalável, com controle feito totalmente por tokens[cite: 9]:

* **O Padrão DTO (`UsuarioDto.java`):** Durante o login, a API não exige a entidade `Usuario` inteira (com telefone, etc.)[cite: 9]. Ela recebe um DTO (*Data Transfer Object*), que funciona como um objeto de transporte contendo apenas Nome, Email e Senha[cite: 9].
* **A Geração do Token (`TokenUtil.java`):** Se a senha coincidir com o hash do banco, o JJWT constrói um token contendo o e-mail do usuário (`setSubject`), define a validade de 12 horas e assina digitalmente usando uma chave secreta (`SECRET_KEY`) e o algoritmo `HS256`[cite: 9].
* **A Catraca (`SecurityConfig.java`):** Classe de configuração que define as regras de acesso: a rota de criação e a rota de login (`/usuarios/login`) recebem `permitAll()` (abertas ao público), enquanto qualquer outro pedido exige credencial através de `authenticated()`[cite: 9].
* **O Leitor de Crachás (`SecurityFilter.java`):** Nas rotas protegidas, este filtro intercepta a requisição antes de chegar ao Controller[cite: 9]. Ele procura o cabeçalho `Authorization`, extrai a palavra `Bearer`, descriptografa o token, verifica se não está expirado e libera o acesso no Spring Security[cite: 9].

---

## Tratamento de Erros

* **Captura de Falhas de Validação:** O `UsuarioController` possui um método com a anotação `@ExceptionHandler(MethodArgumentNotValidException.class)` para evitar códigos de erro confusos no front-end[cite: 9].
* **Respostas em JSON:** Caso um usuário envie um JSON sem a senha, o Jakarta Validation acusa o erro e o método captura a falha, devolvendo um JSON limpo e amigável (ex: `{"senha": "A senha é obrigatorio"}`) com Status 400 (`Bad Request`)[cite: 9].
* **Autenticação no Front-end:** Na tela de login (`localhost:8080/index.html`), tentativas de acesso não autorizadas (Status 401) exibem o aviso visual `"E-mail ou senha inválidos."`[cite: 5].

---

## Resumo das Operações HTTP

| Método | Rota | Função | Resposta / Acesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/usuarios/login` | Recebe o `UsuarioDto` e gera o token JWT[cite: 9] | Público (`permitAll()`)[cite: 9] |
| `POST` | `/usuarios` | Salva um novo usuário com senha criptografada via BCrypt[cite: 8] | Status 201[cite: 9] |
| `GET` | `/usuarios` | Busca os usuários no banco de dados[cite: 8] | Status 200 / Exige `Bearer` token[cite: 9] |
| `PUT` | `/usuarios` | Atualiza dados de usuários[cite: 8] | Status 200 / Exige `Bearer` token[cite: 9] |
| `DELETE` | `/usuarios` | Remove registros de usuários[cite: 8] | Status 204 / Exige `Bearer` token[cite: 9] |
