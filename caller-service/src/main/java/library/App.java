package library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import library.controllers.CallerController;
import library.services.BookService;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false)
public class App {

  // Case insensitive: could also use: http://accounts-service
  public static final String BOOKS_SERVICE_URL = "BOOK-SERVICE/api/v1";

  @LoadBalanced // Make sure to create the load-balanced template
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /**
   * Account service calls microservice internally using provided URL.
   */
  @Bean
  public BookService bookService() {
    return new BookService(BOOKS_SERVICE_URL);
  }

  @Bean
  public CallerController accountsController() {
    return new CallerController(bookService());
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
