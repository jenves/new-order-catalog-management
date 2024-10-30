package br.com.order.catalog.management.entity;

import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotBlank(message = "Product name is required")
  private String name;

  @NotNull(message = "Product price is required")
  @Positive(message = "Product price must be positive")
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Product type is required")
  @Column(nullable = false)
  private ProductType type;

  @NotNull(message = "Product active is required")
  private Boolean active;

  public ProductJpaEntity() {
  }

  public ProductJpaEntity(UUID id, String name, BigDecimal price, ProductType type,
      Boolean active) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.type = type;
    this.active = active;
  }

  public static ProductJpaEntity from(Product product) {
    return new ProductJpaEntity(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getType(),
        product.isActive()
    );
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public ProductType getType() {
    return type;
  }

  public void setType(ProductType type) {
    this.type = type;
  }

  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ProductJpaEntity that = (ProductJpaEntity) object;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
