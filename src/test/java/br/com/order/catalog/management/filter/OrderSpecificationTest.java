package br.com.order.catalog.management.filter;

import br.com.order.catalog.management.entity.OrderJpaEntity;
import br.com.order.catalog.management.specification.OrderSpecification;
import br.com.order.catalog.management.specification.filter.OrderFilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrderSpecificationTest {

    @Mock
    private Root<OrderJpaEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Spy
    @InjectMocks
    private OrderSpecification orderSpecification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateWithStatusFilter() {
        OrderFilter filter = new OrderFilter();
        filter.setStatus("PENDING");

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.equal(root.get("status"), "PENDING")).thenReturn(predicate);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        Specification<OrderJpaEntity> specification = OrderSpecification.withStatus(filter);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result, "Predicate for status filter should not be null");
        verify(criteriaBuilder).equal(root.get("status"), "PENDING");
    }
}
