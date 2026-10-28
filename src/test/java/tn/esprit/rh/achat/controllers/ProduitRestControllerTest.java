package tn.esprit.rh.achat.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.services.IProduitService;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProduitRestController.class)
class ProduitRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IProduitService produitService;

    @Test
    void testGetProduits() throws Exception {
        Mockito.when(produitService.retrieveAllProduits()).thenReturn(Arrays.asList(new Produit()));

        mockMvc.perform(get("/produit/retrieve-all-produits")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
}