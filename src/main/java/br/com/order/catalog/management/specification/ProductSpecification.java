package br.com.order.catalog.management.specification;

import br.com.order.catalog.management.entity.ProductJpaEntity;
import br.com.order.catalog.management.specification.filter.ProductFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<ProductJpaEntity> withFilters(ProductFilter productFilter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (productFilter.getName() != null && !productFilter.getName().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + productFilter.getName().toLowerCase() + "%"));
            }

            if (productFilter.getActive() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("active"), productFilter.getActive()));
            }

            if (productFilter.getType() != null && !productFilter.getType().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("type"), productFilter.getType().toUpperCase()));
            }

            return predicate;
        };
    }
}
