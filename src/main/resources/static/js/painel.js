const btnCarregar = document.getElementById("btnCarregar");
const btnSair = document.getElementById("btnSair");
const listaUsuarios = document.getElementById("listaUsuarios");
const mensagemDiv = document.getElementById("mensagem");

const formEditar = document.getElementById("formEditar");
const btnCancelar = document.getElementById("btnCancelar");

const token = localStorage.getItem("meuTokenJWT");

if (!token) {
    window.location.href = "index.html";
}

// 1. LISTAR USUÁRIOS (GET)
async function carregarUsuarios() {
    mensagemDiv.textContent = "Consultando banco de dados...";
    mensagemDiv.className = "";
    listaUsuarios.innerHTML = "";

    try {
        const response = await fetch("http://localhost:8080/usuarios", {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            const usuarios = await response.json();
            mensagemDiv.textContent = `Total de usuários cadastrados: ${usuarios.length}`;
            mensagemDiv.className = "sucesso";

            usuarios.forEach(user => {
                const li = document.createElement("li");
                li.style = "padding: 12px; border-bottom: 1px solid rgb(80, 80, 80); color: #cccccc; font-size: 14px; display: flex; justify-content: space-between; align-items: center;";

                const infoDiv = document.createElement("div");
                infoDiv.innerHTML = `<strong>ID:</strong> ${user.id} | <strong>Nome:</strong> ${user.nome} <br>
                                     <strong>E-mail:</strong> ${user.email} | <strong>Tel:</strong> ${user.telefone || 'N/A'}`;

                const botoesDiv = document.createElement("div");
                botoesDiv.style = "display: flex; gap: 8px;";

                // Botão Editar
                const btnEditar = document.createElement("button");
                btnEditar.textContent = "✏️ Editar";
                btnEditar.style = "background-color: #ff9800; padding: 6px 10px; font-size: 12px; width: auto;";
                btnEditar.onclick = () => abrirFormularioEdicao(user);

                // Botão Excluir
                const btnExcluir = document.createElement("button");
                btnExcluir.textContent = "🗑️ Excluir";
                btnExcluir.style = "background-color: #e53935; padding: 6px 10px; font-size: 12px; width: auto;";
                btnExcluir.onclick = () => excluirUsuario(user.id, user.nome);

                botoesDiv.appendChild(btnEditar);
                botoesDiv.appendChild(btnExcluir);

                li.appendChild(infoDiv);
                li.appendChild(botoesDiv);
                listaUsuarios.appendChild(li);
            });
        } else {
            localStorage.removeItem("meuTokenJWT");
            alert("Sessão expirada. Faça login novamente.");
            window.location.href = "index.html";
        }
    } catch (error) {
        mensagemDiv.textContent = "Erro de conexão com o servidor.";
        mensagemDiv.className = "erro";
    }
}

// 2. EXCLUIR USUÁRIO (DELETE)
async function excluirUsuario(id, nome) {
    if (!confirm(`Tem certeza que deseja excluir o usuário "${nome}" (ID: ${id})?`)) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/usuarios/${id}`, {
            method: "DELETE",
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok || response.status === 204) {
            mensagemDiv.textContent = `Usuário "${nome}" excluído com sucesso!`;
            mensagemDiv.className = "sucesso";
            carregarUsuarios(); // Recarrega a lista automaticamente
        } else {
            mensagemDiv.textContent = "Erro ao tentar excluir o usuário.";
            mensagemDiv.className = "erro";
        }
    } catch (error) {
        mensagemDiv.textContent = "Erro de conexão ao excluir.";
        mensagemDiv.className = "erro";
    }
}

// 3. PREPARAR E SALVAR EDIÇÃO (PUT)
function abrirFormularioEdicao(user) {
    formEditar.style.display = "block";
    document.getElementById("editId").value = user.id;
    document.getElementById("editNome").value = user.nome;
    document.getElementById("editEmail").value = user.email;
    document.getElementById("editTelefone").value = user.telefone || "";
    document.getElementById("editSenha").value = "";
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

btnCancelar.addEventListener("click", () => {
    formEditar.style.display = "none";
});

formEditar.addEventListener("submit", async (event) => {
    event.preventDefault();

    const usuarioAtualizado = {
        id: parseInt(document.getElementById("editId").value),
        nome: document.getElementById("editNome").value,
        email: document.getElementById("editEmail").value,
        telefone: document.getElementById("editTelefone").value,
        senha: document.getElementById("editSenha").value
    };

    try {
        const response = await fetch("http://localhost:8080/usuarios", {
            method: "PUT",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(usuarioAtualizado)
        });

        if (response.ok) {
            formEditar.style.display = "none";
            mensagemDiv.textContent = "Usuário atualizado com sucesso!";
            mensagemDiv.className = "sucesso";
            carregarUsuarios();
        } else {
            mensagemDiv.textContent = "Erro ao atualizar. Verifique se preencheu todos os campos corretamente.";
            mensagemDiv.className = "erro";
        }
    } catch (error) {
        mensagemDiv.textContent = "Erro de conexão ao atualizar.";
        mensagemDiv.className = "erro";
    }
});

// 4. LOGOUT E INICIALIZAÇÃO
btnSair.addEventListener('click', () => {
    localStorage.removeItem("meuTokenJWT");
    window.location.href = "index.html";
});

btnCarregar.addEventListener('click', carregarUsuarios);

// Já carrega a lista automaticamente ao abrir o painel!
carregarUsuarios();