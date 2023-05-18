package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByTitle(String title);

    List<Product> findAllByTitleContaining(String title);

    @Query(nativeQuery = true, value = "select p.id , p.description, p.price, p.sale,  p.title  from product p " +
            "    left outer join product_category_relation pcr " +
            "            on p.id=pcr.product_id " +
            "    left outer join category c " +
            "            on pcr.category_id=c.id " +
            "    where ( c.id in (:categories)) " +
            "        and (:title is null or p.title like %:title%) " +
            "        and (:description is null or p.description like %:description%) \n" +
            "        and (:minPrice is null or p.price >= :minPrice) " +
            "        and (false= :maxPrice or p.price <= :maxPrice) " +
            "        and (:minSale is null or p.sale >= :minSale) " +
            "        and (false= :maxSale or p.sale <= :maxSale) ")
    Page<Product> findWithFilter(@Param("title") String title, @Param("description") String description,
                                 @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice,
                                 @Param("minSale") short minSale, @Param("maxSale") short maxSale,
                                 @Param("categories") Collection<Short> categories,
                                 Pageable pageable
    );

    List<Product> findAllByIdIn(Collection<Long> productIds);
}
