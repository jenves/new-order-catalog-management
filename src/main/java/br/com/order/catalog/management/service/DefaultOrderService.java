package br.com.order.catalog.management.service;

import br.com.order.catalog.management.domain.order.Order;
import br.com.order.catalog.management.domain.order.OrderItem;
import br.com.order.catalog.management.domain.order.PreOrder;
import br.com.order.catalog.management.entity.OrderJpaEntity;
import br.com.order.catalog.management.exceptions.ResourceNotFoundException;
import br.com.order.catalog.management.mapper.OrderMapper;
import br.com.order.catalog.management.repository.OrderRepository;
import java.util.HashSet;
import java.util.UUID;

import br.com.order.catalog.management.specification.OrderSpecification;
import br.com.order.catalog.management.specification.filter.OrderFilter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultOrderService implements OrderService {

  private final static String ORDER_NOT_FOUND_MESSAGE = "Order not found";

  private final OrderRepository orderRepository;

  private final OrderMapper orderMapper;

  private final ProductService productService;

  public DefaultOrderService(OrderRepository orderRepository, OrderMapper orderMapper,
      ProductService productService) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.productService = productService;
  }

  public Page<Order> getOrders(Pageable pageable, OrderFilter orderFilter) {
    Specification<OrderJpaEntity> specification = Specification.where(OrderSpecification.withStatus(orderFilter));
    final var pageOrderEntity = orderRepository.findAll(specification, pageable);
    return pageOrderEntity.map(orderMapper::toDomain);
  }

  @Transactional
  public Order createOrder(PreOrder preOrder) {
    final var orderItems = getOrderItems(preOrder);

    final var order = Order.newOrder(preOrder.status(), orderItems, preOrder.discount());

    final var orderEntity = orderMapper.toEntity(order);
    return orderMapper.toDomain(orderRepository.save(orderEntity));
  }

  @Transactional
  public Order updateOrder(UUID id, PreOrder preOrder) {

    final var newStatus = preOrder.status();
    final var newOrderItems = getOrderItems(preOrder);
    final var newDiscount = preOrder.discount();

    final var order = getOrderById(id);

    order.update(newStatus, newOrderItems, newDiscount);

    final var orderJpaEntity = orderMapper.toEntity(order);

    final var mergedOrder = orderRepository.save(orderJpaEntity);
    return orderMapper.toDomain(mergedOrder);
  }

  public Order getOrderById(UUID id) {
    final var orderEntityJpa = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE));
    return orderMapper.toDomain(orderEntityJpa);
  }

  private HashSet<OrderItem> getOrderItems(PreOrder preOrder) {
    final var products = productService.getProductsById(preOrder.getItemIds());
    final var orderItems = new HashSet<OrderItem>();
    products.forEach(product -> orderItems.add(
        OrderItem.newOrderItem(product, preOrder.getItemAmountByItemId(product.getId()))));
    return orderItems;
  }

  @Transactional
  public void deleteOrderById(UUID id) {
    if (!orderRepository.existsById(id)) {
      throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE);
    }
    orderRepository.deleteById(id);
  }
}
