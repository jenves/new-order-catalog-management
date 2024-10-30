package br.com.order.catalog.management.mapper;

import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.domain.order.OrderItem;
import br.com.order.catalog.management.domain.order.PreOrder;
import br.com.order.catalog.management.domain.order.PreOrderItem;
import br.com.order.catalog.management.domain.order.OrderStatus;
import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreOrderMapperTest {

    private PreOrderMapper preOrderMapper;

    @BeforeEach
    void setUp() {
        preOrderMapper = Mappers.getMapper(PreOrderMapper.class);
    }

    @Test
    void testToPreOrderFromCreateOrderRequest() {
        UUID productId = UUID.randomUUID();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                OrderStatus.OPEN,
                List.of(new PreOrderItem(productId, 5)),
                10
        );

        PreOrder preOrder = preOrderMapper.toPreOrder(createOrderRequest);

        assertNotNull(preOrder, "PreOrder should not be null");
        assertEquals(createOrderRequest.status(), preOrder.status());
        assertEquals(createOrderRequest.discount(), preOrder.discount());
        assertEquals(1, preOrder.getItemIds().size(), "PreOrder should have one item");
    }

    @Test
    void testToDomainFromCreateOrderRequest() {
        UUID productId = UUID.randomUUID();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                OrderStatus.OPEN,
                List.of(new PreOrderItem(productId, 5)),
                15
        );

        PreOrder preOrder = preOrderMapper.toDomain(createOrderRequest);

        assertNotNull(preOrder, "PreOrder should not be null");
        assertEquals(OrderStatus.OPEN, preOrder.status(), "Status should be OPEN");
        assertEquals(15, preOrder.discount(), "Discount should match the request discount");
        assertEquals(1, preOrder.items().size(), "PreOrder should have one item");
        assertEquals(productId, preOrder.items().get(0).id(), "Product ID should match");
    }

    @Test
    void testToPreOrderItemFromOrderItem() {
        Product product = Product.newProduct("Test Product", new BigDecimal("50.0"), ProductType.PRODUCT, true);
        OrderItem orderItem = OrderItem.newOrderItem(product, 5);

        PreOrderItem preOrderItem = preOrderMapper.toPreOrderItem(orderItem);

        assertNotNull(preOrderItem, "PreOrderItem should not be null");
        assertEquals(orderItem.getAmount(), preOrderItem.amount(), "Quantity should match");
    }

    @Test
    void testToDomainFromUpdateOrderRequest() {
        UUID productId = UUID.randomUUID();
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(
                OrderStatus.OPEN,
                List.of(new PreOrderItem(productId, 3)),
                5
        );

        PreOrder preOrder = preOrderMapper.toDomain(updateOrderRequest);

        assertNotNull(preOrder, "PreOrder should not be null");
        assertEquals(OrderStatus.OPEN, preOrder.status(), "Status should be OPEN");
        assertEquals(5, preOrder.discount(), "Discount should match the request discount");
        assertEquals(1, preOrder.items().size(), "PreOrder should have one item");
        assertEquals(productId, preOrder.items().get(0).id(), "Product ID should match");
    }
}
