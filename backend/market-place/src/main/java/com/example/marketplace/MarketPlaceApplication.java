package com.example.marketplace;

import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.DatabaseUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class
		//HibernateJpaAutoConfiguration.class
})
@EnableMongoRepositories
public class MarketPlaceApplication implements CommandLineRunner {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DatabaseUpdateService databaseUpdateService;

	public static void main(String[] args) {
		SpringApplication.run(MarketPlaceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Hello world!");
	}

	//CREATE
/*	void createProducts() {
		System.out.println("Creating products");

		productRepository.save(new Product("1", "Laptop", "Dell Laptop", "Electronics", 54.0, 10));
		productRepository.save(new Product("2", "Mobile", "Samsung Mobile", "Electronics", 40.0, 20));
		productRepository.save(new Product("3", "Shirt", "Peter England Shirt", "Clothing", 15.0, 30));
		productRepository.save(new Product("4", "Trouser", "Levis Trouser", "Clothing", 20.0, 40));
		productRepository.save(new Product("5", "Shoe", "Nike Shoe", "Footwear", 25.0, 50));

		System.out.println("Products created");
	}*/

	//READ
	public void showAllProducts() {
		productRepository.findAll().forEach(product -> System.out.println(product));
	}

	public void showProductByName(String name) {
		System.out.println("Product to be searched --> " + name);
		System.out.println(productRepository.findByName(name));
	}

	/*public void showProductByCategory(String category) {
		System.out.println("Category to be searched --> " + category);
		List<Product> list = productRepository.findAll(category);

		list.forEach(product -> System.out.println("Name " + product.getName() + " Quantity : " + product.getQuantity()));
	}*/

	//DELETE
	public void deleteProduct(String id){
		System.out.println("Product to be deleted --> " + id);
		productRepository.deleteById(id);
		System.out.println("Product deleted");
	}


}
