package br.com.order.catalog.management.domain.order;

import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record PreOrderItem(
    UUID id,
    @Positive(message = "Item amount must be positive") Integer amount
) {

}
