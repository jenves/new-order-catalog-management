package br.com.order.catalog.management.controller.product.model;

import br.com.order.catalog.management.domain.product.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    BigDecimal price,
    ProductType type,
    Boolean active
) {

}

