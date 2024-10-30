package br.com.order.catalog.management.entity;

import br.com.order.catalog.management.domain.order.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "orders")
public class OrderJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull(message = "Status is required")
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItemJpaEntity> items;

  @Min(value = 0, message = "Discount must be greater than or equal to zero")
  private Integer discount;

  private BigDecimal total;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @NotNull(message = "Status is required") OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public List<OrderItemJpaEntity> getItems() {
    return items;
  }

  public void setItems(List<OrderItemJpaEntity> items) {
    this.items = items;
  }

  public @Min(value = 0, message = "Discount must be greater than or equal to zero") Integer getDiscount() {
    return discount;
  }

  public void setDiscount(
      @Min(value = 0, message = "Discount must be greater than or equal to zero") Integer discount) {
    this.discount = discount;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OrderJpaEntity that = (OrderJpaEntity) object;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
