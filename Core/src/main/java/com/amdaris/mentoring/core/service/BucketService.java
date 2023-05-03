package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.BucketDto;

import java.util.List;

public interface BucketService {
    List<BucketDto> findAll();

    BucketDto findById(Long id);

    BucketDto save(long userId);

    BucketDto update(BucketDto bucket, long id);

    void deleteById(Long id);

    void reset(long id);

    void clear();
}
