package com.accenture.service.domain.model;

import com.accenture.service.domain.exception.InvalidStockException;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio: Product (Producto).
 * Representa un producto perteneciente al inventario de una sucursal.
 * POJO puro libre de frameworks o dependencias externas.
 */
public class Product {

    private final Long id;
    private final Long branchId;
    private final String name;
    private final Integer stock;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Product(Long id, Long branchId, String name, Integer stock, Instant createdAt, Instant updatedAt) {
        if (branchId == null) {
            throw new IllegalArgumentException("El ID de la sucursal es obligatorio");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (stock == null || stock < 0) {
            throw new InvalidStockException("El stock no puede ser nulo ni negativo. Valor recibido: " + stock);
        }
        this.id = id;
        this.branchId = branchId;
        this.name = name.trim();
        this.stock = stock;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public Product(Long id, Long branchId, String name, Integer stock) {
        this(id, branchId, name, stock, Instant.now(), Instant.now());
    }

    public Product(Long branchId, String name, Integer stock) {
        this(null, branchId, name, stock, Instant.now(), Instant.now());
    }

    public Long getId() {
        return id;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getName() {
        return name;
    }

    public Integer getStock() {
        return stock;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Product withName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre del producto no puede estar vacío");
        }
        return new Product(this.id, this.branchId, newName.trim(), this.stock, this.createdAt, Instant.now());
    }

    public Product withStock(Integer newStock) {
        if (newStock == null || newStock < 0) {
            throw new InvalidStockException("El stock no puede ser nulo ni negativo. Valor recibido: " + newStock);
        }
        return new Product(this.id, this.branchId, this.name, newStock, this.createdAt, Instant.now());
    }

    public Product withId(Long newId) {
        return new Product(newId, this.branchId, this.name, this.stock, this.createdAt, this.updatedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(branchId, product.branchId) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, branchId, name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", branchId=" + branchId +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
