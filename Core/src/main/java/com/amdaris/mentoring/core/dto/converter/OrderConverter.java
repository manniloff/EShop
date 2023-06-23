package com.amdaris.mentoring.core.dto.converter;

import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.model.Order;
import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.util.OrderStatus;

import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderConverter {
    public static Function<Order, OrderDto> toOrderDto =
            order -> OrderDto.builder()
                    .id(order.getId())
                    .transId(order.getTransId())
                    .creationDate(order.getCreationDate())
                    .status(order.getStatus().name())
                    .products(order.getProducts().stream().map(Product::getId).collect(Collectors.toList()))
                    .orderPrice(order.getOrderPrice())
                    .build();
    public static Function<OrderDto, Order> toOrder =
            orderDto -> Order.builder()
                    .id(orderDto.getId())
                    .transId(orderDto.getTransId())
                    .creationDate(orderDto.getCreationDate())
                    .status(OrderStatus.valueOf(orderDto.getStatus()))
                    .products(orderDto.getProducts().stream().map(Product::new).collect(Collectors.toList()))
                    .orderPrice(orderDto.getOrderPrice())
                    .build();
}
