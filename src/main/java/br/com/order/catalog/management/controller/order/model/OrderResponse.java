package br.com.order.catalog.management.controller.order.model;

import br.com.order.catalog.management.domain.order.OrderItem;
import br.com.order.catalog.management.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    OrderStatus status,
    List<OrderItem> items,
    Integer discount,
    BigDecimal total
) {

}