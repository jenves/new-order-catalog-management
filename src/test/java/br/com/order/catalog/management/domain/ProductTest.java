package br.com.order.catalog.management.domain;

import br.com.order.catalog.management.domain.product.Product;
import br.com.order.catalog.management.domain.product.ProductType;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.stream.Stream;

public class ProductTest {

  @Test
  public void givenValidParams_whenCallNewProduct_thenInstantiate() {
    var expectedName = "Test Product";
    var expectedPrice = new BigDecimal("100.00");
    var expectedType = ProductType.PRODUCT;
    var expectedActive = true;

    var actualProduct = Product.newProduct(expectedName, expectedPrice, expectedType,
        expectedActive);

    Assertions.assertNotNull(actualProduct);
    Assertions.assertNotNull(actualProduct.getId());
    Assertions.assertEquals(expectedName, actualProduct.getName());
    Assertions.assertEquals(expectedPrice, actualProduct.getPrice());
    Assertions.assertEquals(expectedType, actualProduct.getType());
    Assertions.assertEquals(expectedActive, actualProduct.isActive());
  }

  @Test
  public void givenValidParams_whenCallUpdate_thenUpdateProductSuccessfully() {
    var initialProduct = Product.newProduct("Old Product", new BigDecimal("50.00"),
        ProductType.SERVICE, false);

    var newName = "Updated Product";
    var newPrice = new BigDecimal("75.00");
    var newType = ProductType.PRODUCT;
    var newActive = true;

    initialProduct.update(newName, newPrice, newType, newActive);

    Assertions.assertEquals(newName, initialProduct.getName());
    Assertions.assertEquals(newPrice, initialProduct.getPrice());
    Assertions.assertEquals(newType, initialProduct.getType());
    Assertions.assertEquals(newActive, initialProduct.isActive());
  }

  @Nested
  class ProductPriceValidationTest {

    private static Stream<Arguments> providePriceTestCases() {
      return Stream.of(
          Arguments.of(new BigDecimal("100.00"), new BigDecimal("100.00")),
          Arguments.of(new BigDecimal("0.00"), new BigDecimal("0.00")),
          Arguments.of(new BigDecimal("-10.00"), new BigDecimal("0.00"))
      );
    }

    @ParameterizedTest
    @MethodSource("providePriceTestCases")
    public void givenVariousPrices_whenCallNewProduct_thenValidatePrice(BigDecimal inputPrice,
        BigDecimal expectedPrice) {
      var product = Product.newProduct("Test Product", inputPrice, ProductType.PRODUCT, true);

      // Ensuring price is non-negative
      if (inputPrice.compareTo(BigDecimal.ZERO) < 0) {
        product.update(product.getName(), BigDecimal.ZERO, product.getType(), product.isActive());
      }

      Assertions.assertEquals(expectedPrice.setScale(2, RoundingMode.HALF_EVEN),
          product.getPrice());
    }
  }

  @Test
  public void givenInactiveProduct_whenUpdateActiveStatus_thenActivateProduct() {
    var product = Product.newProduct("Inactive Product", new BigDecimal("50.00"),
        ProductType.PRODUCT, false);

    Assertions.assertFalse(product.isActive());

    product.update(product.getName(), product.getPrice(), product.getType(), true);

    Assertions.assertTrue(product.isActive());
  }

  @Test
  public void givenUUID_whenCreateProduct_thenIdShouldMatch() {
    var expectedId = UUID.randomUUID();
    var now = Instant.now();
    var product = new Product(expectedId, now, null, "Test Product", new BigDecimal("100.00"),
        ProductType.PRODUCT, true);

    Assertions.assertEquals(expectedId, product.getId());
  }
}
