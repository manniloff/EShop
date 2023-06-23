package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.common.model.BucketDetails;
import com.amdaris.mentoring.common.model.OrderInfo;
import com.amdaris.mentoring.common.model.ProductDetails;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Page<OrderDto> findByCriteria(OrderSearchCriteria criteria, PageView pageView) {
        List<String> statuses = Optional.ofNullable(criteria.getStatuses())
                .orElse(Arrays.stream(OrderStatus.values()).map(OrderStatus::name).collect(Collectors.toList()));

        List<Long> products = Optional.ofNullable(criteria.getProductIds())
                .orElse(productRepository.findAll().stream().map(Product::getId).collect(Collectors.toList()));

        return orderRepository.findWithFilter(criteria.getStartPeriod(), criteria.getEndPeriod(),
                statuses,
                products,
                criteria.getMinOrderPrice(), criteria.getMaxOrderPrice(),
                PageRequest.of(pageView.getPage(), pageView.getPageSize())
                        .withSort(pageView.getSortDirection(), pageView.getSort())
        ).map(order -> OrderConverter.toOrderDto.apply(order));
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
    public OrderInfo checkout(BucketDetails bucket, UUID transId) {
        List<Product> products = productRepository.findAllByIdIn(bucket.getProductIds()
                .keySet());

        List<ProductDetails> productInfo = products.stream()
                .map(product -> ProductDetails.builder()
                        .title(product.getTitle())
                        .price(product.getPrice())
                        .count(bucket.getProductIds().get(product.getId()))
                        .build())
                .collect(Collectors.toList());

        Order order = Order.builder()
                .transId(transId)
                .creationDate(LocalDateTime.now().withNano(0))
                .status(OrderStatus.created)
                .products(products)
                .orderPrice(products.stream()
                        .mapToDouble(Product::getPrice)
                        .sum())
                .build();

        long orderId = orderRepository.save(order).getId();

        return OrderInfo.builder()
                .id(orderId)
                .products(productInfo)
                .build();
    }
}
