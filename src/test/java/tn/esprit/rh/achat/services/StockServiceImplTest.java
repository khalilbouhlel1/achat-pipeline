package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.StockRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    @Test
    public void testRetrieveAllStocks() {
        Stock s1 = new Stock();
        s1.setIdStock(1L);
        s1.setLibelleStock("Stock Principal");
        s1.setQte(50);
        s1.setQteMin(5);

        List<Stock> list = Arrays.asList(s1);

        when(stockRepository.findAll()).thenReturn(list);

        List<Stock> result = stockService.retrieveAllStocks();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Stock Principal", result.get(0).getLibelleStock());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    public void testAddStock() {
        Stock s = new Stock();
        s.setIdStock(1L);
        s.setLibelleStock("Stock Secondaire");
        s.setQte(100);
        s.setQteMin(10);

        when(stockRepository.save(any(Stock.class))).thenReturn(s);

        Stock savedStock = stockService.addStock(s);

        Assertions.assertNotNull(savedStock);
        Assertions.assertEquals("Stock Secondaire", savedStock.getLibelleStock());
        verify(stockRepository, times(1)).save(s);
    }

    @Test
    public void testRetrieveStock() {
        Long id = 1L;
        Stock s = new Stock();
        s.setIdStock(id);
        s.setLibelleStock("Stock Central");

        when(stockRepository.findById(id)).thenReturn(Optional.of(s));

        Stock result = stockService.retrieveStock(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getIdStock());
        verify(stockRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteStock() {
        Long id = 1L;

        doNothing().when(stockRepository).deleteById(id);

        stockService.deleteStock(id);

        verify(stockRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateStock() {
        Stock s = new Stock();
        s.setIdStock(1L);
        s.setLibelleStock("Stock Modifié");

        when(stockRepository.save(any(Stock.class))).thenReturn(s);

        Stock updatedStock = stockService.updateStock(s);

        Assertions.assertNotNull(updatedStock);
        Assertions.assertEquals("Stock Modifié", updatedStock.getLibelleStock());
        verify(stockRepository, times(1)).save(s);
    }

    @Test
    public void testRetrieveStatusStockWithAlerts() {
        Stock s = new Stock("Stock Danger", 2, 10);
        when(stockRepository.retrieveStatusStock()).thenReturn(Arrays.asList(s));

        String status = stockService.retrieveStatusStock();

        Assertions.assertNotNull(status);
        Assertions.assertTrue(status.contains("Stock Danger"));
        Assertions.assertTrue(status.contains("inférieur à la quantité minimale"));
        verify(stockRepository, times(1)).retrieveStatusStock();
    }

    @Test
    public void testRetrieveStatusStockEmpty() {
        when(stockRepository.retrieveStatusStock()).thenReturn(new ArrayList<>());

        String status = stockService.retrieveStatusStock();

        Assertions.assertNotNull(status);
        Assertions.assertEquals("", status);
        verify(stockRepository, times(1)).retrieveStatusStock();
    }

    @Test
    public void testStockConstructorAndProperties() {
        Stock s = new Stock("Stock Test", 20, 5);
        Assertions.assertEquals("Stock Test", s.getLibelleStock());
        Assertions.assertEquals(20, s.getQte());
        Assertions.assertEquals(5, s.getQteMin());
    }
}
