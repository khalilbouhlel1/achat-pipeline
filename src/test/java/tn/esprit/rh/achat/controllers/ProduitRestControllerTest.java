package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.services.IProduitService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProduitRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProduitService produitService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetProduits() throws Exception {
        Produit p1 = new Produit();
        p1.setIdProduit(1L);
        p1.setCodeProduit("PROD01");
        p1.setLibelleProduit("PC Portable");

        List<Produit> list = Arrays.asList(p1);

        when(produitService.retrieveAllProduits()).thenReturn(list);

        mockMvc.perform(get("/produit/retrieve-all-produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].codeProduit").value("PROD01"))
                .andExpect(jsonPath("$[0].libelleProduit").value("PC Portable"));
    }

    @Test
    public void testRetrieveProduit() throws Exception {
        Long id = 1L;
        Produit p = new Produit();
        p.setIdProduit(id);
        p.setCodeProduit("PROD02");
        p.setLibelleProduit("Imprimante");

        when(produitService.retrieveProduit(id)).thenReturn(p);

        mockMvc.perform(get("/produit/retrieve-produit/{produit-id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduit").value(1L))
                .andExpect(jsonPath("$.codeProduit").value("PROD02"));
    }

    @Test
    public void testAddProduit() throws Exception {
        Produit p = new Produit();
        p.setIdProduit(1L);
        p.setCodeProduit("PROD03");
        p.setLibelleProduit("Clavier");

        when(produitService.addProduit(any(Produit.class))).thenReturn(p);

        mockMvc.perform(post("/produit/add-produit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduit").value(1L))
                .andExpect(jsonPath("$.libelleProduit").value("Clavier"));
    }

    @Test
    public void testModifyProduit() throws Exception {
        Produit p = new Produit();
        p.setIdProduit(1L);
        p.setCodeProduit("PROD03-MOD");
        p.setLibelleProduit("Clavier Modifié");

        when(produitService.updateProduit(any(Produit.class))).thenReturn(p);

        mockMvc.perform(put("/produit/modify-produit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeProduit").value("PROD03-MOD"))
                .andExpect(jsonPath("$.libelleProduit").value("Clavier Modifié"));
    }

    @Test
    public void testRemoveProduit() throws Exception {
        Long id = 1L;

        doNothing().when(produitService).deleteProduit(id);

        mockMvc.perform(delete("/produit/remove-produit/{produit-id}", id))
                .andExpect(status().isOk());
    }
}