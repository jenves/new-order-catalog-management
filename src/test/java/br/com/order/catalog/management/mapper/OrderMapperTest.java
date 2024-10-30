package br.com.order.catalog.management.mapper;

import br.com.order.catalog.management.controller.order.DefaultOrderController;
import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.OrderResponse;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.domain.order.Order;
import br.com.order.catalog.management.domain.order.OrderItem;
import br.com.order.catalog.management.domain.order.OrderStatus;
import br.com.order.catalog.management.domain.order.PreOrderItem;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.entity.OrderItemJpaEntity;
import br.com.order.catalog.management.entity.OrderJpaEntity;
import br.com.order.catalog.management.entity.ProductJpaEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderMapperTest {

    @InjectMocks
    private DefaultOrderController orderController;

    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    private UUID orderId;
    private UUID orderItemId;
    private UUID productId;
    private OrderJpaEntity orderEntity;
    private Set<OrderItem> items;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderId = UUID.randomUUID();
        orderItemId = UUID.randomUUID();
        productId = UUID.randomUUID();

        orderEntity = new OrderJpaEntity();
        orderEntity.setId(orderId);
        orderEntity.setStatus(OrderStatus.OPEN);
        orderEntity.setDiscount(10);

        ProductJpaEntity productJpaEntity = new ProductJpaEntity(productId, "Sample Product", new BigDecimal("200"), ProductType.PRODUCT, true);
        List<OrderItemJpaEntity> orderItems = List.of(new OrderItemJpaEntity(orderItemId, orderEntity, productJpaEntity, 10));
        orderEntity.setItems(orderItems);
        orderEntity.setTotal(BigDecimal.valueOf(100.0));

        items = new HashSet<>();
        Product product = Product.newProduct("Sample Product", new BigDecimal("20"), ProductType.PRODUCT, true);
        OrderItem item = OrderItem.newOrderItem(product, 10);
        items.add(item);

        order = new Order(orderId, null, null, OrderStatus.OPEN, items, 10, BigDecimal.valueOf(100.0));

        when(orderMapper.toDomain(any(CreateOrderRequest.class))).thenReturn(order);
        when(orderMapper.toDomain(any(UpdateOrderRequest.class))).thenReturn(order);
    }

    @Test
    void testToDomainFromOrderJpaEntity() {
        when(orderMapper.toDomain(orderEntity)).thenReturn(order);

        Order result = orderMapper.toDomain(orderEntity);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.OPEN, result.getStatus());
        assertEquals(10, result.getDiscount());
        assertEquals(BigDecimal.valueOf(100.0), result.getTotal());
        assertEquals(items, result.getItems());
    }

    @Test
    void testToResponse() {
        when(orderMapper.toResponse(order)).thenReturn(new OrderResponse(orderId, OrderStatus.OPEN, new ArrayList<>(), 10, BigDecimal.valueOf(100.0)));

        OrderResponse response = orderMapper.toResponse(order);

        assertNotNull(response);
        assertEquals(order.getId(), response.id());
        assertEquals(order.getStatus(), response.status());
        assertEquals(order.getTotal(), response.total());
    }

    @Test
    void testToEntity() {
        when(orderMapper.toEntity(order)).thenReturn(orderEntity);

        OrderJpaEntity orderJpaEntity = orderMapper.toEntity(order);

        assertNotNull(orderJpaEntity);
        assertEquals(order.getId(), orderJpaEntity.getId());
        assertEquals(order.getDiscount(), orderJpaEntity.getDiscount());
        assertEquals(order.getStatus(), orderJpaEntity.getStatus());
        assertEquals(order.getTotal(), orderJpaEntity.getTotal());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testToDomainFromCreateOrderRequest() {
        Product product = new Product(UUID.randomUUID(), Instant.now(), null, "Sample Product", BigDecimal.valueOf(20.0), ProductType.PRODUCT, true);
        PreOrderItem preOrderItem = new PreOrderItem(product.getId(), 10);

        List<PreOrderItem> items = new ArrayList<>();
        items.add(preOrderItem);

        CreateOrderRequest request = new CreateOrderRequest(OrderStatus.OPEN, items, 10);

        Order result = orderMapper.toDomain(request);

        assertEquals(10, result.getDiscount());
        assertEquals(OrderStatus.OPEN, result.getStatus());
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void testToDomainFromUpdateOrderRequest() {
        UUID preOrderItemId = UUID.randomUUID();

        Product product = Product.newProduct("Sample Product", new BigDecimal("20"), ProductType.PRODUCT, true);
        OrderItem orderItem = OrderItem.newOrderItem(product, 10);

        List<PreOrderItem> preOrderItems = new ArrayList<>();
        preOrderItems.add(new PreOrderItem(preOrderItemId, 10));

        UpdateOrderRequest request = new UpdateOrderRequest(OrderStatus.OPEN, preOrderItems, 5);

        Order result = orderMapper.toDomain(request);

        assertNotNull(result);
        assertEquals(OrderStatus.OPEN, result.getStatus());
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
    }
}
