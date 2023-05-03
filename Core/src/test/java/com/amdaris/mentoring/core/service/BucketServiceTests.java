package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
public class BucketServiceTests {
    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void beforeEach() {
        bucketService.clear();
        userService.clear();
        addressService.clear();
        productService.clear();
        categoryService.clear();
    }

    @DisplayName("Test that all buckets were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, bucketService.findAll().size());

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        AddressDto secondAddress = new AddressDto();
        secondAddress.setCountry("Moldova");
        secondAddress.setCity("Chisinau");
        secondAddress.setStreet("Puskin");
        secondAddress.setHouse("24");

        AddressDto savedSecondAddress = addressService.save(secondAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456784");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        userService.save(firstUser);

        UserDto secondUser = new UserDto();
        secondUser.setEmail("test99@user.md");
        secondUser.setRole("ADMIN");
        secondUser.setBirthday(LocalDate.of(1990, 10, 5));
        secondUser.setFirstName("Test2");
        secondUser.setLastName("User2");
        secondUser.setPhoneNumber("068456783");
        secondUser.setAddressIds(Set.of(savedSecondAddress.getId()));
        secondUser.setPassword("secretpass2");

        userService.save(secondUser);

        Assertions.assertEquals(2, bucketService.findAll().size());
    }

    @DisplayName("Test that found by id bucket from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, bucketService.findAll().size());

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test5@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456122");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        BucketDto bucketDto = bucketService.findById(saveFirstUser.getId());

        Assertions.assertEquals(saveFirstUser.getId(), bucketDto.getId());
        Assertions.assertEquals(Map.of(), bucketDto.getProductIds());
    }

    @DisplayName("Test that found by id bucket throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> bucketService.findById(1L));

        Assertions.assertEquals("Bucket with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that bucket was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test3@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456782");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        BucketDto bucket = bucketService.findById(saveFirstUser.getId());

        Assertions.assertEquals(BucketDto.builder()
                .id(saveFirstUser.getId())
                .productIds(Map.of())
                .build(), bucket);
    }

    @DisplayName("Test that save bucket throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test4@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456721");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> bucketService.save(saveFirstUser.getId()));

        Assertions.assertEquals("Bucket with id - " + saveFirstUser.getId() + " already exists!", exception.getMessage());
    }

    @DisplayName("Test that bucket was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        CategoryDto homeCategoryDto = new CategoryDto();
        homeCategoryDto.setTitle("For Home");

        CategoryDto savedHomeCategory = categoryService.save(homeCategoryDto);

        ProductDto clothingProduct = new ProductDto();
        clothingProduct.setTitle("Clothing");
        clothingProduct.setDescription("Clothing product");
        clothingProduct.setPrice(150.0);
        clothingProduct.setSale((short) 0);
        clothingProduct.setCategoryIds(List.of(savedHomeCategory.getId()));

        ProductDto savedProduct = productService.save(clothingProduct);

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test4@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456721");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        BucketDto bucket = bucketService.findById(saveFirstUser.getId());

        Assertions.assertEquals(Map.of(), bucket.getProductIds());

        bucket.setProductIds(Map.of(savedProduct.getId(), 1));

        BucketDto updatedBucket = bucketService.update(bucket, bucket.getId());

        Assertions.assertEquals(bucket.getProductIds(), updatedBucket.getProductIds());
    }

    @DisplayName("Test that update bucket throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        BucketDto bucketDto = BucketDto.builder()
                .id(1L)
                .productIds(Map.of())
                .build();

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> bucketService.update(bucketDto, bucketDto.getId()));

        Assertions.assertEquals("Bucket with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that bucket was reset in database")
    @Test
    public void reset_dataIsPresent_returnNewOrder() {
        CategoryDto homeCategoryDto = new CategoryDto();
        homeCategoryDto.setTitle("For Home");

        CategoryDto savedHomeCategory = categoryService.save(homeCategoryDto);

        ProductDto clothingProduct = new ProductDto();
        clothingProduct.setTitle("Clothing");
        clothingProduct.setDescription("Clothing product");
        clothingProduct.setPrice(150.0);
        clothingProduct.setSale((short) 0);
        clothingProduct.setCategoryIds(List.of(savedHomeCategory.getId()));

        ProductDto savedProduct = productService.save(clothingProduct);

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test10@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456741");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        BucketDto bucket = bucketService.findById(saveFirstUser.getId());

        Assertions.assertEquals(Map.of(), bucket.getProductIds());

        bucket.setProductIds(Map.of(savedProduct.getId(), 1));

        BucketDto updatedBucket = bucketService.update(bucket, bucket.getId());

        Assertions.assertEquals(bucket.getProductIds(), updatedBucket.getProductIds());

        bucketService.reset(updatedBucket.getId());

        BucketDto emptyBucket = bucketService.findById(updatedBucket.getId());

        Assertions.assertEquals(BucketDto.builder()
                .id(saveFirstUser.getId())
                .productIds(Map.of())
                .build(), emptyBucket);
    }

    @DisplayName("Test that bucket was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test4@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456721");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        Assertions.assertEquals(1, bucketService.findAll().size());

        long bucketId = bucketService.findById(saveFirstUser.getId()).getId();
        bucketService.deleteById(bucketId);

        Assertions.assertEquals(0, bucketService.findAll().size());
    }

    @DisplayName("Test that delete bucket throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> bucketService.deleteById(1L));

        Assertions.assertEquals("Bucket with id - 1 doesn't exist!", exception.getMessage());
    }
}
