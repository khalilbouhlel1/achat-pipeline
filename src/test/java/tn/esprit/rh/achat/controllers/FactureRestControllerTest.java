package tn.esprit.rh.achat.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.services.IFactureService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FactureRestController.class)
class FactureRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IFactureService factureService;

    @Test
    void testRetrieveFacture() throws Exception {
        Mockito.when(factureService.retrieveFacture(1L)).thenReturn(new Facture());

        mockMvc.perform(get("/facture/retrieve-facture/1")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
}