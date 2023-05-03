package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.dto.converter.OrderConverter;
import com.amdaris.mentoring.core.dto.criteria.OrderSearchCriteria;
import com.amdaris.mentoring.core.model.Order;
import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.repository.OrderRepository;
import com.amdaris.mentoring.core.repository.ProductRepository;
import com.amdaris.mentoring.core.service.BucketService;
import com.amdaris.mentoring.core.service.OrderService;
import com.amdaris.mentoring.core.util.OrderStatus;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BucketService bucketService;

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderConverter.toOrderDto.apply(order))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDto> findByCriteria(OrderSearchCriteria criteria, PageView pageView) {
        List<String> statuses = Optional.ofNullable(criteria.getStatuses())
                .orElse(Arrays.stream(OrderStatus.values()).map(OrderStatus::name).collect(Collectors.toList()));

        List<Long> products = Optional.ofNullable(criteria.getProductIds())
                .orElse(productRepository.findAll().stream().map(Product::getId).collect(Collectors.toList()));

        Page<Order> result = orderRepository.findWithFilter(criteria.getStartPeriod(), criteria.getEndPeriod(),
                statuses,
                products,
                criteria.getMinOrderPrice(), criteria.getMaxOrderPrice(),
                PageRequest.of(pageView.getPage(), pageView.getPageSize())
                        .withSort(pageView.getSortDirection(), pageView.getSort())
        );

        return result.map(order -> OrderConverter.toOrderDto.apply(order));
    }

    @Override
    public OrderDto findById(long id) {
        return orderRepository.findById(id)
                .map(order -> OrderConverter.toOrderDto.apply(order))
                .orElseThrow(() -> new NoSuchElementException("Order with id - " + id + " doesn't exist!"));
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        orderRepository.findByTransId(orderDto.getTransId())
                .ifPresent(order -> {
                    throw new EntityExistsException("Order with transId - " + orderDto.getTransId() + " already exists!");
                });
        Order apply = OrderConverter.toOrder.apply(orderDto);
        apply.setProducts(productRepository.findAllByIdIn(orderDto.getProducts()));
        Order order = orderRepository.save(apply);
        return OrderConverter.toOrderDto.apply(order);
    }

    @Override
    public OrderDto update(OrderDto orderDto, UUID transId) {
        Order order = orderRepository.findByTransId(transId)
                .orElseThrow(() -> new NoSuchElementException("Order with transId - " + transId + " doesn't exist!"));

        orderDto.setId(order.getId());
        Order savedOrder = orderRepository.saveAndFlush(OrderConverter.toOrder.apply(orderDto));

        if (orderDto.getStatus().equals(OrderStatus.paid.name()) || orderDto.getStatus().equals(OrderStatus.completed.name())) {
            bucketService.reset(orderDto.getUserId());
        }
        return OrderConverter.toOrderDto.apply(savedOrder);
    }

    @Override
    public void deleteById(long id) {
        orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    return order;
                })
                .orElseThrow(() -> new NoSuchElementException("Order with id - " + id + " doesn't exist!"));
    }

    @Override
    public void clear() {
        orderRepository.deleteAll();
    }

    @Override
    public OrderDto checkout(BucketDto bucket, UUID transId) {
        List<Product> products = productRepository.findAllByIdIn(bucket.getProductIds()
                .keySet());

        Order order = Order.builder()
                .transId(transId)
                .creationDate(LocalDateTime.now().withNano(0))
                .status(OrderStatus.created)
                .products(products)
                .orderPrice(products.stream()
                        .mapToDouble(Product::getPrice)
                        .sum())
                .build();

        OrderDto newOrder = OrderConverter.toOrderDto.apply(orderRepository.save(order));
        newOrder.setUserId(bucket.getId());

        return newOrder;
    }
}
