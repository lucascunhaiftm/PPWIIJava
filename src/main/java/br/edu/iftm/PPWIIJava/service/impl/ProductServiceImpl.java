package br.edu.iftm.PPWIIJava.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iftm.PPWIIJava.model.Product;
import br.edu.iftm.PPWIIJava.repository.ProductRepository;
import br.edu.iftm.PPWIIJava.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List <Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(Product product){
        this.productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) {
        Optional < Product > optional = productRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

}
