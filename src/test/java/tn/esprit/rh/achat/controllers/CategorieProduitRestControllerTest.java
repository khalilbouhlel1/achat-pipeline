package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.services.ICategorieProduitService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategorieProduitRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategorieProduitService categorieProduitService;

    @Autowired
    private ObjectMapper objectMapper;

    CategorieProduitRestControllerTest() {
    }

    @Test
    public void testGetCategorieProduit() throws Exception {
        CategorieProduit cp1 = new CategorieProduit();
        cp1.setIdCategorieProduit(1L);
        cp1.setCodeCategorie("CAT01");
        cp1.setLibelleCategorie("Informatique");

        List<CategorieProduit> list = Arrays.asList(cp1);

        when(categorieProduitService.retrieveAllCategorieProduits()).thenReturn(list);

        mockMvc.perform(get("/categorieProduit/retrieve-all-categorieProduit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].codeCategorie").value("CAT01"))
                .andExpect(jsonPath("$[0].libelleCategorie").value("Informatique"));
    }

    @Test
    public void testRetrieveCategorieProduit() throws Exception {
        Long id = 1L;
        CategorieProduit cp = new CategorieProduit();
        cp.setIdCategorieProduit(id);
        cp.setCodeCategorie("CAT02");
        cp.setLibelleCategorie("Bureautique");

        when(categorieProduitService.retrieveCategorieProduit(id)).thenReturn(cp);

        mockMvc.perform(get("/categorieProduit/retrieve-categorieProduit/{categorieProduit-id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1L))
                .andExpect(jsonPath("$.codeCategorie").value("CAT02"));
    }

    @Test
    public void testAddCategorieProduit() throws Exception {
        CategorieProduit cp = new CategorieProduit();
        cp.setIdCategorieProduit(1L);
        cp.setCodeCategorie("CAT03");
        cp.setLibelleCategorie("Alimentaire");

        when(categorieProduitService.addCategorieProduit(any(CategorieProduit.class))).thenReturn(cp);

        mockMvc.perform(post("/categorieProduit/add-categorieProduit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1L))
                .andExpect(jsonPath("$.libelleCategorie").value("Alimentaire"));
    }

    @Test
    public void testModifyCategorieProduit() throws Exception {
        CategorieProduit cp = new CategorieProduit();
        cp.setIdCategorieProduit(1L);
        cp.setCodeCategorie("CAT03-MOD");
        cp.setLibelleCategorie("Alimentaire Modifié");

        when(categorieProduitService.updateCategorieProduit(any(CategorieProduit.class))).thenReturn(cp);

        mockMvc.perform(put("/categorieProduit/modify-categorieProduit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeCategorie").value("CAT03-MOD"))
                .andExpect(jsonPath("$.libelleCategorie").value("Alimentaire Modifié"));
    }

    @Test
    public void testRemoveCategorieProduit() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/categorieProduit/remove-categorieProduit/{categorieProduit-id}", id))
                .andExpect(status().isOk());
    }
}