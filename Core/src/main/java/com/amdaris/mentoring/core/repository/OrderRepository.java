package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByTransId(UUID transId);

    @Query(nativeQuery = true, value = "select distinct o.*  from order_info o \n" +
            "    left outer join product_order_relation por on o.id=por.order_id \n" +
            "    left outer join product p on por.product_id=p.id \n" +
            "    where (o.creation_date between :startPeriod and :endPeriod) \n" +
            "        and ( o.status in (:statuses)) \n" +
            "        and ( p.id in (:productIds)) \n" +
            "        and (:minOrderPrice is null or o.order_price >= :minOrderPrice) \n" +
            "        and (false= :maxOrderPrice or o.order_price <= :maxOrderPrice) "
    )
    Page<Order> findWithFilter(@Param("startPeriod") LocalDateTime startPeriod, @Param("endPeriod") LocalDateTime endPeriod,
                               @Param("statuses") Collection<String> statuses,
                               @Param("productIds") Collection<Long> productIds,
                               @Param("minOrderPrice") double minOrderPrice, @Param("maxOrderPrice") double maxOrderPrice,
                               Pageable pageable
    );
}
