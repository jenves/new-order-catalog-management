package br.com.order.catalog.management.controller;

import br.com.order.catalog.management.controller.order.DefaultOrderController;
import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.OrderResponse;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.domain.order.*;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.mapper.OrderMapper;
import br.com.order.catalog.management.mapper.PreOrderMapper;
import br.com.order.catalog.management.service.OrderService;
import br.com.order.catalog.management.specification.filter.OrderFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultOrderControllerTest {

    @InjectMocks
    private DefaultOrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PreOrderMapper preOrderMapper;

    private UUID orderId;
    private Product product;
    private Set<OrderItem> orderItemsSet;
    private List<OrderItem> orderItemList;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderId = UUID.randomUUID();
        product = Product.newProduct("Laptop", new BigDecimal("2500"), ProductType.PRODUCT, true);
        orderItemsSet = new HashSet<>(Collections.singleton(new OrderItem(orderId, Instant.now(), null, product, 5)));
        orderItemList = new ArrayList<>(orderItemsSet);
        order = new Order(orderId, Instant.now(), null, OrderStatus.OPEN, orderItemsSet, 5, new BigDecimal("12500"));
        orderResponse = new OrderResponse(orderId, OrderStatus.OPEN, orderItemList, 5, new BigDecimal("12500"));
    }

    @Test
    void testFindById() {
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        EntityModel<OrderResponse> response = orderController.findById(orderId);

        assertNotNull(response);
        assertEquals(orderResponse, response.getContent());
        assertEquals(3, response.getLinks().stream().toList().size());
    }

    @Test
    void testFindAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Order> ordersPage = new PageImpl<>(Collections.singletonList(order));

        String status = OrderStatus.OPEN.toString();

        when(orderService.getOrders(eq(pageable), any(OrderFilter.class))).thenReturn(ordersPage);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        Page<OrderResponse> response = orderController.findAll(pageable, status);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(orderResponse, response.getContent().get(0));
    }

    @Test
    void testCreate() {
        List<PreOrderItem> preOrderItems = List.of(new PreOrderItem(orderId, 5));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(OrderStatus.OPEN, preOrderItems, 5);
        PreOrder preOrder = new PreOrder(OrderStatus.OPEN, preOrderItems, 5);

        when(preOrderMapper.toDomain(createOrderRequest)).thenReturn(preOrder);
        when(orderService.createOrder(preOrder)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        EntityModel<OrderResponse> response = orderController.create(createOrderRequest);

        assertNotNull(response);
        assertEquals(orderResponse, response.getContent());
    }

    @Test
    void testUpdate() {
        List<PreOrderItem> preOrderItems = orderItemList.stream()
                .map(item -> new PreOrderItem(item.getProduct().getId(), item.getAmount()))
                .collect(Collectors.toList());
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(OrderStatus.OPEN, preOrderItems, 5);
        PreOrder updatedPreOrder = new PreOrder(OrderStatus.OPEN, preOrderItems, 5);

        when(preOrderMapper.toDomain(updateOrderRequest)).thenReturn(updatedPreOrder);
        when(orderService.updateOrder(orderId, updatedPreOrder)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        EntityModel<OrderResponse> response = orderController.update(orderId, updateOrderRequest);

        assertNotNull(response);
        assertEquals(orderResponse, response.getContent());
    }

    @Test
    void testDeleteById() {
        ResponseEntity<Void> response = orderController.deleteById(orderId);

        verify(orderService).deleteOrderById(orderId);
        assertEquals(204, response.getStatusCodeValue());
    }
}
