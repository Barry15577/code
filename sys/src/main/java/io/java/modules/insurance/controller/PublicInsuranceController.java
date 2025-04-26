package io.java.modules.insurance.controller;

import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.modules.insurance.entity.InsuranceOrderEntity;
import io.java.modules.insurance.entity.InsuranceProductEntity;
import io.java.modules.insurance.service.InsuranceOrderService;
import io.java.modules.insurance.service.InsuranceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Public Insurance API Controller
 * This controller provides endpoints for public access to insurance product information
 * and order submission without requiring login.
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/api/insurance")
public class PublicInsuranceController {
    @Autowired
    private InsuranceProductService insuranceProductService;
    
    @Autowired
    private InsuranceOrderService insuranceOrderService;

    /**
     * Get a list of active insurance products for display on homepage
     */
    @GetMapping("/products")
    public R listActiveProducts(@RequestParam Map<String, Object> params) {
        // Only show active products
        params.put("status", 1);
        
        PageUtils page = insuranceProductService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    /**
     * Get featured products (limit to 4 products for homepage display)
     */
    @GetMapping("/featured-products")
    public R getFeaturedProducts() {
        Map<String, Object> params = new HashMap<>();
        params.put("status", 1);
        params.put("page", "1");
        params.put("limit", "4");
        
        PageUtils page = insuranceProductService.queryPage(params);
        
        return R.ok().put("featuredProducts", page.getList());
    }
    
    /**
     * Get products by type
     */
    @GetMapping("/products-by-type")
    public R getProductsByType(@RequestParam String productType) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", 1);
        params.put("productType", productType);
        params.put("limit", "10");
        
        PageUtils page = insuranceProductService.queryPage(params);
        
        return R.ok().put("products", page.getList());
    }
    
    /**
     * Get product details
     */
    @GetMapping("/product/{productId}")
    public R getProductDetails(@PathVariable("productId") Long productId) {
        InsuranceProductEntity product = insuranceProductService.getProductById(productId);
        
        // Only allow access to active products
        if (product != null && product.getStatus() == 1) {
            return R.ok().put("product", product);
        }
        
        return R.error("Product not found or not active");
    }
    
    /**
     * Submit an insurance appointment order
     */
    @PostMapping("/submit-order")
    public R submitOrder(@RequestBody InsuranceOrderEntity order) {
        ValidatorUtils.validateEntity(order);
        
        // Get the product to validate it exists and is active
        InsuranceProductEntity product = insuranceProductService.getProductById(order.getProductId());
        if (product == null || product.getStatus() != 1) {
            return R.error("Product not found or not active");
        }
        
        // Set the product name from the product entity
        order.setProductName(product.getProductName());
        
        // Save the order
        insuranceOrderService.saveOrder(order);
        
        return R.ok().put("orderId", order.getOrderId());
    }
    
    /**
     * Get a list of product types
     */
    @GetMapping("/product-types")
    public R getProductTypes() {
        Map<String, Object> params = new HashMap<>();
        params.put("status", 1);
        params.put("limit", "100");
        
        PageUtils page = insuranceProductService.queryPage(params);
        
        @SuppressWarnings("unchecked")
        List<InsuranceProductEntity> products = (List<InsuranceProductEntity>) page.getList();
        
        // Extract unique product types
        List<String> productTypes = products.stream()
                .map(InsuranceProductEntity::getProductType)
                .distinct()
                .collect(Collectors.toList());
        
        return R.ok().put("productTypes", productTypes);
    }
} 