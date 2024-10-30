package br.com.order.catalog.management.specification;

import br.com.order.catalog.management.entity.OrderJpaEntity;
import br.com.order.catalog.management.specification.filter.OrderFilter;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<OrderJpaEntity> withStatus(OrderFilter orderFilter) {
        return (root, query, criteriaBuilder) -> {
            if (orderFilter.getStatus() != null) {
                return criteriaBuilder.equal(root.get("status"), orderFilter.getStatus().toUpperCase());
            }
            return criteriaBuilder.conjunction();
        };
    }
}
