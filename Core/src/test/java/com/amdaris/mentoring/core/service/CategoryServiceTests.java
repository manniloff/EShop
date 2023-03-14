package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class CategoryServiceTests {
    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void beforeEach() {
        categoryService.clear();
    }

    @DisplayName("Test that all categories were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, categoryService.findAll().size());

        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        Category clothingCategory = new Category();
        clothingCategory.setTitle("Clothing");

        categoryService.save(clothingCategory);

        Assertions.assertEquals(2, categoryService.findAll().size());
    }

    @DisplayName("Test that found by title category from database")
    @Test
    public void findByTitle_dataIsPresent_returnExistingData() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");
        vehicleCategory.setProducts(Set.of());

        categoryService.save(vehicleCategory);

        Optional<Category> foundByTitle = categoryService.findByTitle(vehicleCategory.getTitle());

        Assertions.assertEquals(Optional.of(vehicleCategory), foundByTitle);
    }

    @DisplayName("Test that found by title category throws error when didn't found any item")
    @Test
    public void findByTitle_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> categoryService.findByTitle("Vehicle"));

        Assertions.assertEquals("Category with title - Vehicle doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by id category from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        Optional<Category> foundByTitle = categoryService.findByTitle("Vehicle");
        Optional<Category> category = categoryService.findById((foundByTitle.get().getId()));

        Assertions.assertEquals(vehicleCategory.getTitle(), category.get().getTitle());
    }

    @DisplayName("Test that found by id category throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> categoryService.findById((short) 1L));

        Assertions.assertEquals("Category with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that category was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        List<Category> categories = categoryService.findAll();

        Assertions.assertEquals(1, categories.size());
        Assertions.assertEquals("Vehicle", categories.get(0).getTitle());
    }

    @DisplayName("Test that save category throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        Category repeateVehicleCategory = new Category();
        repeateVehicleCategory.setTitle("Vehicle");

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> categoryService.save(repeateVehicleCategory));

        Assertions.assertEquals("Category with title - Vehicle, exists!", exception.getMessage());
    }

    @DisplayName("Test that category was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        short categoryId = categoryService.findByTitle(vehicleCategory.getTitle()).get().getId();

        vehicleCategory.setTitle("Vehicle Updated");
        categoryService.update(vehicleCategory, categoryId);

        Optional<Category> updatedCategory = categoryService.findById(categoryId);

        Assertions.assertEquals("Vehicle Updated", updatedCategory.get().getTitle());
    }

    @DisplayName("Test that update category throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> categoryService.update(vehicleCategory, (short) 1));

        Assertions.assertEquals("Category with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that category was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        Category vehicleCategory = new Category();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        List<Category> categories = categoryService.findAll();
        Assertions.assertEquals(1, categories.size());
        categoryService.deleteById(categories.get(0).getId());

        Assertions.assertEquals(0, categoryService.findAll().size());
    }

    @DisplayName("Test that delete category throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> categoryService.deleteById((short) 1));

        Assertions.assertEquals("Category with id - 1 doesn't exist!", exception.getMessage());
    }
}
