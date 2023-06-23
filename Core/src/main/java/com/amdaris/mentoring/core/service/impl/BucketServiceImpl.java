package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.dto.converter.BucketConverter;
import com.amdaris.mentoring.core.model.Bucket;
import com.amdaris.mentoring.core.repository.BucketRepository;
import com.amdaris.mentoring.core.service.BucketService;
import javax.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;

    @Override
    public List<BucketDto> findAll() {
        return bucketRepository.findAll()
                .stream()
                .map(bucket -> BucketConverter.toBucketDto.apply(bucket))
                .collect(Collectors.toList());
    }

    @Override
    public BucketDto findById(Long id) {
        Bucket bucket = bucketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bucket with id - " + id + " doesn't exist!"));

        return BucketConverter.toBucketDto.apply(bucket);
    }

    @Override
    public BucketDto save(long userId) {
        bucketRepository.findById(userId)
                .ifPresent(bucket -> {
                    throw new EntityExistsException("Bucket with id - " + userId + " already exists!");
                });

        return BucketConverter.toBucketDto.apply(bucketRepository.save(Bucket.builder()
                .id(userId)
                .products(List.of())
                .build()));
    }

    @Override
    public BucketDto update(BucketDto bucketDto, long id) {
        Optional<Bucket> byId = bucketRepository.findById(id);

        if (byId.isPresent()) {
            Bucket bucket = BucketConverter.toBucket.apply(bucketDto);
            return BucketConverter.toBucketDto.apply(bucketRepository.save(bucket));
        }
        throw new NoSuchElementException("Bucket with id - " + id + " doesn't exist!");
    }

    @Override
    public void deleteById(Long id) {
        Optional<Bucket> byId = bucketRepository.findById(id);

        if (byId.isPresent()) {
            bucketRepository.delete(byId.get());
        } else {
            throw new NoSuchElementException("Bucket with id - " + id + " doesn't exist!");
        }

    }

    @Override
    public void reset(long id) {
        Optional<Bucket> bucket = bucketRepository.findById(id);

        if (bucket.isPresent()) {
            bucket.get().setProducts(List.of());
            bucketRepository.save(bucket.get());
        }
    }

    @Override
    public void clear() {
        bucketRepository.deleteAll();
    }
}
