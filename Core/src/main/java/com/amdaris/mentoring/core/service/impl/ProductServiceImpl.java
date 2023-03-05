package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.repository.ProductRepository;
import com.amdaris.mentoring.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .getContent();
    }

    @Override
    public Optional<Product> findById(long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return product;
        }

        throw new NoSuchElementException("Product with id - " + id + " doesn't exist!");
    }

    @Override
    public long save(Product product) {
        Optional<Product> byTitle = productRepository.findByTitle(product.getTitle());

        if (byTitle.isPresent()) {
            throw new EntityExistsException("Product with title - " + product.getTitle() + ", exists!");
        }
        return productRepository.save(product).getId();
    }

    @Override
    public long update(Product product, long id) {
        Optional<Product> byId = productRepository.findById(id);

        if (byId.isPresent()) {
            return productRepository.save(product).getId();
        }
        throw new NoSuchElementException("Product with id - " + id + " doesn't exist!");
    }

    @Override
    public long deleteById(long id) {
        Optional<Product> byId = productRepository.findById(id);

        if (byId.isPresent()) {
            productRepository.delete(byId.get());
            return byId.get().getId();
        }
        throw new NoSuchElementException("Product with id - " + id + " doesn't exist!");
    }

    @Override
    public Optional<Product> findByTitle(String title) {
        Optional<Product> product = productRepository.findByTitle(title);
        if (product.isPresent()) {
            return product;
        } else {
            throw new NoSuchElementException("Product with title - " + title + " doesn't exist!");
        }
    }

    @Override
    public void clear() {
        productRepository.deleteAll();
    }
}
