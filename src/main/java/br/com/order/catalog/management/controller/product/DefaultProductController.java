package br.com.order.catalog.management.controller.product;

import br.com.order.catalog.management.controller.product.model.CreateProductRequest;
import br.com.order.catalog.management.controller.product.model.ProductResponse;
import br.com.order.catalog.management.controller.product.model.UpdateProductRequest;
import br.com.order.catalog.management.mapper.ProductMapper;
import br.com.order.catalog.management.service.ProductService;
import java.util.UUID;

import br.com.order.catalog.management.specification.filter.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class DefaultProductController implements ProductController {

  private final ProductService productService;

  private final ProductMapper productMapper;

  public DefaultProductController(ProductService productService, ProductMapper productMapper) {
    this.productService = productService;
    this.productMapper = productMapper;
  }

  public EntityModel<ProductResponse> findById(UUID id) {
    final var product = productService.getProductById(id);
    final var resource = EntityModel.of(productMapper.toResponse(product));

    Link selfLink = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(DefaultProductController.class).findById(id)).withSelfRel();
    resource.add(selfLink);

    resource.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(DefaultProductController.class).findAll(null,null,null,null))
            .withRel("products"));
    resource.add(WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(DefaultProductController.class).update(id,
                new UpdateProductRequest(product.getName(), product.getPrice(), product.getType(),
                    product.isActive())))
        .withRel("update"));
    resource.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(DefaultProductController.class).deleteById(id))
            .withRel("delete"));

    return resource;
  }

  public Page<ProductResponse> findAll(Pageable pageable, String name, String type, Boolean active) {
    ProductFilter productFilter = new ProductFilter(name, type, active);
    final var productsPage = productService.getProducts(pageable, productFilter);

    return productsPage.map(productMapper::toResponse);
  }

  public EntityModel<ProductResponse> create(CreateProductRequest request) {
    final var product = productService.saveProduct(productMapper.toDomain(request));
    final var resource = EntityModel.of(productMapper.toResponse(product));

    Link selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(DefaultProductController.class).create(request))
        .withSelfRel();
    resource.add(selfLink);

    return resource;
  }

  public EntityModel<ProductResponse> update(UUID id, UpdateProductRequest updatedProduct) {
    final var product = productService.updateProduct(id, updatedProduct);
    final var resource = EntityModel.of(productMapper.toResponse(product));

    Link selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(DefaultProductController.class).update(id, updatedProduct))
        .withSelfRel();
    resource.add(selfLink);

    return resource;
  }

  public ResponseEntity<Void> deleteById(UUID id) {
    productService.deleteProductById(id);
    return ResponseEntity.noContent().build();
  }
}
