package br.com.order.catalog.management.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class DomainEntity {

  private final UUID id;

  private final Instant createdAt;

  protected Instant updatedAt;

  protected DomainEntity(UUID id, Instant createdAt, Instant updatedAt) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return Objects.requireNonNull(id, "'id' should not be null");
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }


  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DomainEntity that = (DomainEntity) object;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
