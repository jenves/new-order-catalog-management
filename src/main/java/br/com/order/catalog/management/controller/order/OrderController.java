package br.com.order.catalog.management.controller.order;

import br.com.order.catalog.management.controller.order.model.CreateOrderRequest;
import br.com.order.catalog.management.controller.order.model.OrderResponse;
import br.com.order.catalog.management.controller.order.model.UpdateOrderRequest;
import br.com.order.catalog.management.controller.product.model.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/orders")
@Tag(name = "Orders")
public interface OrderController {

    @Operation(summary = "Get an order by its uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    EntityModel<OrderResponse> findById(@PathVariable("id") UUID id);

    @Operation(summary = "Get a paginated list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    Page<OrderResponse> findAll(@Parameter(description = "Pagination and sorting information. Use `sort=status,asc` for example, not an array.")Pageable pageable,
                                @Parameter(description = "Status of the Order, can be 'OPEN' or 'CLOSED'.") @RequestParam(required = false) String status);

    @Operation(summary = "Create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid body content",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Order contains inactive product",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping

    EntityModel<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request);

    @Operation(summary = "Update an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order updated",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateOrderRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid body content",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    EntityModel<OrderResponse> update(@PathVariable("id") UUID id,
                                      @Valid @RequestBody UpdateOrderRequest request);

    @Operation(summary = "Delete an order by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid UUID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable("id") UUID id);
}
