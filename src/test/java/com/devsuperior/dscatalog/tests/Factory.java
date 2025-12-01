package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        // id null — deixa o banco gerar
        // Instant válido (ano-mês-dia)
        Product product = new Product(null,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.png",
                Instant.parse("2020-07-20T03:00:00Z")); // data válida

        // NÃO adicionar categorias com id fixo aqui.
        // Se precisar testar categorias, injete-as no teste recuperando do repo:
        // Category cat = categoryRepository.findById(2L).orElseThrow(...);
        // product.getCategories().add(cat);

        return product;
    }

    public static Product createProductWithoutCategories() {
        return createProduct(); // helper para deixar claro
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        // se ProductDTO tem construtor (product, categories)
        return new ProductDTO(product, product.getCategories());
    }
}
