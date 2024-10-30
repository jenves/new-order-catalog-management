package br.com.order.catalog.management.mapper;

import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.domain.order.OrderItem;
import br.com.order.catalog.management.domain.order.PreOrder;
import br.com.order.catalog.management.domain.order.PreOrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreOrderMapper {

  PreOrder toPreOrder(CreateOrderRequest createOrderRequest);

  PreOrder toDomain(CreateOrderRequest request);

  PreOrderItem toPreOrderItem(OrderItem orderItem);

  PreOrder toDomain(UpdateOrderRequest request);
}

