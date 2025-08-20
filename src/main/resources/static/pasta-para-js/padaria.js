const API_PRODUTO = "https://padarao.onrender.com/padaria"; // Troque para a URL do Render depois

// CADASTRAR PRODUTO
document.getElementById("formProduto").addEventListener("submit", async function (e) {
    e.preventDefault();
    const produto = {
        nome: document.getElementById("nome").value,
        preco: parseFloat(document.getElementById("preco").value)
    };

    try {
        const res = await fetch(API_PRODUTO, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(produto)
        });

        if (res.ok) {
            alert("Produto cadastrado com sucesso!");
            document.getElementById("formProduto").reset();
            carregarProdutos();
        } else {
            alert("Erro ao cadastrar produto.");
        }
    } catch (err) {
        alert("Erro de conexão com o servidor.");
        console.error(err);
    }
});

// LISTAR TODOS OS PRODUTOS
async function carregarProdutos() {
    try {
        const res = await fetch(API_PRODUTO);
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        const produtos = await res.json();
        const div = document.getElementById("listaProdutos");
        div.innerHTML = produtos.map(p =>
            `<p>${p.nome} - R$${p.preco.toFixed(2)} 
                <button onclick="deletarProduto(${p.id})">Excluir</button>
            </p>`
        ).join("");
    } catch (err) {
        console.error(err);
        alert("Falha ao carregar produtos");
    }
}

// DELETAR PRODUTO
async function deletarProduto(id) {
    try {
        const res = await fetch(`${API_PRODUTO}/${id}`, { method: "DELETE" });
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        carregarProdutos();
    } catch (err) {
        console.error(err);
        alert("Falha ao deletar produto");
    }
}

// Carrega produtos ao abrir a página
carregarProdutos();
