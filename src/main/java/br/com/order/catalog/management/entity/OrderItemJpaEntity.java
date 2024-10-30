package br.com.order.catalog.management.entity;

import br.com.order.catalog.management.domain.order.OrderItem;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItemJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private OrderJpaEntity order;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private ProductJpaEntity product;

  @Positive(message = "Amount must be positive")
  private Integer amount;

  public OrderItemJpaEntity() {
  }

  public OrderItemJpaEntity(UUID id, OrderJpaEntity order, ProductJpaEntity product,
      Integer amount) {
    this.id = id;
    this.order = order;
    this.product = product;
    this.amount = amount;
  }

  public UUID getId() {
    return id;
  }

  public OrderJpaEntity getOrder() {
    return order;
  }

  public ProductJpaEntity getProduct() {
    return product;
  }

  public @Positive(message = "Amount must be positive") Integer getAmount() {
    return amount;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setOrder(OrderJpaEntity order) {
    this.order = order;
  }

  public void setProduct(ProductJpaEntity product) {
    this.product = product;
  }

  public void setAmount(
      @Positive(message = "Amount must be positive") Integer amount) {
    this.amount = amount;
  }

  public static OrderItemJpaEntity from(OrderItem orderItem, OrderJpaEntity orderJpaEntity) {
    return new OrderItemJpaEntity(orderItem.getId(), orderJpaEntity,
        ProductJpaEntity.from(orderItem.getProduct()),
        orderItem.getAmount());
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OrderItemJpaEntity that = (OrderItemJpaEntity) object;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
