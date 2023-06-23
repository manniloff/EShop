package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM user u where full_name like %:fullName%")
    List<User> findByFullName(@Param("fullName") String fullName);

    @Query(nativeQuery = true, value = "SELECT u.* FROM user u" +
            " left outer join user_address_relation uar on u.id = uar.user_id" +
            " left outer join address a on uar.address_id = a.id" +
            " left outer join role r on u.role_id = r.id" +
            " where (a.id in (:addressesId))" +
            " and (:email is null or u.email like %:email%)" +
            " and (:phoneNumber is null or u.phone_number like %:phoneNumber%)" +
            " and (:firstName is null or u.first_name like %:firstName%)" +
            " and (:lastName is null or u.last_name like %:lastName%)" +
            " and (:role is null or r.role_type like %:role%)" +
            " and (u.birthday between :startPeriod and :endPeriod)"
    )
    Page<User> findByCriteria(@Param("addressesId") Collection<Long> addressesId, @Param("email") String email,
                              @Param("phoneNumber") String phoneNumber, @Param("firstName") String firstName,
                              @Param("lastName") String lastName, @Param("role") String role,
                              @Param("startPeriod") LocalDate startPeriod, @Param("endPeriod") LocalDate endPeriod,
                              Pageable pageable
    );
}
