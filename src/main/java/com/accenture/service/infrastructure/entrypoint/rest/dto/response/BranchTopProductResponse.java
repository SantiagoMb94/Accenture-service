package com.accenture.service.infrastructure.entrypoint.rest.dto.response;

import com.accenture.service.domain.model.BranchTopProduct;

public class BranchTopProductResponse {

    private Long branchId;
    private String branchName;
    private ProductResponse topProduct;

    public BranchTopProductResponse() {
    }

    public BranchTopProductResponse(Long branchId, String branchName, ProductResponse topProduct) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.topProduct = topProduct;
    }

    public static BranchTopProductResponse fromDomain(BranchTopProduct model) {
        if (model == null) {
            return null;
        }
        return new BranchTopProductResponse(
                model.getBranchId(),
                model.getBranchName(),
                model.hasProduct() ? ProductResponse.fromDomain(model.getTopProduct()) : null
        );
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public ProductResponse getTopProduct() {
        return topProduct;
    }

    public void setTopProduct(ProductResponse topProduct) {
        this.topProduct = topProduct;
    }
}
