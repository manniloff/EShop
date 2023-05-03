package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.criteria.OrderSearchCriteria;
import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.service.CategoryService;
import com.amdaris.mentoring.core.service.KafkaMessageSender;
import com.amdaris.mentoring.core.service.OrderService;
import com.amdaris.mentoring.core.service.ProductService;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final KafkaMessageSender orderDetailsMessageSender;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping(value = "/filter", produces = "application/json")
    ResponseEntity<?> findByCriteriaAll(PageView pageView, OrderSearchCriteria criteria) {
        log.info("Try to get list of products by criteria");
        return ResponseEntity.ok(orderService.findByCriteria(criteria, pageView));
    }

    @GetMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> findAll() {
        log.info("Try to get list of orders");
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> findById(@PathVariable long id) {
        log.info("Try to get order by id - {}", id);
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody OrderDto orderDto) {
        log.info("Try to save new order with id - {}", orderDto);
        return new ResponseEntity<>(orderService.save(orderDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody OrderDto orderDto, @PathVariable UUID id) {
        log.info("Try to update order with id- {}", id);
        return new ResponseEntity<>(orderService.update(orderDto, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete order with id - {}", id);
        orderService.deleteById(id);
        return ResponseEntity.ok("Order with id - " + id + ", was deleted");
    }

    @GetMapping(value = "/test", produces = "application/json")
    ResponseEntity<?> test() {
        log.info("Try to test");
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
        secondOrder.setCreationDate(LocalDateTime.now().withNano(0).plusHours(3));
        secondOrder.setTransId(new UUID(1, 2));
        secondOrder.setUserId(new User().getId());
        secondOrder.setStatus("created");
        secondOrder.setProducts(List.of(firstProduct.getId(), firstProduct.getId()));
        secondOrder.setOrderPrice(15.0);

        orderService.save(secondOrder);

        OrderDto thirdOrder = new OrderDto();
        thirdOrder.setCreationDate(LocalDateTime.now().withNano(0).plusHours(3));
        thirdOrder.setTransId(new UUID(1, 3));
        thirdOrder.setUserId(new User().getId());
        thirdOrder.setStatus("canceled");
        thirdOrder.setProducts(List.of(secondProduct.getId(), secondProduct.getId()));
        thirdOrder.setOrderPrice(10.0);

        orderService.save(thirdOrder);

        OrderDto fourthOrder = new OrderDto();
        fourthOrder.setCreationDate(LocalDateTime.now().withNano(0).plusHours(3));
        fourthOrder.setTransId(new UUID(1, 4));
        fourthOrder.setUserId(new User().getId());
        fourthOrder.setStatus("in_progress");
        fourthOrder.setProducts(List.of(firstProduct.getId(), secondProduct.getId(), firstProduct.getId()));
        fourthOrder.setOrderPrice(550.0);

        orderService.save(fourthOrder);

        OrderDto fifthOrder = new OrderDto();
        fifthOrder.setCreationDate(LocalDateTime.now().withNano(0).plusHours(3));
        fifthOrder.setTransId(new UUID(1, 5));
        fifthOrder.setUserId(new User().getId());
        fifthOrder.setStatus("completed");
        fifthOrder.setProducts(List.of(secondProduct.getId(), firstProduct.getId(), secondProduct.getId()));
        fifthOrder.setOrderPrice(1500.0);

        orderService.save(fifthOrder);
        return ResponseEntity.ok("Test");
    }

    @PostMapping(value = "/send/to/pay")
    public ResponseEntity<?> sendOrderToPay(@RequestBody OrderDetails orderDetails) {
        orderDetailsMessageSender.send(orderDetails);
        return ResponseEntity.ok("message was sent to kafka");
    }
}
