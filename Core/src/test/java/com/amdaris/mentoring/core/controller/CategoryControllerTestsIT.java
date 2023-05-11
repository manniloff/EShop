package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.dto.converter.CategoryConverter;
import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class CategoryControllerTestsIT {
    @Autowired
    private CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that endpoint return all categories")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        categoryRepository.deleteAll();

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        Category clothingCategory = new Category();
        clothingCategory.setTitle("Clothing");

        List<Category> categories = List.of(vehicleCategory, clothingCategory);

        categoryRepository.saveAll(categories);

        mvc.perform(MockMvcRequestBuilders.get("/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(
                                categories.stream()
                                        .map(category -> CategoryConverter.toCategoryDto.apply(category))
                                        .collect(Collectors.toList())),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return category by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        categoryRepository.deleteAll();

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        Category category = categoryRepository.save(vehicleCategory);

        mvc.perform(MockMvcRequestBuilders.get("/category/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(
                                CategoryConverter.toCategoryDto.apply(vehicleCategory)),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists category by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        categoryRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Category with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new category")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        categoryRepository.deleteAll();

        Category animalCategory = new Category();
        animalCategory.setTitle("Animal");
        CategoryDto categoryDto = CategoryConverter.toCategoryDto.apply(animalCategory);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        CategoryDto byTitle = CategoryConverter.toCategoryDto.apply(
                categoryRepository.findByTitle(animalCategory.getTitle()).get()
        );

        Assertions.assertEquals(objectMapper.writeValueAsString(byTitle),
                mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Test that exception throws when try to create category which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        categoryRepository.deleteAll();

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryRepository.save(vehicleCategory);

        mvc.perform(MockMvcRequestBuilders.post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleCategory)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Category with title - Vehicle, exists!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that category was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        categoryRepository.deleteAll();

        Category homeCategory = new Category();
        homeCategory.setTitle("Home Staff");

        Category category = categoryRepository.save(homeCategory);

        homeCategory.setTitle("Home updated");
        CategoryDto categoryDto = CategoryConverter.toCategoryDto.apply(category);

        mvc.perform(MockMvcRequestBuilders.patch("/category/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(homeCategory)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(categoryDto), result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update category which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        categoryRepository.deleteAll();

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        mvc.perform(MockMvcRequestBuilders.patch("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleCategory)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Category with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted category by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        categoryRepository.deleteAll();

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        Category category = categoryRepository.save(vehicleCategory);

        mvc.perform(MockMvcRequestBuilders.delete("/category/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Category with id - " + category.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete category which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        categoryRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/category/" + (short) 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Category with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }
}
