package br.edu.iftm.PPWIIJava.product;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.iftm.PPWIIJava.config.TestConfig;
import br.edu.iftm.PPWIIJava.controller.ProductController;
import br.edu.iftm.PPWIIJava.model.Product;
import br.edu.iftm.PPWIIJava.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(TestConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @AfterEach
    void resetMocks() {
        reset(productService);
    }

    private List<Product> testCreateProductList(){
        Product productB = new Product();
        productB.setId(1L);
        productB.setDescription("Descricao");
        productB.setName("Produto B");
        productB.setPrice(65.24f);
        productB.setStock(121);

        return List.of(productB);
    }

    @Test
    @DisplayName("GET /product - Listar produtos na tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
         mockMvc.perform(get("/product"))
            .andExpect(status().isUnauthorized()); // Correção aqui
    }

    @Test
    @WithMockUser
    @DisplayName("GET /product - Listar produtos na tela index com usuário logado")
    void testIndexAuthenticatedUser() throws Exception {
        when(productService.getAllProducts()).thenReturn(testCreateProductList());

        mockMvc.perform(get("/product"))
               .andExpect(status().isOk())
               .andExpect(view().name("product/index"))
               .andExpect(model().attributeExists("productsList"))
               .andExpect(content().string(containsString("Listagem de Produto")))
               .andExpect(content().string(containsString("Produto B")));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("GET /product/create - Exibe formulário de criação")
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/create"))
                .andExpect(model().attributeExists("product"))
                .andExpect(content().string(containsString("Cadastrar Produto")));
    }

    @Test
    @WithMockUser(username = "aluno2@iftm.edu.br", authorities = { "Manager" })
    @DisplayName("GET /product - Verificar o link de cadastrar para um usuario não admin logado")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(productService.getAllProducts()).thenReturn(testCreateProductList());
       // Obter o HTML da página renderizada pelo controlador
       mockMvc.perform(get("/product/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("product/create"))
            .andExpect(model().attributeExists("product"))
            .andExpect(content().string(not(containsString("<a class=\"dropdown-item\" href=\"/product/create\">Cadastrar</a>"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /product/save - Falha na validação e retorna para o formulário")
    void testSaveProductValidationError() throws Exception {
        Product product = new Product(); // Produto sem nome, o que causará erro de validação

        mockMvc.perform(post("/product/save")
                        .with(csrf())
                        .flashAttr("product", product))
                .andExpect(status().isOk())
                .andExpect(view().name("product/create"))
                .andExpect(model().attributeHasErrors("product"));

        verify(productService, never()).saveProduct(any(Product.class));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("POST /product/save - Produto válido é salvo com sucesso")
    void testSaveValidProduct() throws Exception {
        Product product = new Product();
        product.setName("Novo Produto");
        product.setDescription("Descrição");
        product.setPrice(100f);
        product.setStock(10);

        mockMvc.perform(post("/product/save")
                        .with(csrf())
                        .flashAttr("product", product))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/product"));

        verify(productService).saveProduct(any(Product.class));
    }

}
