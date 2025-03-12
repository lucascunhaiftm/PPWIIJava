package br.edu.iftm.PPWIIJava.service;

import java.util.List;

import br.edu.iftm.PPWIIJava.model.Product;

public interface ProductService {

    List <Product> getAllProducts();
    void saveProduct(Product product);
    Product getProductById(long id);
    void deleteProductById(long id);
}
