package org.mosaic.product_service.application.dtos;

import org.mosaic.product_service.domain.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateProductResponse {
	private String companyId;
	private String productUuid;
	private String productHubId;
	private String productName;
	private Long productPrice;
	private String productDescription;
	private Long stockQuantity;

	@Builder
	private UpdateProductResponse(String companyId, String productUuid,
		String productHubId, String productName, Long productPrice,
		String productDescription, Long stockQuantity) {
		this.companyId = companyId;
		this.productUuid = productUuid;
		this.productHubId = productHubId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.stockQuantity = stockQuantity;
	}

	public static UpdateProductResponse of(Product product){
		return UpdateProductResponse.builder()
			.productUuid(product.getProductUuid())
			.companyId(product.getCompanyId())
			.productHubId(product.getProductHubId())
			.productName(product.getProductName())
			.productPrice(product.getProductPrice())
			.productDescription(product.getProductDescription())
			.stockQuantity(product.getStockQuantity())
			.build();
	}

}
