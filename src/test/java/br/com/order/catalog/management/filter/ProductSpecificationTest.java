package br.com.order.catalog.management.filter;

import br.com.order.catalog.management.entity.ProductJpaEntity;
import br.com.order.catalog.management.specification.ProductSpecification;
import br.com.order.catalog.management.specification.filter.ProductFilter;
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

import static org.mockito.Mockito.*;

class ProductSpecificationTest {

    @Mock
    private Root<ProductJpaEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Spy
    @InjectMocks
    private ProductSpecification productSpecification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateWithNameFilter() {
        ProductFilter filter = new ProductFilter();
        filter.setName("TestProduct");

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.lower(root.get("name"))).thenReturn(mock(jakarta.persistence.criteria.Expression.class));
        when(criteriaBuilder.like(any(), eq("%testproduct%"))).thenReturn(predicate);

        Specification<ProductJpaEntity> specification = ProductSpecification.withFilters(filter);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(any(), eq("%testproduct%"));
    }

    @Test
    void shouldReturnPredicateWithActiveFilter() {
        ProductFilter filter = new ProductFilter();
        filter.setActive(true);

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.equal(root.get("active"), true)).thenReturn(predicate);

        Specification<ProductJpaEntity> specification = ProductSpecification.withFilters(filter);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("active"), true);
    }

    @Test
    void shouldReturnPredicateWithTypeFilter() {
        ProductFilter filter = new ProductFilter();
        filter.setType("PRODUCT");

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(predicate);
        when(criteriaBuilder.equal(root.get("type"), "PRODUCT")).thenReturn(predicate);

        Specification<ProductJpaEntity> specification = ProductSpecification.withFilters(filter);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("type"), "PRODUCT");
    }
}
