package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
}
