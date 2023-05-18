package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.common.model.BucketDetails;
import com.amdaris.mentoring.common.model.OrderInfo;
import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.dto.criteria.OrderSearchCriteria;
import com.amdaris.mentoring.core.util.PageView;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> findAll();

    Page<OrderDto> findByCriteria(OrderSearchCriteria criteria, PageView pageView);

    OrderDto findById(long id);

    OrderDto save(OrderDto orderDto);

    OrderDto update(OrderDto orderDto, UUID transId);

    void deleteById(long id);

    void clear();

    OrderInfo checkout(BucketDetails bucket, UUID transId);
}
