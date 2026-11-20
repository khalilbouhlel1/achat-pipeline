package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.services.IFournisseurService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FournisseurRestController.class)
class FournisseurRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IFournisseurService fournisseurService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetFournisseurs() throws Exception {
        Mockito.when(fournisseurService.retrieveAllFournisseurs()).thenReturn(Arrays.asList(new Fournisseur()));

        mockMvc.perform(get("/fournisseur/retrieve-all-fournisseurs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveFournisseur() throws Exception {
        Mockito.when(fournisseurService.retrieveFournisseur(1L)).thenReturn(new Fournisseur());

        mockMvc.perform(get("/fournisseur/retrieve-fournisseur/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddFournisseur() throws Exception {
        Fournisseur f = new Fournisseur();
        Mockito.when(fournisseurService.addFournisseur(any(Fournisseur.class))).thenReturn(f);

        mockMvc.perform(post("/fournisseur/add-fournisseur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(f)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveFournisseur() throws Exception {
        doNothing().when(fournisseurService).deleteFournisseur(1L);

        mockMvc.perform(delete("/fournisseur/remove-fournisseur/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testModifyFournisseur() throws Exception {
        Fournisseur f = new Fournisseur();
        Mockito.when(fournisseurService.updateFournisseur(any(Fournisseur.class))).thenReturn(f);

        mockMvc.perform(put("/fournisseur/modify-fournisseur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(f)))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignSecteurActiviteToFournisseur() throws Exception {
        doNothing().when(fournisseurService).assignSecteurActiviteToFournisseur(1L, 2L);

        mockMvc.perform(put("/fournisseur/assignSecteurActiviteToFournisseur/1/2"))
                .andExpect(status().isOk());
    }
}
