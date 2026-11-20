package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Reglement;
import tn.esprit.rh.achat.services.IReglementService;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReglementRestController.class)
class ReglementRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IReglementService reglementService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testAddReglement() throws Exception {
        Reglement r = new Reglement();
        Mockito.when(reglementService.addReglement(any(Reglement.class))).thenReturn(r);

        mockMvc.perform(post("/reglement/add-reglement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReglement() throws Exception {
        Mockito.when(reglementService.retrieveAllReglements()).thenReturn(Arrays.asList(new Reglement()));

        mockMvc.perform(get("/reglement/retrieve-all-reglements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveReglement() throws Exception {
        Mockito.when(reglementService.retrieveReglement(1L)).thenReturn(new Reglement());

        mockMvc.perform(get("/reglement/retrieve-reglement/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveReglementByFacture() throws Exception {
        Mockito.when(reglementService.retrieveReglementByFacture(1L)).thenReturn(Arrays.asList(new Reglement()));

        mockMvc.perform(get("/reglement/retrieveReglementByFacture/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDate() throws Exception {
        Mockito.when(reglementService.getChiffreAffaireEntreDeuxDate(any(Date.class), any(Date.class))).thenReturn(5000.0f);

        mockMvc.perform(get("/reglement/getChiffreAffaireEntreDeuxDate/2026-01-01/2026-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDateException() throws Exception {
        doThrow(new RuntimeException("Error")).when(reglementService).getChiffreAffaireEntreDeuxDate(any(Date.class), any(Date.class));

        mockMvc.perform(get("/reglement/getChiffreAffaireEntreDeuxDate/2026-01-01/2026-12-31"))
                .andExpect(status().isOk());
    }
}
