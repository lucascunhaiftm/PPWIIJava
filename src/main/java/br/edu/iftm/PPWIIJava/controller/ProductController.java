package br.edu.iftm.PPWIIJava.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  
}
