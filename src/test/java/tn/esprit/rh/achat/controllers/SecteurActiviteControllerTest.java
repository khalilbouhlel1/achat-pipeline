package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.SecteurActivite;
import tn.esprit.rh.achat.services.ISecteurActiviteService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecteurActiviteController.class)
class SecteurActiviteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ISecteurActiviteService secteurActiviteService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetSecteurActivite() throws Exception {
        Mockito.when(secteurActiviteService.retrieveAllSecteurActivite()).thenReturn(Arrays.asList(new SecteurActivite()));

        mockMvc.perform(get("/secteurActivite/retrieve-all-secteurActivite")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveSecteurActivite() throws Exception {
        Mockito.when(secteurActiviteService.retrieveSecteurActivite(1L)).thenReturn(new SecteurActivite());

        mockMvc.perform(get("/secteurActivite/retrieve-secteurActivite/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddSecteurActivite() throws Exception {
        SecteurActivite sa = new SecteurActivite();
        Mockito.when(secteurActiviteService.addSecteurActivite(any(SecteurActivite.class))).thenReturn(sa);

        mockMvc.perform(post("/secteurActivite/add-secteurActivite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sa)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveSecteurActivite() throws Exception {
        doNothing().when(secteurActiviteService).deleteSecteurActivite(1L);

        mockMvc.perform(delete("/secteurActivite/remove-secteurActivite/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testModifySecteurActivite() throws Exception {
        SecteurActivite sa = new SecteurActivite();
        Mockito.when(secteurActiviteService.updateSecteurActivite(any(SecteurActivite.class))).thenReturn(sa);

        mockMvc.perform(put("/secteurActivite/modify-secteurActivite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sa)))
                .andExpect(status().isOk());
    }
}
