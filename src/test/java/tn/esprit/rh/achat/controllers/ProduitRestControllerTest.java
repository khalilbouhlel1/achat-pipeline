package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProduitRestController.class)
class ProduitRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IProduitService produitService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetProduits() throws Exception {
        Mockito.when(produitService.retrieveAllProduits()).thenReturn(Arrays.asList(new Produit()));

        mockMvc.perform(get("/produit/retrieve-all-produits")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveRayon() throws Exception {
        Mockito.when(produitService.retrieveProduit(1L)).thenReturn(new Produit());

        mockMvc.perform(get("/produit/retrieve-produit/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddProduit() throws Exception {
        Produit p = new Produit();
        Mockito.when(produitService.addProduit(any(Produit.class))).thenReturn(p);

        mockMvc.perform(post("/produit/add-produit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveProduit() throws Exception {
        doNothing().when(produitService).deleteProduit(1L);

        mockMvc.perform(delete("/produit/remove-produit/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testModifyProduit() throws Exception {
        Produit p = new Produit();
        Mockito.when(produitService.updateProduit(any(Produit.class))).thenReturn(p);

        mockMvc.perform(put("/produit/modify-produit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignProduitToStock() throws Exception {
        doNothing().when(produitService).assignProduitToStock(1L, 2L);

        mockMvc.perform(put("/produit/assignProduitToStock/1/2"))
                .andExpect(status().isOk());
    }
}
