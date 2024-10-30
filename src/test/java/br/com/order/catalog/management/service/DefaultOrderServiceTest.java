package br.com.order.catalog.management.service;

import br.com.order.catalog.management.domain.order.*;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.entity.OrderJpaEntity;
import br.com.order.catalog.management.exceptions.ResourceNotFoundException;
import br.com.order.catalog.management.mapper.OrderMapper;
import br.com.order.catalog.management.repository.OrderRepository;

import br.com.order.catalog.management.specification.filter.OrderFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private DefaultOrderService orderService;

    private UUID orderId;
    private PreOrder preOrder;
    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setup() {
        orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        List<PreOrderItem> preOrderItems = Collections.singletonList(new PreOrderItem(productId, 10));
        Product product = Product.newProduct("Smartphone", new BigDecimal("799.99"), ProductType.PRODUCT, true);
        orderItem = OrderItem.newOrderItem(product, 2);

        preOrder = new PreOrder(OrderStatus.OPEN, preOrderItems, 10);
        order = Order.newOrder(OrderStatus.OPEN, Set.of(orderItem), 10);
    }

    @Test
    void shouldReturnPagedOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        OrderJpaEntity orderJpaEntity = new OrderJpaEntity();

        List<OrderJpaEntity> orderJpaEntities = List.of(orderJpaEntity);
        Page<OrderJpaEntity> expectedPage = new PageImpl<>(orderJpaEntities);

        OrderFilter orderFilter = new OrderFilter();

        when(orderRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);
        when(orderMapper.toDomain(orderJpaEntity)).thenReturn(order);

        Page<Order> result = orderService.getOrders(pageable, orderFilter);

        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getContent().size(), result.getContent().size());
        verify(orderRepository).findAll(any(Specification.class), eq(pageable));
        verify(orderMapper, atLeastOnce()).toDomain(any(OrderJpaEntity.class));
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, Instant.now(), null, "Laptop", new BigDecimal("1200.00"), ProductType.PRODUCT, true);

        preOrder = new PreOrder(OrderStatus.OPEN, List.of(new PreOrderItem(productId, 1)), 10);

        when(productService.getProductsById(preOrder.getItemIds())).thenReturn(Set.of(product));
        when(orderMapper.toEntity(any())).thenReturn(new OrderJpaEntity());
        when(orderRepository.save(any())).thenReturn(new OrderJpaEntity());
        when(orderMapper.toDomain(any(OrderJpaEntity.class))).thenReturn(order);

        Order result = orderService.createOrder(preOrder);

        assertEquals(order, result);
        verify(orderRepository).save(any());
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new OrderJpaEntity()));
        when(orderMapper.toDomain(any(OrderJpaEntity.class))).thenReturn(order);
        when(orderMapper.toEntity(any())).thenReturn(new OrderJpaEntity());
        when(orderRepository.save(any())).thenReturn(new OrderJpaEntity());

        Order result = orderService.updateOrder(orderId, preOrder);

        assertEquals(order, result);
        verify(orderRepository).save(any());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderId, preOrder));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldReturnOrderByIdSuccessfully() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new OrderJpaEntity()));
        when(orderMapper.toDomain(any(OrderJpaEntity.class))).thenReturn(order);

        Order result = orderService.getOrderById(orderId);

        assertEquals(order, result);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderByIdNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void shouldDeleteOrderByIdSuccessfully() {
        when(orderRepository.existsById(orderId)).thenReturn(true);

        orderService.deleteOrderById(orderId);

        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentOrder() {
        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrderById(orderId));
        verify(orderRepository, never()).deleteById(any());
    }
}
