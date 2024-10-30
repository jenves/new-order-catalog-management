package br.com.order.catalog.management.service;

import br.com.order.catalog.management.controller.product.model.UpdateProductRequest;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.entity.ProductJpaEntity;
import br.com.order.catalog.management.exceptions.ProductLinkedToOrderException;
import br.com.order.catalog.management.exceptions.ResourceNotFoundException;
import br.com.order.catalog.management.mapper.ProductMapper;
import br.com.order.catalog.management.repository.ProductRepository;
import br.com.order.catalog.management.specification.ProductSpecification;
import br.com.order.catalog.management.specification.filter.ProductFilter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultProductService implements ProductService {

  private final static String PRODUCT_NOT_FOUND_ERROR_MESSAGE = "Product not found";

  private final ProductRepository productRepository;

  private final ProductMapper productMapper;

  public DefaultProductService(ProductRepository productRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  public Page<Product> getProducts(Pageable pageable, ProductFilter productFilter) {

    Specification<ProductJpaEntity> specification = ProductSpecification.withFilters(productFilter);

    return productRepository.findAll(specification, pageable).map(productMapper::toDomain);
  }

  @Transactional
  public Product saveProduct(Product product) {
    final var productJpaEntity = productRepository.save(productMapper.toEntity(product));
    return productMapper.toDomain(productJpaEntity);
  }

  @Transactional
  public Product updateProduct(UUID id, UpdateProductRequest request) {
    final var product = getProductById(id);

    final var willBeActive = request.active();
    final var newName = request.name();
    final var newType = request.type();
    final var newPrice = request.price();

    product.update(newName, newPrice, newType, willBeActive);

    final var productJpaEntity = productMapper.toEntity(product);

    final var mergedProductJpaEntity = productRepository.save(productJpaEntity);
    return productMapper.toDomain(mergedProductJpaEntity);
  }

  public Product getProductById(UUID id) {
    final var productJpaEntity = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE));
    return productMapper.toProduct(productJpaEntity);
  }

  public void deleteProductById(UUID id) {
    if (!productRepository.existsById(id)) {
      throw new ResourceNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE);
    }
    try {
      productRepository.deleteById(id);
    } catch (DataIntegrityViolationException ex) {
      throw new ProductLinkedToOrderException("It is not possible to delete a product that is linked to an order");
    }

  }

  @Override
  public Set<Product> getProductsById(Set<UUID> items) {
    return productRepository.findAllById(items).stream().map(productMapper::toDomain)
        .collect(Collectors.toSet());
  }

}
