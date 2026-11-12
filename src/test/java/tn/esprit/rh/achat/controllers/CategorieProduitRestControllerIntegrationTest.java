package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.services.ICategorieProduitService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategorieProduitRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Injection du VRAI service pour interagir directement avec la base de données
    @Autowired
    private ICategorieProduitService categorieProduitService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCategorieProduit() throws Exception {
        // Préparation d'une donnée réelle en base
        CategorieProduit cp1 = new CategorieProduit();
        cp1.setCodeCategorie("CAT01");
        cp1.setLibelleCategorie("Informatique");
        categorieProduitService.addCategorieProduit(cp1);

        mockMvc.perform(get("/categorieProduit/retrieve-all-categorieProduit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNumber());
    }

    @Test
    public void testRetrieveCategorieProduit() throws Exception {
        // Préparation d'une donnée réelle en base
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT02");
        cp.setLibelleCategorie("Bureautique");
        CategorieProduit saved = categorieProduitService.addCategorieProduit(cp);

        mockMvc.perform(get("/categorieProduit/retrieve-categorieProduit/{categorieProduit-id}", saved.getIdCategorieProduit()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(saved.getIdCategorieProduit()))
                .andExpect(jsonPath("$.codeCategorie").value("CAT02"));
    }

    @Test
    public void testAddCategorieProduit() throws Exception {
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT03");
        cp.setLibelleCategorie("Alimentaire");

        mockMvc.perform(post("/categorieProduit/add-categorieProduit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelleCategorie").value("Alimentaire"));
    }

    @Test
    public void testModifyCategorieProduit() throws Exception {
        // Préparation d'une donnée réelle en base
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT03");
        cp.setLibelleCategorie("Alimentaire");
        CategorieProduit saved = categorieProduitService.addCategorieProduit(cp);

        saved.setCodeCategorie("CAT03-MOD");
        saved.setLibelleCategorie("Alimentaire Modifié");

        mockMvc.perform(put("/categorieProduit/modify-categorieProduit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeCategorie").value("CAT03-MOD"))
                .andExpect(jsonPath("$.libelleCategorie").value("Alimentaire Modifié"));
    }

    @Test
    public void testRemoveCategorieProduit() throws Exception {
        // Préparation d'une donnée réelle en base
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT04");
        cp.setLibelleCategorie("Nettoyage");
        CategorieProduit saved = categorieProduitService.addCategorieProduit(cp);

        mockMvc.perform(delete("/categorieProduit/remove-categorieProduit/{categorieProduit-id}", saved.getIdCategorieProduit()))
                .andExpect(status().isOk());
    }
}