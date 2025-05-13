package br.edu.iftm.PPWIIJava.product;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.iftm.PPWIIJava.model.Product;
import br.edu.iftm.PPWIIJava.repository.ProductRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Limpa o banco após cada teste
public class ProductIntegrationTest {
    

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(authorities = { "Admin" })
    void testSaveProductIntegration() throws Exception {

        Product productA = new Product();
        productA.setDescription("Descricao");
        productA.setName("Produto A");
        productA.setPrice(65.24f);
        productA.setStock(121);


        mockMvc.perform(post("/product/save")
                .with(csrf())
                .flashAttr("product", productA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product"));

        // Verifica no banco se foi salvo
        assertTrue(productRepository.findAll()
                .stream()
                .anyMatch(p -> "Produto A".equals(p.getName())));
        
    }
}
