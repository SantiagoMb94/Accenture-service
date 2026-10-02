package com.accenture.service.domain.model;

import java.util.Objects;

/**
 * Modelo de Dominio / Value Object: BranchTopProduct.
 * Representa el resultado analítico de la sucursal junto con su producto de mayor stock.
 */
public class BranchTopProduct {

    private final Long branchId;
    private final String branchName;
    private final Product topProduct;

    public BranchTopProduct(Long branchId, String branchName, Product topProduct) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.topProduct = topProduct;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public Product getTopProduct() {
        return topProduct;
    }

    public boolean hasProduct() {
        return topProduct != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchTopProduct that = (BranchTopProduct) o;
        return Objects.equals(branchId, that.branchId) &&
                Objects.equals(branchName, that.branchName) &&
                Objects.equals(topProduct, that.topProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId, branchName, topProduct);
    }

    @Override
    public String toString() {
        return "BranchTopProduct{" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", topProduct=" + topProduct +
                '}';
    }
}
