package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rh.achat.entities.Produit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ProduitServiceIntegrationTest {

    @Autowired
    IProduitService produitService; // This is now the REAL service connected to H2

    @Test
    void testAddAndRetrieveProduit() {
        // 1. Create a real product in the database
        Produit p = new Produit();
        p.setCodeProduit("PROD-INT-01");
        p.setLibelleProduit("Integration Test Product");
        p.setPrix(150.0f);

        Produit savedProduit = produitService.addProduit(p);
        assertNotNull(savedProduit.getIdProduit(), "The database should generate an ID.");

        // 2. Retrieve it to prove the transaction succeeded
        List<Produit> allProduits = produitService.retrieveAllProduits();
        assertTrue(allProduits.size() > 0, "The database should contain at least one product.");
    }
}