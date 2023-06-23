package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Address;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(nativeQuery = true, value = "SELECT a.* FROM address a where full_address like %:addressPart%")
    List<Address> findByFilter(@Param("addressPart") String addressPart);

    @Query("SELECT a.id FROM Address a")
    List<Long> findAllIds();
}
