package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {

        Product product = new Product(null,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.png",
                Instant.parse("2020-07-20T03:00:00Z")); // data válida



        return product;
    }

    public static Product createProductWithoutCategories() {
        return createProduct(); // helper para deixar claro
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }

}
