package br.com.order.catalog.management.controller.order;

import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.OrderResponse;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.mapper.OrderMapper;
import br.com.order.catalog.management.mapper.PreOrderMapper;
import br.com.order.catalog.management.service.OrderService;

import br.com.order.catalog.management.specification.filter.OrderFilter;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.hateoas.EntityModel;

import java.util.UUID;

@RestController
public class DefaultOrderController implements OrderController {

  private final OrderService orderService;

  private final OrderMapper orderMapper;

  private final PreOrderMapper preOrderMapper;

  public DefaultOrderController(OrderService orderService, OrderMapper orderMapper,
      PreOrderMapper preOrderMapper) {
    this.orderService = orderService;
    this.orderMapper = orderMapper;
    this.preOrderMapper = preOrderMapper;
  }

  public EntityModel<OrderResponse> findById(UUID id) {
    final var order = orderService.getOrderById(id);
    final var resource = EntityModel.of(orderMapper.toResponse(order));

    Link selfLink = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(OrderController.class).findById(id)).withSelfRel();
    resource.add(selfLink);

    resource.add(WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(OrderController.class).update(id,
                new UpdateOrderRequest(order.getStatus(),
                    order.getItems().stream().map(preOrderMapper::toPreOrderItem).toList(),
                    order.getDiscount())))
        .withRel("update"));
    resource.add(
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).deleteById(id))
            .withRel("delete"));

    return resource;
  }

  public Page<OrderResponse> findAll(Pageable pageable, String status) {
    final var pageOrder = orderService.getOrders(pageable , new OrderFilter(status));
    return pageOrder.map(orderMapper::toResponse);
  }

  public EntityModel<OrderResponse> create(CreateOrderRequest request) {

    final var order = orderService.createOrder(preOrderMapper.toDomain(request));

    final var resource = EntityModel.of(orderMapper.toResponse(order));

    Link selfLink = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(OrderController.class).create(request)).withSelfRel();
    resource.add(selfLink);

    return resource;
  }

  public EntityModel<OrderResponse> update(UUID id, UpdateOrderRequest request) {
    final var order = orderService.updateOrder(id, preOrderMapper.toDomain(request));
    final var resource = EntityModel.of(orderMapper.toResponse(order));

    Link selfLink = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(OrderController.class).update(id, request)).withSelfRel();
    resource.add(selfLink);

    return resource;
  }

  public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
    orderService.deleteOrderById(id);
    return ResponseEntity.noContent().build();
  }
}
