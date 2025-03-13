package br.edu.iftm.PPWIIJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.iftm.PPWIIJava.model.Product;
import br.edu.iftm.PPWIIJava.service.ProductService;



@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public String index(Model model) {
        model.addAttribute("productsList", productService.getAllProducts());
        return "product/index";
    }

    @GetMapping("/product/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        return "product/create";
    }

    @PostMapping("/product/save")
    public String postMethodName(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/product";
    }

    @GetMapping("/product/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.productService.deleteProductById(id);
        return "redirect:/product";
    }
    
}
