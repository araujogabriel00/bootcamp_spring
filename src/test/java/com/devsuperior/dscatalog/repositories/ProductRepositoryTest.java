package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Long nonExistingId;
    private Long existingId;
    private Long countTotalProjects;

    @BeforeEach
    void setUp() {
        nonExistingId = 1000L;
        existingId = 1L;
        countTotalProjects = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);


        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProjects +1, product.getId());

    }

    @Test
    public void getShouldGetProductIfIdIsNotNull(){

        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void getShouldGetProductIfIdIsNull(){

        Optional<Product> result = productRepository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {


        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAcessExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });

    }


}