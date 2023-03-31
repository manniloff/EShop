package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.converter.ProductConverter;
import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class ProductControllerTestsIT {
    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that endpoint return all products")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        Product clothingProduct = new Product();
        clothingProduct.setTitle("Clothing");
        clothingProduct.setDescription("Clothing product");
        clothingProduct.setPrice(150.0);
        clothingProduct.setSale((short) 0);
        clothingProduct.setCategories(Set.of());

        productRepository.saveAll(List.of(vehicleProduct, clothingProduct));

        List<ProductDto> products = productRepository.findAllByTitleContaining("l")
                .stream()
                .map(product -> ProductConverter.toProductDto.apply(product))
                .collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(products),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return product by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        ProductDto product = ProductConverter.toProductDto.apply(productRepository.save(vehicleProduct));

        mvc.perform(MockMvcRequestBuilders.get("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(product),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists product by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        productRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Product with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new product")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        ProductDto byTitle = ProductConverter.toProductDto.apply(
                productRepository.findByTitle(vehicleProduct.getTitle()).get());

        Assertions.assertEquals(objectMapper.writeValueAsString(byTitle), mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Test that exception throws when try to create product which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        productRepository.save(vehicleProduct);

        mvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleProduct)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Product with title - Vehicle, exists!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that product was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        ProductDto product = ProductConverter.toProductDto.apply(productRepository.save(vehicleProduct));

        product.setTitle("Vehicle updated");
        mvc.perform(MockMvcRequestBuilders.patch("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(product),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update product which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        mvc.perform(MockMvcRequestBuilders.patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleProduct)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Product with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted product by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        productRepository.deleteAll();

        Product vehicleProduct = new Product();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategories(Set.of());

        Product product = productRepository.save(vehicleProduct);

        mvc.perform(MockMvcRequestBuilders.delete("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Product with id - " + product.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete product which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        productRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/product/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Product with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }
}
