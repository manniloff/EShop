package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void beforeEach() {
        productService.clear();
    }

    @DisplayName("Test that all products were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Assertions.assertEquals(0, productService.findAll(pageable).size());

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        Category clothingCategory = new Category();
        clothingCategory.setTitle("Clothing");

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        Product clothingProduct = new Product();
        clothingProduct.setTitle("Clothing");
        clothingProduct.setDescription("Clothing product");
        clothingProduct.setPrice(150.0);
        clothingProduct.setSale((short) 0);
        clothingProduct.setCategories(Set.of());

        productService.save(clothingProduct);

        Assertions.assertEquals(2, productService.findAll(pageable).size());
    }

    @DisplayName("Test that found by title product from database")
    @Test
    public void findByTitle_dataIsPresent_returnExistingData() {
        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        Optional<Product> foundByTitle = productService.findByTitle(vehicleProduct.getTitle());

        Assertions.assertEquals(Optional.of(vehicleProduct), foundByTitle);
    }

    @DisplayName("Test that found by title product throws error when didn't found any item")
    @Test
    public void findByTitle_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.findByTitle("Vehicle product"));

        Assertions.assertEquals("Product with title - Vehicle product doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by id category from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        Optional<Product> foundByTitle = productService.findByTitle("Vehicle");
        Optional<Product> product = productService.findById((foundByTitle.get().getId()));

        Assertions.assertEquals(vehicleProduct.getTitle(), product.get().getTitle());
    }

    @DisplayName("Test that found by id product throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.findById(1L));

        Assertions.assertEquals("Product with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that product was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        List<Product> products = productService.findAll(pageable);

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("Vehicle", products.get(0).getTitle());
    }

    @DisplayName("Test that save product throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> productService.save(vehicleProduct));

        Assertions.assertEquals("Product with title - Vehicle, exists!", exception.getMessage());
    }

    @DisplayName("Test that product was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        long productId = productService.findByTitle(vehicleProduct.getTitle()).get().getId();

        vehicleProduct.setTitle("Vehicle Product Updated");
        productService.update(vehicleProduct, productId);

        Optional<Product> updatedCategory = productService.findById(productId);

        Assertions.assertEquals("Vehicle Product Updated", updatedCategory.get().getTitle());
    }

    @DisplayName("Test that update product throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");
        vehicleCategory.setProducts(Set.of());

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of(vehicleCategory));

        //productService.save(vehicleProduct);

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.update(vehicleProduct, 1L));

        Assertions.assertEquals("Product with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that product was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productService.save(vehicleProduct);

        List<Product> products = productService.findAll(pageable);
        Assertions.assertEquals(1, products.size());

        productService.deleteById(products.get(0).getId());

        Assertions.assertEquals(0, productService.findAll(pageable).size());
    }

    @DisplayName("Test that delete product throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.deleteById(1L));

        Assertions.assertEquals("Product with id - 1 doesn't exist!", exception.getMessage());
    }
}
