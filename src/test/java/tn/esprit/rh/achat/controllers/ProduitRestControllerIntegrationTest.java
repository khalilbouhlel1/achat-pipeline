package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.services.IProduitService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProduitRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // On injecte le VRAI service
    @Autowired
    private IProduitService produitService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProduitFlowReal() throws Exception {
        // 1. Préparation d'une donnée réelle en base via le service
        Produit p = new Produit();
        p.setCodeProduit("PROD-INT");
        p.setLibelleProduit("PC Portable Intégration");
        Produit saved = produitService.addProduit(p);

        // 2. Test de récupération de tous les produits (GET) - URL corrigée avec le 's'
        mockMvc.perform(get("/produit/retrieve-all-produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNumber());

        // 3. Test de récupération par ID (GET)
        mockMvc.perform(get("/produit/retrieve-produit/{produit-id}", saved.getIdProduit()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduit").value(saved.getIdProduit()))
                .andExpect(jsonPath("$.codeProduit").value("PROD-INT"));

        // 4. Test d'ajout d'un produit via le contrôleur (POST)
        Produit newProduit = new Produit();
        newProduit.setCodeProduit("PROD-POST");
        newProduit.setLibelleProduit("Imprimante Test");

        mockMvc.perform(post("/produit/add-produit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeProduit").value("PROD-POST"));

        // 5. Test de suppression via le contrôleur (DELETE)
        mockMvc.perform(delete("/produit/remove-produit/{produit-id}", saved.getIdProduit()))
                .andExpect(status().isOk());
    }
}