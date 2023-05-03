package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.dto.converter.BucketConverter;
import com.amdaris.mentoring.core.model.Bucket;
import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.repository.BucketRepository;
import com.amdaris.mentoring.core.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
public class BucketControllerTestsIT {
    @Autowired
    private BucketRepository bucketRepository;

    @Autowired
    private ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that endpoint return all buckets")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        bucketRepository.deleteAll();

        Bucket firstBucket = new Bucket();
        firstBucket.setId(1L);
        firstBucket.setProducts(List.of());

        Bucket secondBucket = new Bucket();
        secondBucket.setId(2L);
        secondBucket.setProducts(List.of());

        List<Bucket> buckets = List.of(firstBucket, secondBucket);

        bucketRepository.saveAll(buckets);

        List<BucketDto> response = buckets.stream()
                .map(bucket -> BucketConverter.toBucketDto.apply(bucket))
                .collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.get("/bucket")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(response),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return bucket by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        bucketRepository.deleteAll();

        Bucket firstBucket = new Bucket();
        firstBucket.setId(1L);
        firstBucket.setProducts(List.of());

        Bucket bucket = bucketRepository.save(firstBucket);

        mvc.perform(MockMvcRequestBuilders.get("/bucket/" + bucket.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(BucketConverter.toBucketDto.apply(bucket)),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists bucket by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        bucketRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/bucket/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Bucket with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that bucket was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        bucketRepository.deleteAll();

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Product");
        product.setDescription("Description");
        product.setPrice(100);
        product.setCategories(Set.of());

        Product savedProduct = productRepository.save(product);

        Bucket firstBucket = new Bucket();
        firstBucket.setId(1L);
        firstBucket.setProducts(null);

        Bucket bucket = bucketRepository.save(firstBucket);

        bucket.setProducts(List.of(savedProduct));
        BucketDto bucketDto = BucketConverter.toBucketDto.apply(bucket);

        mvc.perform(MockMvcRequestBuilders.patch("/bucket/" + bucket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bucketDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(bucketDto),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update bucket which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        bucketRepository.deleteAll();

        Bucket firstBucket = new Bucket();
        firstBucket.setId(1L);
        firstBucket.setProducts(List.of());

        mvc.perform(MockMvcRequestBuilders.patch("/bucket/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstBucket)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Bucket with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted bucket by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        bucketRepository.deleteAll();

        Bucket firstBucket = new Bucket();
        firstBucket.setId(1L);
        firstBucket.setProducts(List.of());

        Bucket bucket = bucketRepository.save(firstBucket);

        mvc.perform(MockMvcRequestBuilders.delete("/bucket/" + bucket.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Bucket with id - " + bucket.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete bucket which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        bucketRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/bucket/" + (short) 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Bucket with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }
}
