-- ============================================================================
-- SCRIPT DE INICIALIZACIÓN DE BASE DE DATOS POSTGRESQL
-- Sistema de Franquicias, Sucursales y Productos
-- ============================================================================

-- 1. Tabla de Franquicias
CREATE TABLE IF NOT EXISTS franchises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Sucursales
CREATE TABLE IF NOT EXISTS branches (
    id BIGSERIAL PRIMARY KEY,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_branch_franchise FOREIGN KEY (franchise_id) REFERENCES franchises (id) ON DELETE CASCADE,
    CONSTRAINT uq_branch_franchise_name UNIQUE (franchise_id, name)
);

-- 3. Tabla de Productos
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_branch FOREIGN KEY (branch_id) REFERENCES branches (id) ON DELETE CASCADE,
    CONSTRAINT uq_product_branch_name UNIQUE (branch_id, name)
);

-- 4. Índices para Optimización de Consultas Relacionales y Analíticas
CREATE INDEX IF NOT EXISTS idx_branches_franchise_id ON branches (franchise_id);
CREATE INDEX IF NOT EXISTS idx_products_branch_id ON products (branch_id);
CREATE INDEX IF NOT EXISTS idx_products_branch_stock ON products (branch_id, stock DESC);
