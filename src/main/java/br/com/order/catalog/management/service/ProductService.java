package br.com.order.catalog.management.service;

import br.com.order.catalog.management.controller.product.model.UpdateProductRequest;
import br.com.order.catalog.management.domain.product.Product;

import java.util.Set;

import br.com.order.catalog.management.specification.filter.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

  Product getProductById(UUID id);

  Page<Product> getProducts(Pageable pageable, ProductFilter productFilter);

  Product saveProduct(Product product);

  Product updateProduct(UUID id, UpdateProductRequest request);

  void deleteProductById(UUID id);

  Set<Product> getProductsById(Set<UUID> items);
}
