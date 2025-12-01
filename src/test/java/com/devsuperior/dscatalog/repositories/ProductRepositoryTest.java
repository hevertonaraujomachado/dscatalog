package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long baselineCount;// contagem inicial antes de criarmos o produto de teste
    private  long countTotalProducts;

    @BeforeEach
    void setUp() {
        // captura contagem atual (pode haver registros inseridos por data.sql)
        baselineCount = repository.count();

        // salva um produto para ter um existingId garantido
        Product p = new Product(null, "Produto Teste", "Descricao", 10.0, "http://img", Instant.now());
        p = repository.save(p);
        existingId = p.getId();

        // nonExistingId: um id extremamente improvável de existir (pode ser Long.MAX_VALUE ou um offset)
        nonExistingId = Long.MAX_VALUE - 1000;
        countTotalProducts = 25L;
    }
    @Test
    void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist(){
        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty(),"Quando o Id existe, findById deve retornar não vazio");
    }
    @Test
    void findByIdShouldReturnEmptOptionalWhenIdDoesNotExist() {
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent(), "Quando o ID existe, findbyId deve retornar Iptional não vazio");
    }
    @Test
    void saveShouldPersistOneInsert_simpleProduct() {
        long before = repository.count();

        Product product = new Product(
                null,
                "Diag Product X " + System.nanoTime(),
                "desc",
                1.0,
                "http://img",
                Instant.parse("2020-07-20T03:00:00Z")
        );

        Product saved = repository.save(product);
        repository.flush();

        List<Product> all = repository.findAll()
                .stream()
                .filter(p -> p.getName().startsWith("Diag Product X"))
                .toList();

        // Deve existir somente 1 produto com esse nome específico
        assertEquals(1, all.size(), "Deve existir apenas um registro para o produto inserido");
        assertNotNull(saved.getId());
        assertEquals(before + 1, repository.count());


    }

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {
        // sanity: after save, count increased by 1
        assertTrue(repository.existsById(existingId));
        assertEquals(baselineCount + 1, repository.count());

        // action
        repository.deleteById(existingId);

        // verification: id removido e contagem volta ao baseline
        assertFalse(repository.existsById(existingId));
        assertEquals(baselineCount, repository.count());
    }

    @Test
    void deleteShouldNotChangeCountWhenIdDoesNotExist() {
        // sanity: nonExistingId realmente não existe
        assertFalse(repository.existsById(nonExistingId));

        long before = repository.count();

        // action: chamamos deleteById — não esperamos exceção aqui porque a implementação
        // pode ou não lançar; testamos o efeito observável
        repository.deleteById(nonExistingId);

        // verification: contagem permanece a mesma
        long after = repository.count();
        assertEquals(before, after, "Ao tentar deletar id inexistente, a contagem não deve mudar");
    }
}
