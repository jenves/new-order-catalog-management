package br.com.order.catalog.management.controller.order.model;

import br.com.order.catalog.management.domain.order.OrderStatus;
import br.com.order.catalog.management.domain.order.PreOrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderRequest(
    @NotNull(message = "Status is required") OrderStatus status,
    @Valid List<PreOrderItem> items,
    @Min(value = 0, message = "Discount must be greater than or equal to zero") Integer discount
) {

}
