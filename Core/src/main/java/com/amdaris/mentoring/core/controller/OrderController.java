package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.common.model.BucketDetails;
import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.OrderInfo;
import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.dto.criteria.OrderSearchCriteria;
import com.amdaris.mentoring.core.service.CategoryService;
import com.amdaris.mentoring.core.service.KafkaMessageSender;
import com.amdaris.mentoring.core.service.OrderService;
import com.amdaris.mentoring.core.service.ProductService;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/core/order")
@RequiredArgsConstructor
public class OrderController {
    private final KafkaMessageSender<OrderDetails> orderDetailsMessageSender;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping(value = "/filter", produces = "application/json")
    ResponseEntity<?> findByCriteriaAll(PageView pageView, OrderSearchCriteria criteria) {
        log.info("Try to get list of products by criteria");
        return ResponseEntity.ok(orderService.findByCriteria(criteria, pageView));
    }

    @GetMapping(value = {"", "/"}, produces = "application/json")
    List<OrderDto> findAll() {
        log.info("Try to get list of orders");
        return orderService.findAll();
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

    @PostMapping(value = "/checkout/{transId}", produces = "application/json", consumes = "application/json")
    public OrderInfo getOrderInfo(@RequestBody BucketDetails bucketDetails, @PathVariable UUID transId) {
        log.info("Try to save new order for bucket id - {}", bucketDetails.getId());
        return orderService.checkout(bucketDetails, transId);
    }

    @PostMapping(value = "/send/to/pay")
    public ResponseEntity<?> sendOrderToPay(@RequestBody OrderDetails orderDetails) {
        orderDetailsMessageSender.send(orderDetails);
        return ResponseEntity.ok("message was sent to kafka");
    }
}
