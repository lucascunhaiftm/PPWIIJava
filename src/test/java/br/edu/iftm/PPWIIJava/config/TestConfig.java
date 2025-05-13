package br.edu.iftm.PPWIIJava.config;

import br.edu.iftm.PPWIIJava.service.ProductService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }

}