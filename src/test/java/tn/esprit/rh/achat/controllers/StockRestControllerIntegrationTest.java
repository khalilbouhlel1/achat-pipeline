package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.services.IStockService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StockRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // On injecte le VRAI service (plus de @MockBean)
    @Autowired
    private IStockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testStockFlowReal() throws Exception {
        // 1. Préparation d'une donnée réelle en base via le service pour les tests de lecture/modification/suppression
        Stock s = new Stock();
        s.setLibelleStock("Stock Principal Intégration");
        s.setQte(100);
        s.setQteMin(10);
        Stock saved = stockService.addStock(s);

        // 2. Test de récupération de tous les stocks (GET)
        mockMvc.perform(get("/stock/retrieve-all-stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNumber());

        // 3. Test de récupération par ID (GET)
        mockMvc.perform(get("/stock/retrieve-stock/{stock-id}", saved.getIdStock()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStock").value(saved.getIdStock()))
                .andExpect(jsonPath("$.libelleStock").value("Stock Principal Intégration"));

        // 4. Test d'ajout d'un nouveau stock via le contrôleur (POST)
        Stock newStock = new Stock();
        newStock.setLibelleStock("Nouveau Stock Post");
        newStock.setQte(50);
        newStock.setQteMin(5);

        mockMvc.perform(post("/stock/add-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelleStock").value("Nouveau Stock Post"));

        // 5. Test de modification via le contrôleur (PUT)
        saved.setLibelleStock("Stock Modifié Intégration");
        mockMvc.perform(put("/stock/modify-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelleStock").value("Stock Modifié Intégration"));

        // 6. Test de suppression via le contrôleur (DELETE)
        mockMvc.perform(delete("/stock/remove-stock/{stock-id}", saved.getIdStock()))
                .andExpect(status().isOk());
    }
}