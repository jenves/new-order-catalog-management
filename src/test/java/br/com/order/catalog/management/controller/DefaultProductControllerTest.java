package br.com.order.catalog.management.controller;

import br.com.order.catalog.management.controller.product.DefaultProductController;
import br.com.order.catalog.management.controller.product.model.CreateProductRequest;
import br.com.order.catalog.management.controller.product.model.ProductResponse;
import br.com.order.catalog.management.controller.product.model.UpdateProductRequest;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.mapper.ProductMapper;
import br.com.order.catalog.management.service.ProductService;
import br.com.order.catalog.management.specification.filter.ProductFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultProductControllerTest {

    @InjectMocks
    private DefaultProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        product = new Product(productId, Instant.now(), null, "Product Name", new BigDecimal("100.00"), null, true);
    }

    @Test
    void shouldFindProductById() {
        when(productService.getProductById(productId)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(new ProductResponse(productId, "Product Name", new BigDecimal("100.00"), ProductType.PRODUCT, true));

        EntityModel<ProductResponse> response = productController.findById(productId);

        assertNotNull(response);
        assertEquals(productId, response.getContent().id());
        assertEquals("Product Name", response.getContent().name());
        assertTrue(response.hasLink("self"));
        assertTrue(response.hasLink("update"));
        assertTrue(response.hasLink("delete"));
    }

    @Test
    void shouldFindAllProducts() {

        Pageable pageable = Pageable.unpaged();

        ProductResponse productResponse = new ProductResponse(productId, "Product Name", new BigDecimal("100.00"), ProductType.PRODUCT, true);
        List<ProductResponse> productResponseList = Collections.singletonList(productResponse);
        Page<ProductResponse> expectedPage = new PageImpl<>(productResponseList);

        Product product = new Product(productId, Instant.now(), null, "Product Name", new BigDecimal("100.00"), ProductType.PRODUCT, true);
        Page<Product> mockProductPage = new PageImpl<>(Collections.singletonList(product), pageable, 1);

        when(productService.getProducts(eq(pageable), any(ProductFilter.class))).thenReturn(mockProductPage);

        Page<ProductResponse> actualPage = productController.findAll(pageable, "Product Name", ProductType.PRODUCT.toString(), true);

        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertFalse(actualPage.isEmpty());
    }



    @Test
    void shouldCreateProduct() {
        CreateProductRequest request = new CreateProductRequest("New Product", new BigDecimal("150.00"), null, true);
        when(productMapper.toDomain(request)).thenReturn(product);
        when(productService.saveProduct(any())).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(new ProductResponse(productId, "Product Name", new BigDecimal("100.00"), ProductType.PRODUCT, true));

        EntityModel<ProductResponse> response = productController.create(request);

        assertNotNull(response);
        assertEquals(productId, response.getContent().id());
        assertEquals("Product Name", response.getContent().name());
        assertTrue(response.hasLink("self"));
    }

    @Test
    void shouldUpdateProduct() {
        UpdateProductRequest updateRequest = new UpdateProductRequest("Updated Product", new BigDecimal("200.00"), null, true);
        when(productService.updateProduct(eq(productId), any())).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(new ProductResponse(productId, "Updated Product", new BigDecimal("200.00"), ProductType.PRODUCT, true));

        EntityModel<ProductResponse> response = productController.update(productId, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Product", response.getContent().name());
        assertTrue(response.hasLink("self"));
    }

    @Test
    void shouldDeleteProductById() {
        doNothing().when(productService).deleteProductById(productId);

        ResponseEntity<Void> response = productController.deleteById(productId);

        assertEquals(204, response.getStatusCodeValue());
        verify(productService).deleteProductById(productId);
    }
}
