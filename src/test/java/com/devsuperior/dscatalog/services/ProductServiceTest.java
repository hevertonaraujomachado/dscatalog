package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;


    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));






    }
    @Test
    public void  findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist () {
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

    }
        @Test
        public void updateShouldReturnProductDTOWhenIdExists() {
            Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
            Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

            ProductDTO dto = Factory.createProductDTO();

            ProductDTO result = service.update(existingId, dto);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(product.getId(), result.getId());
        }

        @Test
        public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
            Mockito.when(repository.getReferenceById(nonExistingId))
                    .thenThrow(jakarta.persistence.EntityNotFoundException.class);

            ProductDTO dto = Factory.createProductDTO();

            Assertions.assertThrows(ResourceNotFoundException.class, () -> {
                service.update(nonExistingId, dto);
            });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(existingId);


        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }
    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(repository.findAll(pageable)).thenReturn(page);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);

    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

    }


}



