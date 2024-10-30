package br.com.order.catalog.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;


import br.com.order.catalog.management.controller.product.model.UpdateProductRequest;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.entity.ProductJpaEntity;
import br.com.order.catalog.management.exceptions.ProductLinkedToOrderException;
import br.com.order.catalog.management.exceptions.ResourceNotFoundException;
import br.com.order.catalog.management.mapper.ProductMapper;
import br.com.order.catalog.management.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import br.com.order.catalog.management.specification.filter.ProductFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class DefaultProductServiceTest {

  @InjectMocks
  private DefaultProductService productService;

  @Mock
  private ProductRepository productRepository;

  @Spy
  private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

  private UUID productId;
  private Product existingProduct;
  private UpdateProductRequest updateProductRequest;

  @BeforeEach
  void setUp() {
    productId = UUID.randomUUID();
    existingProduct = new Product(productId, Instant.now(), null, "Café Especial",
        new BigDecimal("100.00"), ProductType.PRODUCT, true);
    updateProductRequest = new UpdateProductRequest("Café Premium", new BigDecimal("150.00"),
        ProductType.PRODUCT, false);
  }

  @Test
  void shouldReturnProductsPage() {

    Pageable pageable = Pageable.ofSize(10);

    ProductJpaEntity productEntity = new ProductJpaEntity(UUID.randomUUID(), "Café Regular", new BigDecimal("20"), ProductType.PRODUCT, true);

    Page<ProductJpaEntity> expectedPage = new PageImpl<>(List.of(productEntity), pageable, 1);

    when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

    ProductFilter productFilter = new ProductFilter();

    Page<Product> actualPage = productService.getProducts(pageable, productFilter);

    assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
    assertEquals(expectedPage.getContent().size(), actualPage.getContent().size());
    assertNotEquals(productEntity, actualPage.getContent().get(0));

    verify(productRepository).findAll(any(Specification.class), eq(pageable));
  }

  @Test
  void shouldSaveProduct() {
    when(productRepository.save(any())).thenReturn(new ProductJpaEntity());

    Product savedProduct = productService.saveProduct(existingProduct);

    assertNotNull(savedProduct);
    verify(productRepository).save(any());
  }

  @Test
  void shouldUpdateProduct() {
    ProductJpaEntity existingProductEntity = new ProductJpaEntity(productId, "Café Antigo",
        new BigDecimal("100"), ProductType.PRODUCT, true);

    when(productRepository.findById(productId)).thenReturn(Optional.of(existingProductEntity));

    ProductJpaEntity updatedProductEntity = new ProductJpaEntity(productId, "Café Atualizado",
        new BigDecimal("150"), ProductType.PRODUCT, true);
    when(productRepository.save(any(ProductJpaEntity.class))).thenReturn(updatedProductEntity);

    Product result = productService.updateProduct(productId, updateProductRequest);

    assertNotNull(result);
    assertEquals("Café Atualizado", result.getName());
    assertEquals(0, result.getPrice().compareTo(new BigDecimal("150")));
    verify(productRepository).findById(productId);
    verify(productRepository).save(updatedProductEntity);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentProduct() {
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> productService.updateProduct(productId, updateProductRequest));
    verify(productRepository).findById(productId);
  }

  @Test
  void shouldGetProductById() {
    when(productRepository.findById(productId)).thenReturn(
        Optional.of(new ProductJpaEntity(productId, null, null, null, null)));

    Product foundProduct = productService.getProductById(productId);

    assertNotNull(foundProduct);
    assertEquals(existingProduct.getId(), foundProduct.getId());
    verify(productRepository).findById(productId);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenProductNotFound() {
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
    verify(productRepository).findById(productId);
  }

  @Test
  void shouldDeleteProductById() {
    when(productRepository.existsById(productId)).thenReturn(true);

    productService.deleteProductById(productId);

    verify(productRepository).deleteById(productId);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentProduct() {
    when(productRepository.existsById(productId)).thenReturn(false);

    assertThrows(ResourceNotFoundException.class,
        () -> productService.deleteProductById(productId));
    verify(productRepository).existsById(productId);
  }

  @Test
  void shouldThrowProductLinkedToOrderExceptionWhenProductIsLinkedToOrder() {
    when(productRepository.existsById(productId)).thenReturn(true);
    doThrow(new DataIntegrityViolationException("")).when(productRepository).deleteById(productId);

    assertThrows(ProductLinkedToOrderException.class,
        () -> productService.deleteProductById(productId));
    verify(productRepository).deleteById(productId);
  }

  @Test
  void shouldReturnProductsById() {
    Set<UUID> ids = Set.of(productId);
    when(productRepository.findAllById(ids)).thenReturn(List.of(new ProductJpaEntity()));

    Set<Product> products = productService.getProductsById(ids);

    assertFalse(products.isEmpty());
    verify(productRepository).findAllById(ids);
  }

  @Test
  void shouldReturnEmptySetWhenNoProductsFoundByIds() {
    Set<UUID> ids = Set.of(UUID.randomUUID());
    when(productRepository.findAllById(ids)).thenReturn(List.of());

    Set<Product> products = productService.getProductsById(ids);

    assertTrue(products.isEmpty());
    verify(productRepository).findAllById(ids);
  }

}
