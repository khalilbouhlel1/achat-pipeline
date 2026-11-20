package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.services.IFactureService;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FactureRestController.class)
class FactureRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IFactureService factureService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testRetrieveFacture() throws Exception {
        Mockito.when(factureService.retrieveFacture(1L)).thenReturn(new Facture());

        mockMvc.perform(get("/facture/retrieve-facture/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveAllFactures() throws Exception {
        Mockito.when(factureService.retrieveAllFactures()).thenReturn(Arrays.asList(new Facture()));

        mockMvc.perform(get("/facture/retrieve-all-factures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddFacture() throws Exception {
        Facture f = new Facture();
        Mockito.when(factureService.addFacture(any(Facture.class))).thenReturn(f);

        mockMvc.perform(post("/facture/add-facture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(f)))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelFacture() throws Exception {
        doNothing().when(factureService).cancelFacture(1L);

        mockMvc.perform(put("/facture/cancel-facture/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFactureByFournisseur() throws Exception {
        Mockito.when(factureService.getFacturesByFournisseur(1L)).thenReturn(Arrays.asList(new Facture()));

        mockMvc.perform(get("/facture/getFactureByFournisseur/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignOperateurToFacture() throws Exception {
        doNothing().when(factureService).assignOperateurToFacture(1L, 2L);

        mockMvc.perform(put("/facture/assignOperateurToFacture/1/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testPourcentageRecouvrement() throws Exception {
        Mockito.when(factureService.pourcentageRecouvrement(any(Date.class), any(Date.class))).thenReturn(80.5f);

        mockMvc.perform(get("/facture/pourcentageRecouvrement/2026-01-01/2026-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    void testPourcentageRecouvrementException() throws Exception {
        doThrow(new RuntimeException("Error")).when(factureService).pourcentageRecouvrement(any(Date.class), any(Date.class));

        mockMvc.perform(get("/facture/pourcentageRecouvrement/2026-01-01/2026-12-31"))
                .andExpect(status().isOk());
    }
}
