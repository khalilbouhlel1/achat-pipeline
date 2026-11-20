package tn.esprit.rh.achat.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProduitRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetProduitsIntegration() throws Exception {
        // We fire a request at the fully-wired application
        mockMvc.perform(get("/produit/retrieve-all-produits")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               // The array might be empty if the DB just spun up, but the endpoint must return a valid JSON array
               .andExpect(jsonPath("$").isArray()); 
    }
}