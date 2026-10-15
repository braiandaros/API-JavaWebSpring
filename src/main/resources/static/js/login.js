/**
 * 
 */const form = document.getElementById("formLogin");
const emailInput = document.getElementById("email");
const senhaInput = document.getElementById("senha");
const mensagemDiv = document.getElementById("mensagem");

async function logar(event) {
    event.preventDefault(); 
    
    mensagemDiv.textContent = "Autenticando...";
    mensagemDiv.className = "";

    // O seu UsuarioDto no backend espera nome, email e senha.
    // Enviamos o email no campo 'nome' também para o método findBynomeOrEmail funcionar perfeitamente.
    const dados = {
        nome: emailInput.value,
        email: emailInput.value,
        senha: senhaInput.value
    };

    try {
        const response = await fetch("http://localhost:8080/usuarios/login", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            // Se o login der certo, a API devolve um JSON com o token!
            const retornoDaApi = await response.json();
            
            // O cofre mágico do navegador: salva o token para usarmos nas próximas telas
            localStorage.setItem("meuTokenJWT", retornoDaApi.token);
			
			window.location.href = "painel.html";

            mensagemDiv.textContent = "Login aprovado! Token salvo com sucesso.";
            mensagemDiv.className = "sucesso";

            // Se você já tiver a outra tela pronta, o JS pode trocar de página sozinho:
            // setTimeout(() => { window.location.href = "cadastro.html"; }, 1500);

        } else {
            // Se o status for diferente de 200/ok
            mensagemDiv.textContent = "E-mail ou senha inválidos.";
            mensagemDiv.className = "erro";
        }

    } catch (error) {
        mensagemDiv.textContent = "Servidor offline ou erro de conexão.";
        mensagemDiv.className = "erro";
        console.error(error);
    }
}

form.addEventListener('submit', logar);