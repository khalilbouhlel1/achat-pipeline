package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.rh.achat.entities.Stock;

import java.util.List;

@SpringBootTest
class StockServiceWithoutMockTest {

    @Autowired
    private IStockService stockService; // Injection du service réel

    @Test
    public void testAddAndRetrieveStock() {
        // Given
        Stock s = new Stock();
        s.setLibelleStock("Stock Test");
        s.setQte(150);
        s.setQteMin(15);

        // When (Add)
        Stock savedStock = stockService.addStock(s);

        // Then (Assert Add)
        Assertions.assertNotNull(savedStock);
        Assertions.assertNotNull(savedStock.getIdStock());
        Assertions.assertEquals("Stock Test", savedStock.getLibelleStock());

        // When (Retrieve by ID)
        Stock foundStock = stockService.retrieveStock(savedStock.getIdStock());

        // Then (Assert Retrieve)
        Assertions.assertNotNull(foundStock);
        Assertions.assertEquals(savedStock.getIdStock(), foundStock.getIdStock());

        // Nettoyage de la base de données
        stockService.deleteStock(savedStock.getIdStock());
    }

    @Test
    public void testRetrieveAllStocks() {
        // Given : Ajout d'un stock pour garantir qu'il y a des données
        Stock s = new Stock();
        s.setLibelleStock("Stock All");
        s.setQte(200);
        s.setQteMin(20);
        Stock saved = stockService.addStock(s);

        // When
        List<Stock> list = stockService.retrieveAllStocks();

        // Then
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() > 0);

        // Nettoyage
        stockService.deleteStock(saved.getIdStock());
    }

    @Test
    public void testUpdateStock() {
        // Given
        Stock s = new Stock();
        s.setLibelleStock("Stock Old");
        s.setQte(50);
        s.setQteMin(5);
        Stock saved = stockService.addStock(s);

        // When
        saved.setLibelleStock("Stock Modifié");
        saved.setQte(75);
        Stock updated = stockService.updateStock(saved);

        // Then
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("Stock Modifié", updated.getLibelleStock());
        Assertions.assertEquals(75, updated.getQte());

        // Nettoyage
        stockService.deleteStock(saved.getIdStock());
    }

    @Test
    public void testDeleteStock() {
        // Given
        Stock s = new Stock();
        s.setLibelleStock("Stock Delete");
        s.setQte(10);
        s.setQteMin(1);
        Stock saved = stockService.addStock(s);
        Long id = saved.getIdStock();

        // When
        stockService.deleteStock(id);

        // Then : Vérifier que le stock n'existe plus
        Stock deleted = stockService.retrieveStock(id);
        Assertions.assertNull(deleted);
    }
}