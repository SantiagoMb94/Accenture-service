package com.accenture.service.infrastructure.configuration;

import com.accenture.service.application.usecase.branch.AddBranchUseCase;
import com.accenture.service.application.usecase.branch.GetBranchesByFranchiseUseCase;
import com.accenture.service.application.usecase.branch.UpdateBranchNameUseCase;
import com.accenture.service.application.usecase.franchise.*;
import com.accenture.service.application.usecase.product.*;
import com.accenture.service.domain.port.out.BranchRepositoryPort;
import com.accenture.service.domain.port.out.FranchiseRepositoryPort;
import com.accenture.service.domain.port.out.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Inyección de Dependencias desacoplada.
 * Registra los casos de uso como Spring Beans sin contaminar
 * la capa de aplicación con anotaciones de framework.
 */
@Configuration
public class UseCaseConfig {

    // Casos de Uso de Franquicias
    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        return new CreateFranchiseUseCase(franchiseRepositoryPort);
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        return new UpdateFranchiseNameUseCase(franchiseRepositoryPort);
    }

    @Bean
    public GetAllFranchisesUseCase getAllFranchisesUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        return new GetAllFranchisesUseCase(franchiseRepositoryPort);
    }

    @Bean
    public GetFranchiseByIdUseCase getFranchiseByIdUseCase(FranchiseRepositoryPort franchiseRepositoryPort) {
        return new GetFranchiseByIdUseCase(franchiseRepositoryPort);
    }

    @Bean
    public GetTopProductPerBranchUseCase getTopProductPerBranchUseCase(
            FranchiseRepositoryPort franchiseRepositoryPort,
            BranchRepositoryPort branchRepositoryPort,
            ProductRepositoryPort productRepositoryPort) {
        return new GetTopProductPerBranchUseCase(franchiseRepositoryPort, branchRepositoryPort, productRepositoryPort);
    }

    // Casos de Uso de Sucursales
    @Bean
    public AddBranchUseCase addBranchUseCase(
            FranchiseRepositoryPort franchiseRepositoryPort,
            BranchRepositoryPort branchRepositoryPort) {
        return new AddBranchUseCase(franchiseRepositoryPort, branchRepositoryPort);
    }

    @Bean
    public UpdateBranchNameUseCase updateBranchNameUseCase(BranchRepositoryPort branchRepositoryPort) {
        return new UpdateBranchNameUseCase(branchRepositoryPort);
    }

    @Bean
    public GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase(
            FranchiseRepositoryPort franchiseRepositoryPort,
            BranchRepositoryPort branchRepositoryPort) {
        return new GetBranchesByFranchiseUseCase(franchiseRepositoryPort, branchRepositoryPort);
    }

    // Casos de Uso de Productos
    @Bean
    public AddProductUseCase addProductUseCase(
            BranchRepositoryPort branchRepositoryPort,
            ProductRepositoryPort productRepositoryPort) {
        return new AddProductUseCase(branchRepositoryPort, productRepositoryPort);
    }

    @Bean
    public UpdateProductNameUseCase updateProductNameUseCase(ProductRepositoryPort productRepositoryPort) {
        return new UpdateProductNameUseCase(productRepositoryPort);
    }

    @Bean
    public UpdateProductStockUseCase updateProductStockUseCase(ProductRepositoryPort productRepositoryPort) {
        return new UpdateProductStockUseCase(productRepositoryPort);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new DeleteProductUseCase(productRepositoryPort);
    }

    @Bean
    public GetProductsByBranchUseCase getProductsByBranchUseCase(
            BranchRepositoryPort branchRepositoryPort,
            ProductRepositoryPort productRepositoryPort) {
        return new GetProductsByBranchUseCase(branchRepositoryPort, productRepositoryPort);
    }
}
