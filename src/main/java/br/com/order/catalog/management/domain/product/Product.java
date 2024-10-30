package br.com.order.catalog.management.domain.product;

import br.com.order.catalog.management.domain.DomainEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Product extends DomainEntity {

  private String name;

  private BigDecimal price;

  private ProductType type;

  private Boolean active;

  public static Product newProduct(final String name, final BigDecimal price,
      final ProductType type,
      final Boolean active) {
    var id = UUID.randomUUID();
    var now = Instant.now();
    return new Product(id, now, null, name, price, type, active);
  }

  public Product(final UUID id, final Instant createdAt, final Instant updatedAt, final String name, final BigDecimal price, final ProductType type,
      final Boolean active) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.price = price;
    this.type = type;
    this.active = active;
  }

  public Product update(String newName, BigDecimal newPrice, ProductType newType,
      Boolean willBeActive) {
    this.name = newName;
    this.price = newPrice.setScale(2, RoundingMode.HALF_EVEN);
    this.type = newType;
    this.active = willBeActive;
    this.updatedAt = Instant.now();
    return this;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return Objects.nonNull(price) ? price.setScale(2, RoundingMode.HALF_EVEN) : null;
  }

  public ProductType getType() {
    return type;
  }

  public Boolean isActive() {
    return active;
  }


}
