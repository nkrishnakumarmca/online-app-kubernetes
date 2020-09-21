package com.kk.productservice.service;

import com.kk.productservice.errorhandler.exception.ProductExistsException;
import com.kk.productservice.errorhandler.exception.ProductNotFoundException;
import com.kk.productservice.model.Product;
import com.kk.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    public static final String NOT_AVAILABLE_IN_CACHE = "Retrieving products from database - category not available in cache";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_ALREADY_EXISTS = "Product with given id already exists";
    /**
     * **TODO**
     * Create a slf4j Logger for logging messages to standard output
     */
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * This method adds a new product to the database.
     * It throws ProductExistsException If the product already exists
     * <p>
     * * TODO **
     * The new product saved should be added to cache with name `products` and
     * the key for the cache should be the id of the product
     * The cached products belonging to the category of saved product should be removed from cache `categoryProducts`
     *
     * @param product
     * @throws ProductExistsException
     */
    @CachePut(value = "products", key = "#product.id")
    @CacheEvict(value = "categoryProducts", key = "#product.category")
    @Override
    public Product addNewProduct(Product product) throws ProductExistsException {
        // ** TODO ** log info message NOT_AVAILABLE_IN_CACHE defined above
        logger.info(NOT_AVAILABLE_IN_CACHE);
        if (productRepository.findById(product.getId()).isPresent()) {
            logger.error("Error while adding existing product. Throwing ProductExistsException");
            throw new ProductExistsException(PRODUCT_ALREADY_EXISTS);
        }
        return productRepository.save(product);
    }

    /**
     * This method gets an existing product from the database given a product id.
     * It throws ProductNotFoundException, If the product is not found in database
     * <p>
     * * TODO **
     * The product retrieved should be added to cache with name `products` with key as
     * id of the product
     */

    @Cacheable(value = "products", key = "#productId")
    @Override
    public Product fetchProductByProductId(String productId) throws ProductNotFoundException {
        // ** TODO ** log info message NOT_AVAILABLE_IN_CACHE defined above
        logger.info(NOT_AVAILABLE_IN_CACHE);
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

    /**
     * This method returns a list of products for a given product category.
     * <p>
     * * TODO **
     * The list of products retrieved should be added to cache with name `product-category` with key
     * as `category` of the products
     */
    @Cacheable(value = "categoryProducts", key = "#category")
    @Override
    public List<Product> fetchProductsByCategory(String category) {
        // **TODO** log below info message
        //  'Retrieving products from database - products of category not available in cache'
        logger.info("Retrieving products from database - products of category not available in cache");
        return productRepository.findByCategoryIgnoreCase(category);
    }

    /**
     * This method updates an existing product given a product id and product object. It throws
     * ProductNotFoundException, If the product already exists
     * <p>
     * * TODO **
     * The product updated should be added to cache with name `products` with key as
     * id of the product
     * The cached products belonging to the category of updated product should be
     * removed from cache `categoryProducts`
     */
    @CachePut(value = "products", key = "#product.id")
    @CacheEvict(value = "categoryProducts", key = "#product.category")
    @Override
    public Product updateProduct(Product product, String productId) throws ProductNotFoundException {
        // ** TODO ** log info message NOT_AVAILABLE_IN_CACHE defined above
        logger.info(NOT_AVAILABLE_IN_CACHE);
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        return productRepository.save(product);
    }

    /**
     * This method removes an existing product given a product id . It throws
     * ProductNotFoundException If the product is not found in database
     * <p>
     * * TODO **
     * The product updated should be added to cache with name `products` with key as
     * productId of the product
     */
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#productId"),
            @CacheEvict(value = "categoryProducts", key = "#result.category", condition = "#result != null")
    })
    @Override
    public Product removeProductById(String productId) throws ProductNotFoundException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> {
                    // ** TODO ** log error message here
                    logger.error("Product with id {} not found", productId);
                    return new ProductNotFoundException(PRODUCT_NOT_FOUND);

                });
        productRepository.delete(existingProduct);
        return existingProduct;
    }
}
