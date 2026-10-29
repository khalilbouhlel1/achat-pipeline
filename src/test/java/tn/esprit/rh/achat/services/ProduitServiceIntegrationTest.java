package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.ProduitRepository;
import tn.esprit.rh.achat.repositories.StockRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProduitServiceIntegrationTest {

    @Autowired
    IProduitService produitService;
    
    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProduitRepository produitRepository;

    @Test
    void testAddAndRetrieveAllProduits() {
        Produit p = new Produit();
        p.setCodeProduit("PROD-INT-01");
        produitService.addProduit(p);

        List<Produit> allProduits = produitService.retrieveAllProduits();
        assertTrue(allProduits.size() > 0, "The database should contain at least one product.");
    }

    @Test
    void testUpdateProduit() {
        Produit p = new Produit();
        p.setCodeProduit("PROD-OLD");
        Produit savedProduit = produitService.addProduit(p);

        savedProduit.setCodeProduit("PROD-UPDATED");
        Produit updatedProduit = produitService.updateProduit(savedProduit);

        assertEquals("PROD-UPDATED", updatedProduit.getCodeProduit(), "The product code should reflect the update.");
    }

    @Test
    void testDeleteProduit() {
        Produit p = new Produit();
        p.setCodeProduit("PROD-TO-DELETE");
        Produit savedProduit = produitService.addProduit(p);
        
        Long id = savedProduit.getIdProduit();
        assertNotNull(produitService.retrieveProduit(id), "Product must exist before deletion.");

        produitService.deleteProduit(id);
        
        assertNull(produitService.retrieveProduit(id), "Product must be completely removed from the database.");
    }

    @Test
    void testRetrieveProduit() {
        Produit p = new Produit();
        p.setCodeProduit("PROD-SPECIFIC");
        Produit saved = produitService.addProduit(p);

        Produit retrieved = produitService.retrieveProduit(saved.getIdProduit());
        assertNotNull(retrieved, "Should retrieve the specific product by ID.");
        assertEquals("PROD-SPECIFIC", retrieved.getCodeProduit(), "Codes must match.");
    }

    @Test
    void testAssignProduitToStock() {
        // 1. Create and persist a physical Stock entity directly to the DB
        Stock s = new Stock();
        s.setLibelleStock("TEST-STOCK");
        Stock savedStock = stockRepository.save(s);

        // 2. Create and persist a Produit entity
        Produit p = new Produit();
        p.setCodeProduit("PROD-STOCK-TEST");
        Produit savedProduit = produitService.addProduit(p);

        // 3. Execute the service logic to bind them
        produitService.assignProduitToStock(savedProduit.getIdProduit(), savedStock.getIdStock());

        // 4. Retrieve from the database and verify the relationship persists
        Produit updatedProduit = produitService.retrieveProduit(savedProduit.getIdProduit());
        assertNotNull(updatedProduit.getStock(), "The stock object should be mapped to the product in the database.");
        assertEquals(savedStock.getIdStock(), updatedProduit.getStock().getIdStock(), "The mapped stock ID must match the assigned stock ID.");
    }
}