package br.com.order.catalog.management.domain.order;

import br.com.order.catalog.management.domain.DomainEntity;
import br.com.order.catalog.management.domain.product.ProductType;
import br.com.order.catalog.management.exceptions.InvalidItemExceptionException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Order extends DomainEntity {

  private OrderStatus status;

  private Set<OrderItem> items;

  private Integer discount;

  private BigDecimal total;

  private final static Integer MONEY_SCALE = 2;

  public static Order newOrder(final OrderStatus status, final Set<OrderItem> items,
      final Integer discount) {
    final var id = UUID.randomUUID();
    var now = Instant.now();
    return new Order(id, now, null, status, items, discount, null);
  }

  public Order(final UUID id, final Instant createdAt, final Instant updatedAt, final OrderStatus status, final Set<OrderItem> items,
      final Integer discount, BigDecimal total) {
    super(id, createdAt, updatedAt);
    this.status = status;
    this.items = items;
    this.discount = discount;
    this.total = Objects.isNull(total) ? calculateTotal() : total;
    validate();
  }

  public Order update(OrderStatus newStatus, Set<OrderItem> newOrderItems, Integer newDiscount) {
    this.status = newStatus;
    this.items = newOrderItems;
    this.discount = newDiscount;
    this.total = calculateTotal();
    this.updatedAt = Instant.now();
    validate();
    return this;
  }

  private void validate() {
    validateItems();
  }

  private void validateItems() {
    items.forEach(item -> {
      if (!item.isProductActivate()) {
        throw new InvalidItemExceptionException("Order contains inactive product");
      }
    });
  }

  private BigDecimal calculateTotal() {
    final var totalWithoutProducts = calculateTotalWithOutProducts();
    final var totalByProducts = calculateTotalByProducts();
    final var totalByProductsAfterDiscount =
        isToCalculateDiscount() ? calculateDiscount(totalByProducts)
            : totalByProducts;
    return totalWithoutProducts.add(totalByProductsAfterDiscount)
        .setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
  }

  private boolean isToCalculateDiscount() {
    return Objects.equals(status, OrderStatus.OPEN) && discount > 0;
  }

  private BigDecimal calculateTotalWithOutProducts() {
    return items.stream()
        .filter(item -> item.getProduct().isActive()
            && item.getProduct().getType() != ProductType.PRODUCT)
        .map(OrderItem::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calculateTotalByProducts() {
    return items.stream()
        .filter(item -> item.getProduct().isActive()
            && item.getProduct().getType() == ProductType.PRODUCT)
        .map(OrderItem::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calculateDiscount(BigDecimal totalProducts) {
    BigDecimal discountAmount = Objects.nonNull(discount) ?
        totalProducts.multiply(
            new BigDecimal(discount).divide(new BigDecimal(100), MONEY_SCALE,
                RoundingMode.HALF_EVEN)) :
        BigDecimal.ZERO;

    return totalProducts.subtract(discountAmount);
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Set<OrderItem> getItems() {
    return items;
  }

  public Integer getDiscount() {
    return discount;
  }

  public BigDecimal getTotal() {
    return total;
  }


}
