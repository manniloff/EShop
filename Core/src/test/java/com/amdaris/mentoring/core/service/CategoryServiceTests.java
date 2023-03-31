package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.CategoryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

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

        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        CategoryDto clothingCategory = new CategoryDto();
        clothingCategory.setTitle("Clothing");

        categoryService.save(clothingCategory);

        Assertions.assertEquals(2, categoryService.findAll().size());
    }

    @DisplayName("Test that found by title category from database")
    @Test
    public void findByTitle_dataIsPresent_returnExistingData() {
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        CategoryDto foundByTitle = categoryService.findByTitle(vehicleCategory.getTitle());

        Assertions.assertEquals(vehicleCategory.getTitle(), foundByTitle.getTitle());
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
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        CategoryDto save = categoryService.save(vehicleCategory);

        CategoryDto category = categoryService.findById(save.getId());

        Assertions.assertEquals(vehicleCategory.getTitle(), category.getTitle());
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
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        List<CategoryDto> categories = categoryService.findAll();

        Assertions.assertEquals(1, categories.size());
        Assertions.assertEquals("Vehicle", categories.get(0).getTitle());
    }

    @DisplayName("Test that save category throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        CategoryDto repeateVehicleCategory = new CategoryDto();
        repeateVehicleCategory.setTitle("Vehicle");

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> categoryService.save(repeateVehicleCategory));

        Assertions.assertEquals("Category with title - Vehicle, exists!", exception.getMessage());
    }

    @DisplayName("Test that category was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        CategoryDto category = categoryService.save(vehicleCategory);

        vehicleCategory.setTitle("Vehicle Updated");
        CategoryDto update = categoryService.update(vehicleCategory, category.getId());

        Assertions.assertEquals("Vehicle Updated", update.getTitle());
    }

    @DisplayName("Test that update category throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> categoryService.update(vehicleCategory, (short) 1));

        Assertions.assertEquals("Category with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that category was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("Vehicle");

        categoryService.save(vehicleCategory);

        List<CategoryDto> categories = categoryService.findAll();
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
