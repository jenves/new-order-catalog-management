package br.com.order.catalog.management.controller.product;

import br.com.order.catalog.management.controller.product.model.CreateProductRequest;
import br.com.order.catalog.management.controller.product.model.ProductResponse;
import br.com.order.catalog.management.controller.product.model.UpdateProductRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import jakarta.validation.Valid;

import java.util.UUID;

@RequestMapping("/products")
@Tag(name = "products")
interface ProductController {

  @Operation(summary = "Get an product by its uuid")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the user",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ProductResponse.class))
          }),
      @ApiResponse(responseCode = "400", description = "Invalid uuid supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content
      ),
      @ApiResponse(responseCode = "500", description = "Internal server error",
          content = @Content
      )
  })
  @GetMapping("/{id}")
  EntityModel<ProductResponse> findById(@PathVariable("id") UUID id);

  @Operation(summary = "Get a paginated list of all products",
          description = "Retrieves a paginated list of products based on the provided filters. " +
                  "Filters include product name, active status, and type. " +
                  "The type parameter can be either 'PRODUCT' or 'SERVICE'. " +
                  "If no filters are provided, all products are returned.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = Page.class,
                                  description = "A paginated response containing a list of products."))),
          @ApiResponse(responseCode = "400", description = "Bad request due to invalid parameters", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping
  public Page<ProductResponse> findAll(
          @Parameter(description = "Pagination and sorting information. Use `sort=name,asc` for example, not an array.")
          Pageable pageable,
          @RequestParam(required = false) String name,
          @Parameter(description = "Type of the product, can be 'PRODUCT' or 'SERVICE'.")
          @RequestParam(required = false) String type,
          @RequestParam(required = false) Boolean active
  );





  @Operation(summary = "Create a new product")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Product created successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @PostMapping
  EntityModel<ProductResponse> create(@Valid @RequestBody CreateProductRequest request);

  @Operation(summary = "Update an existing product by UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product updated successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
      @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @PutMapping("/{id}")
  EntityModel<ProductResponse> update(@PathVariable("id") UUID id,
      @Valid @RequestBody UpdateProductRequest updatedProduct);

  @Operation(summary = "Delete a product by UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid UUID supplied", content = @Content),
      @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteById(@PathVariable("id") UUID id);
}
