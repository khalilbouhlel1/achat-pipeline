package tn.esprit.rh.achat.controllers;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FournisseurRestController.class)
class FournisseurRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IFournisseurService fournisseurService;

    @Test
    void testGetFournisseurs() throws Exception {
        Mockito.when(fournisseurService.retrieveAllFournisseurs()).thenReturn(Arrays.asList(new Fournisseur()));

        mockMvc.perform(get("/fournisseur/retrieve-all-fournisseurs")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
}