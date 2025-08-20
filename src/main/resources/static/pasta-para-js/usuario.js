const API_USUARIO = "https://padarao.onrender.com/usuario"; // Troque para a URL do Render depois

// CADASTRAR USUÁRIO
document.getElementById("formUsuario").addEventListener("submit", async function (e) {
    e.preventDefault();
    const usuario = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        dataNascimento: document.getElementById("dataNascimento").value,
        email: document.getElementById("email").value,
        telefone: document.getElementById("telefone").value
    };

    try {
        const response = await fetch(API_USUARIO, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuario)
        });

        if (response.status === 201 || response.status === 200) {
            alert("Usuário cadastrado com sucesso!");
            document.getElementById("formUsuario").reset();
            carregarUsuarios();
        } else {
            alert("Erro ao cadastrar usuário.");
        }
    } catch (error) {
        alert("Erro de conexão com o servidor.");
        console.error(error);
    }
});

// LISTAR TODOS OS USUÁRIOS
async function carregarUsuarios() {
    try {
        const res = await fetch(API_USUARIO);
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        const usuarios = await res.json();
        const div = document.getElementById("listaUsuarios");
        div.innerHTML = usuarios.map(u =>
            `<p>${u.nome} - ${u.cpf} 
                <button onclick="deletarUsuario('${u.cpf}')">Excluir</button>
            </p>`
        ).join("");
    } catch (err) {
        console.error(err);
        alert("Falha ao carregar usuários");
    }
}

// DELETAR USUÁRIO
async function deletarUsuario(cpf) {
    try {
        const res = await fetch(`${API_USUARIO}?cpf=${cpf}`, { method: "DELETE" });
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        carregarUsuarios();
    } catch (err) {
        console.error(err);
        alert("Falha ao deletar usuário");
    }
}

// BUSCAR USUÁRIO POR CPF
async function buscarUsuario() {
    const cpf = document.getElementById("buscarCpf").value;
    try {
        const response = await fetch(`${API_USUARIO}?cpf=${cpf}`);
        if (response.ok) {
            const usuario = await response.json();
            document.getElementById("resultado").textContent = JSON.stringify(usuario, null, 2);
        } else {
            document.getElementById("resultado").textContent = "Usuário não encontrado.";
        }
    } catch (error) {
        document.getElementById("resultado").textContent = "Erro ao buscar usuário.";
        console.error(error);
    }
}

// Carrega a lista automaticamente ao abrir a página
carregarUsuarios();
