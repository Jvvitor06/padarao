const API_USUARIO = "https://padarao.onrender.com/usuario";
const API_PRODUTO = "https://padarao.onrender.com/padaria";
const API_COMPRA  = "https://padarao.onrender.com/compra";

let usuariosCache = [];
let produtosCache = [];

// Helpers
const money = (v) =>
    Number(v || 0).toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

// CARREGAR USUÁRIOS NO SELECT (guarda id e cpf)
async function carregarUsuarios() {
    try {
        const res = await fetch(API_USUARIO);
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        usuariosCache = await res.json();

        const sel = document.getElementById("usuario");
        sel.innerHTML = '<option value="">Selecione um usuário</option>';

        usuariosCache.forEach(u => {
            // value vai com id (se existir) ou cpf; mas guardamos SEMPRE o cpf no data-cpf
            const value = (u.id ?? u.cpf);
            sel.innerHTML += `<option value="${value}" data-cpf="${u.cpf || ""}">${u.nome}</option>`;
        });
    } catch (err) {
        console.error(err);
        alert("Falha ao carregar usuários");
    }
}

// CARREGAR PRODUTOS NO SELECT
async function carregarProdutos() {
    try {
        const res = await fetch(API_PRODUTO);
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        produtosCache = await res.json();

        const sel = document.getElementById("produto");
        sel.innerHTML = "";
        produtosCache.forEach(p => {
            const preco = typeof p.preco === "number" ? p.preco : parseFloat(p.preco);
            sel.innerHTML += `<option value="${p.id}">${p.nome} - R$ ${money(preco)}</option>`;
        });
    } catch (err) {
        console.error(err);
        alert("Falha ao carregar produtos");
    }
}

// BUSCAR USUÁRIO (tenta por CPF; se não der, tenta por ID)
async function buscarUsuarioAposCompra(usuarioValue, cpfDataAttr) {
    if (cpfDataAttr) {
        const byCpf = await fetch(`${API_USUARIO}?cpf=${encodeURIComponent(cpfDataAttr)}`);
        if (byCpf.ok) {
            const data = await byCpf.json();
            return Array.isArray(data) ? data[0] : data; // pega o primeiro se for array
        }
    }
    const byId = await fetch(`${API_USUARIO}/${usuarioValue}`);
    if (byId.ok) return byId.json();

    throw new Error("Não foi possível obter os dados do usuário.");
}


// REGISTRAR COMPRA + GERAR NOTINHA
document.getElementById("formCompra").addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuarioSelect = document.getElementById("usuario");
    const usuarioValue  = usuarioSelect.value; // pode ser id ou cpf (dependendo do que veio do backend)
    const usuarioCpf    = usuarioSelect.selectedOptions[0]?.dataset?.cpf || "";

    const produtosIds = Array.from(document.getElementById("produto").selectedOptions)
        .map(o => o.value);

    if (!usuarioValue || produtosIds.length === 0) {
        alert("Selecione usuário e pelo menos um produto.");
        return;
    }

    const compra = { usuarioId: usuarioValue, produtosIds }; // mantém seu formato atual

    try {
        const res = await fetch(API_COMPRA, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(compra)
        });

        if (!res.ok) {
            const txt = await res.text().catch(() => "");
            console.error("Falha no POST /compra:", res.status, txt);
            alert("Erro ao registrar compra.");
            return;
        }

        // Buscar dados do usuário (preferindo CPF)
        const usuario = await buscarUsuarioAposCompra(usuarioValue, usuarioCpf);

        // Filtrar produtos escolhidos
        const produtosEscolhidos = produtosCache.filter(p =>
            produtosIds.includes(String(p.id))
        );

        // Calcular total (garantindo número)
        const total = produtosEscolhidos.reduce((sum, p) => {
            const preco = typeof p.preco === "number" ? p.preco : parseFloat(p.preco);
            return sum + (isNaN(preco) ? 0 : preco);
        }, 0);

        // Preencher notinha
        document.getElementById("nomeCliente").textContent = usuario.nome ||"(Sem nome)";
        document.getElementById("cpfCliente").textContent  = usuario?.cpf  || usuarioCpf || "(Sem CPF)";

        document.getElementById("listaProdutos").innerHTML = produtosEscolhidos
            .map(p => {
                const preco = typeof p.preco === "number" ? p.preco : parseFloat(p.preco);
                return `<li>${p.nome} - R$ ${money(preco)}</li>`;
            })
            .join("");

        document.getElementById("totalCompra").textContent = money(total);

        // Mostrar notinha
        document.getElementById("notaCompra").style.display = "block";

        // Resetar form
        document.getElementById("formCompra").reset();

    } catch (err) {
        console.error(err);
        alert("Erro de conexão com o servidor.");
    }
});

// Inicialização
carregarUsuarios();
carregarProdutos();
