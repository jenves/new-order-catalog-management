package br.com.order.catalog.management.mapper;

import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.OrderResponse;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.domain.order.Order;
import br.com.order.catalog.management.domain.order.OrderItem;
import br.com.order.catalog.management.domain.order.PreOrderItem;
import br.com.order.catalog.management.entity.OrderItemJpaEntity;
import br.com.order.catalog.management.entity.OrderJpaEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  Order toDomain(OrderJpaEntity orderEntityJpa);

  OrderResponse toResponse(Order order);

  default OrderJpaEntity toEntity(Order order) {
    final var orderJpaEntity = new OrderJpaEntity();
    final var orderItemJpaEntities = getOrderItemJpaEntities(order, orderJpaEntity);
    orderJpaEntity.setId(order.getId());
    orderJpaEntity.setDiscount(order.getDiscount());
    orderJpaEntity.setStatus(order.getStatus());
    orderJpaEntity.setItems(orderItemJpaEntities);
    orderJpaEntity.setTotal(order.getTotal());
    return orderJpaEntity;
  }

  private static List<OrderItemJpaEntity> getOrderItemJpaEntities(Order order,
      OrderJpaEntity orderJpaEntity) {
    return order.getItems().stream()
        .map(orderItem -> OrderItemJpaEntity.from(orderItem, orderJpaEntity)).toList();
  }

  @Mapping(target = "items", ignore = true)
  Order toDomain(CreateOrderRequest request);

  Order toDomain(UpdateOrderRequest request);

  PreOrderItem toPreOrderItem(OrderItem orderItem);

}
