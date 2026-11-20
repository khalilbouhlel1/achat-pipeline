package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.services.IOperateurService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperateurController.class)
class OperateurControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IOperateurService operateurService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetOperateurs() throws Exception {
        Mockito.when(operateurService.retrieveAllOperateurs()).thenReturn(Arrays.asList(new Operateur()));

        mockMvc.perform(get("/operateur/retrieve-all-operateurs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveOperateur() throws Exception {
        Mockito.when(operateurService.retrieveOperateur(1L)).thenReturn(new Operateur());

        mockMvc.perform(get("/operateur/retrieve-operateur/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddOperateur() throws Exception {
        Operateur op = new Operateur();
        Mockito.when(operateurService.addOperateur(any(Operateur.class))).thenReturn(op);

        mockMvc.perform(post("/operateur/add-operateur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(op)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveOperateur() throws Exception {
        doNothing().when(operateurService).deleteOperateur(1L);

        mockMvc.perform(delete("/operateur/remove-operateur/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testModifyOperateur() throws Exception {
        Operateur op = new Operateur();
        Mockito.when(operateurService.updateOperateur(any(Operateur.class))).thenReturn(op);

        mockMvc.perform(put("/operateur/modify-operateur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(op)))
                .andExpect(status().isOk());
    }
}
