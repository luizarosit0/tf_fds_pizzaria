package com.luiza.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados.EstoqueRepositoryJPA;
import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados.IngredienteBD;
import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados.ItemEstoqueBD;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

// Testar se o Estoque Repository consegue fazer a conexão, o mapeamento 
// e as consultas SQL/JPA corretamente com o banco de dados real
@DataJpaTest
@Import(EstoqueRepositoryJPA.class) 
public class EstoqueRepositoryIntegrationTests {

    private TestEntityManager entityManager; // para substuir o SQL puro -> cria obj e manda persistir
    private EstoqueRepositoryJPA estoqueRepository;
    private JdbcTemplate jdbcTemplate; // para usar o BeforeEach 

    @Autowired
    public EstoqueRepositoryIntegrationTests(TestEntityManager entityManager, 
                                             EstoqueRepositoryJPA estoqueRepository, 
                                             JdbcTemplate jdbcTemplate ){
        this.entityManager = entityManager;
        this.estoqueRepository = estoqueRepository;
        this.jdbcTemplate = jdbcTemplate; 
    }

    @BeforeEach
    void setup() {
        // Limpeza dos filhos antes dos pais para evitar violação de Foreign Key
        
        // 1. Filhos de Ingredientes e Produtos
        jdbcTemplate.update("DELETE FROM itens_pedido");
        jdbcTemplate.update("DELETE FROM cardapio_produto");
        jdbcTemplate.update("DELETE FROM produto_receita");
        jdbcTemplate.update("DELETE FROM receita_ingrediente");
        
        // 2. Filhos de Ingredientes
        jdbcTemplate.update("DELETE FROM itens_estoque"); 
        
        // 3. Pais (Ingredientes, Receitas, Produtos, Pedidos, etc.)
        jdbcTemplate.update("DELETE FROM pedidos"); 
        jdbcTemplate.update("DELETE FROM produtos");
        jdbcTemplate.update("DELETE FROM clientes");
        jdbcTemplate.update("DELETE FROM receitas");
        jdbcTemplate.update("DELETE FROM ingredientes"); 
        jdbcTemplate.update("DELETE FROM cardapios");
        jdbcTemplate.update("DELETE FROM usuarios");
    }

    /*
     * Caso de Teste 1: Buscar ItemEstoque por Ingrediente
     * Objetivo: Validar o mapeamento JPA e a query findByIngrediente ao buscar um ItemEstoque 
     *           real do banco utilizando um objeto Ingrediente de Domínio.
     * Entradas: Objeto Ingrediente (ID 1, "Queijo Mussarela").
     * Estado inicial do banco: IngredienteBD e ItemEstoqueBD (Quantidade 50) persistidos via TestEntityManager.
     * Resultados esperados: Deve retornar um objeto ItemEstoque não nulo com quantidade igual 
     *                       a 50 e descrição do ingrediente correta.
     */ 
    @Test
    @DisplayName("Deve encontrar item de estoque pelo ingrediente")
    void encontraEstoquePorIngrediente() { 
        // cria as entidades de Banco de Dados
        IngredienteBD ingredienteBD = new IngredienteBD(1L, "Queijo Mussarela");
        entityManager.persist(ingredienteBD); // salva na tabela ingredientes

        ItemEstoqueBD itemBD = new ItemEstoqueBD(1L, ingredienteBD, 50);
        entityManager.persist(itemBD); // salva na tabela itensEstoque
        
        entityManager.flush(); // força a gravação no banco agora

        Ingrediente ingredienteBusca = new Ingrediente(1L, "Queijo Mussarela");

        ItemEstoque itemEncontrado = estoqueRepository.findByIngrediente(ingredienteBusca);

        assertNotNull(itemEncontrado, "Deveria ter encontrado o item");
        assertEquals(50, itemEncontrado.getQuantidade());
        assertEquals("Queijo Mussarela", itemEncontrado.getIngrediente().getDescricao());
    }

    /*
     * Caso de Teste 2: Persistir novo ItemEstoque
     * Objetivo: Validar a função save() do repositório, garantindo que ele converte o objeto de 
     *           Domínio para a Entidade de Banco e o salva via JPA.
     * Entradas: Objeto ItemEstoque (Ingrediente ID 2, Quantidade 100).
     * Estado inicial do banco: IngredienteBD ('Tomate') persistido.
     * Resultados Esperados: O item deve ser encontrado no banco via entityManager.find() 
     *                       com quantidade igual a 100.
     */
    @Test
    @DisplayName("Deve salvar/atualizar um item de estoque corretamente")
    void salvarItemEstoque() {
        // cria ingrediente no banco
        IngredienteBD ingredienteBD = new IngredienteBD(2L, "Tomate");
        entityManager.persist(ingredienteBD);
        
        // obj de dominio
        Ingrediente ingrediente = new Ingrediente(2L, "Tomate");
        ItemEstoque novoItem = new ItemEstoque(ingrediente, 100);

        estoqueRepository.save(novoItem); // rep deve converter (dominio -> BD) e salvar

        // buscar direto no banco para ver se salvou
        ItemEstoqueBD itemSalvo = entityManager.find(ItemEstoqueBD.class, 2L);
        
        assertNotNull(itemSalvo);
        assertEquals(100, itemSalvo.getQuantidade());
    }
}
