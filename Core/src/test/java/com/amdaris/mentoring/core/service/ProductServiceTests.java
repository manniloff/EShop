package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.converter.CategoryConverter;
import com.amdaris.mentoring.core.dto.criteria.ProductSearchCriteria;
import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.util.PageView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void beforeEach() {
        productService.clear();
        categoryService.clear();
    }

    @DisplayName("Test that all products were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Assertions.assertEquals(0, productService.findAll(pageable).size());

        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);

        productService.save(vehicleProduct);

        ProductDto clothingProduct = new ProductDto();
        clothingProduct.setTitle("Clothing");
        clothingProduct.setDescription("Clothing product");
        clothingProduct.setPrice(150.0);
        clothingProduct.setSale((short) 0);

        productService.save(clothingProduct);

        Assertions.assertEquals(2, productService.findAll(pageable).size());
    }

    @DisplayName("Test that found by title product from database")
    @Test
    public void findByTitle_dataIsPresent_returnExistingData() {
        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        productService.save(vehicleProduct);

        ProductDto foundByTitle = productService.findByTitle(vehicleProduct.getTitle());

        Assertions.assertEquals(vehicleProduct.getSale(), foundByTitle.getSale());
        Assertions.assertEquals(vehicleProduct.getPrice(), foundByTitle.getPrice());
        Assertions.assertEquals(vehicleProduct.getDescription(), foundByTitle.getDescription());
        Assertions.assertEquals(vehicleProduct.getTitle(), foundByTitle.getTitle());
    }

    @DisplayName("Test that found by title product throws error when didn't found any item")
    @Test
    public void findByTitle_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.findByTitle("Vehicle product"));

        Assertions.assertEquals("Product with title - Vehicle product doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found list of products by symbol in title from database")
    @Test
    public void findAllByTitle_dataIsPresent_returnExistingData() {
        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);

        vehicleProduct = productService.save(vehicleProduct);

        ProductDto gardenProduct = new ProductDto();
        gardenProduct.setTitle("Garden");
        gardenProduct.setDescription("Garden product");
        gardenProduct.setPrice(15000.0);
        gardenProduct.setSale((short) 0);

        gardenProduct = productService.save(gardenProduct);

        ProductDto homeProduct = new ProductDto();
        homeProduct.setTitle("Home");
        homeProduct.setDescription("Home product");
        homeProduct.setPrice(15000.0);
        homeProduct.setSale((short) 0);

        homeProduct = productService.save(homeProduct);

        List<ProductDto> findAllByE = productService.findAllByTitle("e");

        Assertions.assertEquals(List.of(vehicleProduct, gardenProduct, homeProduct), findAllByE);

        List<ProductDto> findAllByH = productService.findAllByTitle("h");

        Assertions.assertEquals(List.of(vehicleProduct, homeProduct), findAllByH);

        List<ProductDto> findAllByV = productService.findAllByTitle("v");

        Assertions.assertEquals(List.of(vehicleProduct), findAllByV);
    }

    @DisplayName("Test that found list of products by symbol in title throws error when didn't found any item")
    @Test
    public void findAllByTitle_dataNoPresent_returnErrorMessage() {
        Assertions.assertEquals(List.of(), productService.findAllByTitle("V"));
    }

    @DisplayName("Test that found by id category from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        productService.save(vehicleProduct);

        long productId = productService.findProductId(vehicleProduct.getTitle());
        ProductDto product = productService.findById(productId);

        Assertions.assertEquals(vehicleProduct.getTitle(), product.getTitle());
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

        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        productService.save(vehicleProduct);

        List<ProductDto> products = productService.findAll(pageable);

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("Vehicle", products.get(0).getTitle());
    }

    @DisplayName("Test that save product throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        productService.save(vehicleProduct);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> productService.save(vehicleProduct));

        Assertions.assertEquals("Product with title - Vehicle, exists!", exception.getMessage());
    }

    @DisplayName("Test that product was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        ProductDto productDto = productService.save(vehicleProduct);

        vehicleProduct.setTitle("Vehicle Product Updated");
        ProductDto updatedCategory = productService.update(vehicleProduct, productDto.getId());

        Assertions.assertEquals("Vehicle Product Updated", updatedCategory.getTitle());
    }

    @DisplayName("Test that update product throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");
        vehicleCategory.setProducts(Set.of());

        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        productService.save(vehicleProduct);

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.update(vehicleProduct, 1L));

        Assertions.assertEquals("Product with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that product was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of((short) 1));

        productService.save(vehicleProduct);

        List<ProductDto> products = productService.findAll(pageable);
        Assertions.assertEquals(1, products.size());

        long productId = productService.findProductId(vehicleProduct.getTitle());
        productService.deleteById(productId);

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
