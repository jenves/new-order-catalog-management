package br.com.order.catalog.management.domain.order;

import br.com.order.catalog.management.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record PreOrder(
    OrderStatus status,
    List<PreOrderItem> items,
    Integer discount
) {

  public Set<UUID> getItemIds() {
    return items().stream().map(PreOrderItem::id).collect(Collectors.toSet());
  }

  public Integer getItemAmountByItemId(UUID id) {
    return items.stream().filter(item -> item.id().equals(id))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"))
        .amount();
  }
}
