package br.com.order.catalog.management.repository;
import br.com.order.catalog.management.entity.ProductJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductJpaEntity, UUID>, JpaSpecificationExecutor<ProductJpaEntity> {

}
