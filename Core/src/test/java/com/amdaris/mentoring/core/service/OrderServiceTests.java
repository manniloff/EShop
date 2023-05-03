package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.criteria.OrderSearchCriteria;
import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.util.PageView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
public class OrderServiceTests {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void beforeEach() {
        orderService.clear();
    }

    @DisplayName("Test that all orders were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, orderService.findAll().size());

        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        orderService.save(firstOrder);

        OrderDto secondOrder = new OrderDto();
        secondOrder.setCreationDate(LocalDateTime.now().withNano(0).plusHours(3));
        secondOrder.setTransId(new UUID(1, 2));
        secondOrder.setUserId(new User().getId());
        secondOrder.setStatus("created");
        secondOrder.setProducts(List.of());
        secondOrder.setOrderPrice(150.0);

        orderService.save(secondOrder);

        Assertions.assertEquals(2, orderService.findAll().size());
    }

    @DisplayName("Test that found by id order from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, orderService.findAll().size());

        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        OrderDto savedOrder = orderService.save(firstOrder);

        OrderDto orderDto = orderService.findById(savedOrder.getId());

        Assertions.assertEquals(firstOrder.getTransId(), orderDto.getTransId());
        Assertions.assertEquals(firstOrder.getStatus(), orderDto.getStatus());
        Assertions.assertEquals(firstOrder.getOrderPrice(), orderDto.getOrderPrice());
    }

    @DisplayName("Test that found by id order throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> orderService.findById(1L));

        Assertions.assertEquals("Order with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by filter order from database")
    @Test
    public void findByFilter_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, orderService.findAll().size());

        CategoryDto homeCategoryDto = new CategoryDto();
        homeCategoryDto.setTitle("For Home");

        CategoryDto savedHomeCategory = categoryService.save(homeCategoryDto);

        CategoryDto vehicleCategory = new CategoryDto();
        vehicleCategory.setTitle("For vehicle");

        CategoryDto savedVehicleCategory = categoryService.save(vehicleCategory);

        ProductDto vehicleProduct = new ProductDto();
        vehicleProduct.setTitle("Vehicle");
        vehicleProduct.setDescription("Vehicle product");
        vehicleProduct.setPrice(15000.0);
        vehicleProduct.setSale((short) 0);
        vehicleProduct.setCategoryIds(List.of(savedVehicleCategory.getId()));

        ProductDto firstProduct = productService.save(vehicleProduct);

        ProductDto clothingProduct = new ProductDto();
        clothingProduct.setTitle("Clothing");
        clothingProduct.setDescription("Clothing product");
        clothingProduct.setPrice(150.0);
        clothingProduct.setSale((short) 0);
        clothingProduct.setCategoryIds(List.of(savedHomeCategory.getId()));

        ProductDto secondProduct = productService.save(clothingProduct);

        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of(firstProduct.getId(), secondProduct.getId()));
        firstOrder.setOrderPrice(100.0);

        orderService.save(firstOrder);

        OrderDto secondOrder = new OrderDto();
        secondOrder.setCreationDate(LocalDateTime.now().withNano(0).minusDays(1));
        secondOrder.setTransId(new UUID(1, 2));
        secondOrder.setUserId(new User().getId());
        secondOrder.setStatus("created");
        secondOrder.setProducts(List.of(firstProduct.getId(), firstProduct.getId()));
        secondOrder.setOrderPrice(15.0);

        orderService.save(secondOrder);

        OrderDto thirdOrder = new OrderDto();
        thirdOrder.setCreationDate(LocalDateTime.now().withNano(0).minusDays(3));
        thirdOrder.setTransId(new UUID(1, 3));
        thirdOrder.setUserId(new User().getId());
        thirdOrder.setStatus("canceled");
        thirdOrder.setProducts(List.of(secondProduct.getId(), secondProduct.getId()));
        thirdOrder.setOrderPrice(10.0);

        orderService.save(thirdOrder);

        OrderDto fourthOrder = new OrderDto();
        fourthOrder.setCreationDate(LocalDateTime.now().withNano(0).minusDays(8));
        fourthOrder.setTransId(new UUID(1, 4));
        fourthOrder.setUserId(new User().getId());
        fourthOrder.setStatus("in_progress");
        fourthOrder.setProducts(List.of(firstProduct.getId(), secondProduct.getId(), firstProduct.getId()));
        fourthOrder.setOrderPrice(550.0);

        orderService.save(fourthOrder);

        OrderDto fifthOrder = new OrderDto();
        fifthOrder.setCreationDate(LocalDateTime.now().withNano(0).minusDays(12));
        fifthOrder.setTransId(new UUID(1, 5));
        fifthOrder.setUserId(new User().getId());
        fifthOrder.setStatus("completed");
        fifthOrder.setProducts(List.of(secondProduct.getId(), firstProduct.getId(), secondProduct.getId()));
        fifthOrder.setOrderPrice(1500.0);

        orderService.save(fifthOrder);

        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        PageView pageView = new PageView();


        //filter with default values
        Assertions.assertEquals(5, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());

        //filter by date
        orderSearchCriteria.setStartPeriod(LocalDateTime.now().withNano(0).minusDays(4));
        Assertions.assertEquals(3, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());

        //filter by status
        orderSearchCriteria.setStartPeriod(LocalDateTime.now().minusDays(30).withNano(0));
        orderSearchCriteria.setStatuses(List.of("created"));

        Assertions.assertEquals(2, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());


        //filter by min price
        orderSearchCriteria.setStatuses(null);
        orderSearchCriteria.setMinOrderPrice(600.0);
        Assertions.assertEquals(1, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());

        //filter by max price
        orderSearchCriteria.setMinOrderPrice(0.0);
        orderSearchCriteria.setMaxOrderPrice(100.0);
        Assertions.assertEquals(3, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());

        //filter by min and max price
        orderSearchCriteria.setMinOrderPrice(10.0);
        orderSearchCriteria.setMaxOrderPrice(15.0);
        Assertions.assertEquals(2, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());

        //filter by product
        orderSearchCriteria.setMinOrderPrice(0.0);
        orderSearchCriteria.setMaxOrderPrice(0.0);

        orderSearchCriteria.setProductIds(List.of(firstProduct.getId()));
        Assertions.assertEquals(4, orderService.findByCriteria(orderSearchCriteria, pageView).getTotalElements());
    }

    @DisplayName("Test that order was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        orderService.save(firstOrder);

        List<OrderDto> orders = orderService.findAll();

        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(firstOrder.getTransId(), orders.get(0).getTransId());
        Assertions.assertEquals(firstOrder.getStatus(), orders.get(0).getStatus());
        Assertions.assertEquals(firstOrder.getOrderPrice(), orders.get(0).getOrderPrice());
    }

    @DisplayName("Test that save order throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        OrderDto saved = orderService.save(firstOrder);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> orderService.save(saved));

        Assertions.assertEquals("Order with transId - 00000000-0000-0001-0000-000000000001 already exists!", exception.getMessage());
    }

    @DisplayName("Test that order was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        orderService.save(firstOrder);

        firstOrder.setStatus("paid");
        OrderDto updatedOrder = orderService.update(firstOrder, firstOrder.getTransId());

        Assertions.assertEquals("paid", updatedOrder.getStatus());
    }

    @DisplayName("Test that update order throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> orderService.update(firstOrder, firstOrder.getTransId()));

        Assertions.assertEquals("Order with transId - 00000000-0000-0001-0000-000000000001 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that order was updated in database")
    @Test
    public void checkout_dataIsPresent_returnNewOrder() {
        BucketDto firstBucket = new BucketDto();
        firstBucket.setId(1L);
        firstBucket.setProductIds(Map.of(1L, 2, 2L, 2));

        UUID transId = UUID.randomUUID();
        OrderDto checkout = orderService.checkout(firstBucket, transId);

        Assertions.assertEquals("created", checkout.getStatus());
        Assertions.assertEquals(transId, checkout.getTransId());
    }

    @DisplayName("Test that order was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        OrderDto firstOrder = new OrderDto();
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setTransId(new UUID(1, 1));
        firstOrder.setUserId(new User().getId());
        firstOrder.setStatus("created");
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(100.0);

        OrderDto savedOrder = orderService.save(firstOrder);

        Assertions.assertEquals(1, orderService.findAll().size());

        long orderId = orderService.findById(savedOrder.getId()).getId();
        orderService.deleteById(orderId);

        Assertions.assertEquals(0, orderService.findAll().size());
    }

    @DisplayName("Test that delete order throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> orderService.deleteById(1L));

        Assertions.assertEquals("Order with id - 1 doesn't exist!", exception.getMessage());
    }
}
