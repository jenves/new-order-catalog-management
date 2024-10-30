package br.com.order.catalog.management.mapper;

import br.com.order.catalog.management.controller.product.model.CreateProductRequest;
import br.com.order.catalog.management.controller.product.model.ProductResponse;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.entity.ProductJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void shouldMapProductToEntity() {
        Product product = new Product(UUID.randomUUID(), null, null, "Product Name", new BigDecimal("99.99"), ProductType.PRODUCT, true);
        ProductJpaEntity entity = productMapper.toEntity(product);

        assertNotNull(entity);
        assertEquals(product.getName(), entity.getName());
        assertEquals(product.getPrice(), entity.getPrice());
        assertEquals(product.getType(), entity.getType());
        assertEquals(product.isActive(), entity.isActive());
    }

    @Test
    void shouldMapEntityToProduct() {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Entity Name");
        entity.setPrice(new BigDecimal("150.00"));
        entity.setType(ProductType.PRODUCT);
        entity.setActive(true);

        Product product = productMapper.toDomain(entity);

        assertNotNull(product);
        assertEquals(entity.getName(), product.getName());
        assertEquals(entity.getPrice(), product.getPrice());
        assertEquals(entity.getType(), product.getType());
        assertEquals(entity.isActive(), product.isActive());
    }

    @Test
    void shouldMapCreateProductRequestToProduct() {
        CreateProductRequest request = new CreateProductRequest("New Product", new BigDecimal("75.00"), ProductType.PRODUCT, true);
        Product product = productMapper.toDomain(request);

        assertNotNull(product);
        assertEquals(request.name(), product.getName());
        assertEquals(request.price(), product.getPrice());
        assertEquals(request.type(), product.getType());
        assertEquals(request.active(), product.isActive());
    }

    @Test
    void shouldMapProductToResponse() {
        Product product = new Product(UUID.randomUUID(), null, null, "Product Name", new BigDecimal("99.99"), ProductType.PRODUCT, true);
        ProductResponse response = productMapper.toResponse(product);

        assertNotNull(response);
        assertEquals(product.getName(), response.name());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getType(), response.type());
        assertEquals(product.isActive(), response.active());
    }
}
