package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.converter.ProductConverter;
import com.amdaris.mentoring.core.dto.criteria.ProductSearchCriteria;
import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.repository.CategoryRepository;
import com.amdaris.mentoring.core.repository.ProductRepository;
import com.amdaris.mentoring.core.service.ProductService;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductDto> findByCriteria(PageView pageView, ProductSearchCriteria productSearchCriteria) {
        List<Category> categories = Optional.ofNullable(productSearchCriteria.getCategories()).orElse(categoryRepository.findAll());
        return productRepository.findWithFilter(productSearchCriteria.getTitle(), productSearchCriteria.getDescription(),
                productSearchCriteria.getMinPrice(), productSearchCriteria.getMaxPrice(),
                productSearchCriteria.getMinSale(), productSearchCriteria.getMaxSale(),
                categories.stream().map(Category::getId).collect(Collectors.toList()),
                PageRequest.of(pageView.getPage(), pageView.getPageSize())
                        .withSort(pageView.getSortDirection(), pageView.getSort())
        ).map(product -> ProductConverter.toProductDto.apply(product));
    }

    @Override
    public List<ProductDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(product -> ProductConverter.toProductDto.apply(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findAllPageableAndSortAndFilter(PageView pageView) {
        return null;
    }

    @Override
    public ProductDto findById(long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ProductConverter.toProductDto.apply(product.get());
        }

        throw new NoSuchElementException("Product with id - " + id + " doesn't exist!");
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Optional<Product> byTitle = productRepository.findByTitle(productDto.getTitle());

        if (byTitle.isPresent()) {
            throw new EntityExistsException("Product with title - " + productDto.getTitle() + ", exists!");
        }
        Product product = ProductConverter.toProduct.apply(productDto);
        Product save = productRepository.save(product);
        return ProductConverter.toProductDto.apply(save);
    }

    @Override
    public ProductDto update(ProductDto productDto, long id) {
        Optional<Product> byId = productRepository.findById(id);

        if (byId.isPresent()) {
            Product product = ProductConverter.toProduct.apply(productDto);
            return ProductConverter.toProductDto.apply(productRepository.save(product));
        }
        throw new NoSuchElementException("Product with id - " + id + " doesn't exist!");
    }

    @Override
    public long findProductId(String title) {
        Optional<Product> byTitle = productRepository.findByTitle(title);
        if (byTitle.isPresent()) {
            return byTitle.get().getId();
        }
        throw new NoSuchElementException("Product with title - " + title + " doesn't exist!");
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
    public ProductDto findByTitle(String title) {
        Optional<Product> product = productRepository.findByTitle(title);
        if (product.isPresent()) {
            return ProductConverter.toProductDto.apply(product.get());
        } else {
            throw new NoSuchElementException("Product with title - " + title + " doesn't exist!");
        }
    }

    @Override
    public List<ProductDto> findAllByTitle(String title) {
        return productRepository.findAllByTitleContaining(title)
                .stream()
                .map(product -> ProductConverter.toProductDto.apply(product))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        productRepository.deleteAll();
    }
}
