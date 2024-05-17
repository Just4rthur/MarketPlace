package com.example.marketplace.service;

import com.example.marketplace.dto.*;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.marketplace.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    // Lägger till en ny produkt
    public void addProduct(Product2 product) {
        String oldName = product.getName();
        product.setName(oldName.toLowerCase());
        productRepository.save(product);
    }

    // Hämtar en produkt med på Product Name
    public Optional<Product2> getProductByName(ProductNameDTO dto) {
        return productRepository.findByName(dto.ProductName());
    }

    // Uppdaterar en befintlig produkt
    public boolean updateProduct(SearchProductDTO searchProductDTO) {
        Optional<Product2> optionalProduct = productRepository.findByName(searchProductDTO.userName());

        if (optionalProduct.isPresent()) {
            Product2 product = optionalProduct.get();
            product.setName(searchProductDTO.productName());
            product.setPrice(searchProductDTO.price());
            product.setYearOfProduction(searchProductDTO.yearOfProduction());
            product.setColor(searchProductDTO.color());
            product.setCondition(searchProductDTO.condition());
            productRepository.save(product);
            return true;
        } else {
            return false; // product not found
        }
    }

    // Tar bort en produkt
    public boolean deleteProduct(ProductNameDTO dto) {
        Optional<Product2> product = productRepository.findByName(dto.ProductName());
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        }
        return false;
    }

    // Hämta alla produkter
    public List<Product2> getAllProducts() {
        return productRepository.findAll();
    }

   // Hämta produkter inom ett specifikt prisintervall
   public List<Product2> getProductsByPriceRange(PriceRangeDTO priceRangeDTO) {
       return productRepository.findByPriceBetween(priceRangeDTO.minPrice(), priceRangeDTO.maxPrice());
   }
   // Hämta produkter baserat på skick
    public List<Product2> getProductsByCondition(ConditionDTO conditionDTO) {
        return productRepository.findByCondition(conditionDTO.condition());
    }

    // Hämta produkter baserat på namn
    public Optional<Product2> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    // Hämta produkter baserat på skick
    public List<Product2> getProductsByCondition(String condition) {
        return productRepository.findByCondition(condition);
    }
    public boolean changeStatesOfProductsToPending(List<ProductIdDTO> productIds, String username){
        try {
            for (ProductIdDTO id : productIds) {
                Optional<User> userOpt = userRepository.findByUsername(username);
                User user = userOpt.get();
                Optional<Product2> productOpt = productRepository.findById(id.id());
                Product2 product = productOpt.get();
                product.setState(ProductState.PENDING);
                product.setBuyer(user);
                productRepository.save(product);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeStateOfProductToAvailable(ProductIdDTO productIdDTO) {
        try {
            Optional<Product2> productOpt = productRepository.findById(productIdDTO.id());
            Product2 product = productOpt.get();
            product.setState(ProductState.AVAILABLE);
            product.setBuyer(null);
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeStateOfProductToAccept(ProductIdDTO productIdDTO) {
        try {
            Optional<Product2> productOpt = productRepository.findById(productIdDTO.id());
            Product2 product = productOpt.get();
            product.setState(ProductState.PURCHASE_CONFIRMED);
            product.setTimeWhenBought(LocalDateTime.now());
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Product2> getBoughtProducts() {
        ArrayList<Product2> boughtProducts = new ArrayList<>();
        for (Product2 boughtProduct : productRepository.findAll()) {
            if(boughtProduct.getState() == ProductState.PURCHASE_CONFIRMED) {
                boughtProducts.add(boughtProduct);
            }
        }
        return boughtProducts;
    }

    public List<Product2> getOffers(String username) {
        ArrayList<Product2> offers = new ArrayList<>();
        for (Product2 offer : productRepository.findAll()) {
            if(offer.getSeller().getUsername().equals(username) && offer.getState() == ProductState.PENDING) {
                offers.add(offer);
            }
        }
        return offers;
    }

    public List<Product2> getMyListings(String username) {
        ArrayList<Product2> listings = new ArrayList<>();
        for (Product2 listing : productRepository.findAll()) {
            if(listing.getSeller().getUsername().equals(username) && listing.getState() == ProductState.AVAILABLE) {
                listings.add(listing);
            }
        }
        return listings;
    }

    public List<Product2> getMySubmittedOffers(String username) {
        ArrayList<Product2> offers = new ArrayList<>();
        for (Product2 offer : productRepository.findAll()) {
            if(offer.getBuyer().getUsername().equals(username) && offer.getState() == ProductState.PENDING) {
                offers.add(offer);
            }
        }
        return offers;
    }
}

