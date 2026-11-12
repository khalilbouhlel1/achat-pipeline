package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.services.IStockService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StockRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRetrieveAllStocks() throws Exception {
        Stock s1 = new Stock();
        s1.setIdStock(1L);
        s1.setLibelleStock("Stock Principal");
        s1.setQte(100);
        s1.setQteMin(10);

        List<Stock> list = Arrays.asList(s1);

        when(stockService.retrieveAllStocks()).thenReturn(list);

        // Adaptez l'URL selon le @RequestMapping de votre StockRestRestController
        mockMvc.perform(get("/stock/retrieve-all-stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].libelleStock").value("Stock Principal"));
    }

    @Test
    public void testRetrieveStock() throws Exception {
        Long id = 1L;
        Stock s = new Stock();
        s.setIdStock(id);
        s.setLibelleStock("Stock Central");

        when(stockService.retrieveStock(id)).thenReturn(s);

        mockMvc.perform(get("/stock/retrieve-stock/{stock-id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStock").value(1L))
                .andExpect(jsonPath("$.libelleStock").value("Stock Central"));
    }

    @Test
    public void testAddStock() throws Exception {
        Stock s = new Stock();
        s.setIdStock(1L);
        s.setLibelleStock("Nouveau Stock");
        s.setQte(50);
        s.setQteMin(5);

        when(stockService.addStock(any(Stock.class))).thenReturn(s);

        mockMvc.perform(post("/stock/add-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(s)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStock").value(1L))
                .andExpect(jsonPath("$.libelleStock").value("Nouveau Stock"));
    }

    @Test
    public void testModifyStock() throws Exception {
        Stock s = new Stock();
        s.setIdStock(1L);
        s.setLibelleStock("Stock Modifié");

        when(stockService.updateStock(any(Stock.class))).thenReturn(s);

        mockMvc.perform(put("/stock/modify-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(s)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelleStock").value("Stock Modifié"));
    }

    @Test
    public void testRemoveStock() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/stock/remove-stock/{stock-id}", id))
                .andExpect(status().isOk());
    }
}