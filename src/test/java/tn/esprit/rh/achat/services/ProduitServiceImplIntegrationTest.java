package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.rh.achat.entities.Produit;

import java.util.List;

@SpringBootTest
class ProduitServiceWithoutMockTest {

    @Autowired
    private IProduitService produitService; // Injection du service réel

    @Test
    public void testAddAndRetrieveProduit() {
        // Given
        Produit p = new Produit();
        p.setCodeProduit("PROD_TEST");
        p.setLibelleProduit("Test Produit");

        // When (Add)
        Produit savedProduit = produitService.addProduit(p);

        // Then (Assert Add)
        Assertions.assertNotNull(savedProduit);
        Assertions.assertNotNull(savedProduit.getIdProduit());
        Assertions.assertEquals("PROD_TEST", savedProduit.getCodeProduit());

        // When (Retrieve by ID)
        Produit foundProduit = produitService.retrieveProduit(savedProduit.getIdProduit());

        // Then (Assert Retrieve)
        Assertions.assertNotNull(foundProduit);
        Assertions.assertEquals(savedProduit.getIdProduit(), foundProduit.getIdProduit());

        // Nettoyage de la base de données
        produitService.deleteProduit(savedProduit.getIdProduit());
    }

    @Test
    public void testRetrieveAllProduits() {
        // Given : Ajout d'un produit pour s'assurer qu'il y a des données
        Produit p = new Produit();
        p.setCodeProduit("PROD_ALL");
        p.setLibelleProduit("Libelle All");
        Produit saved = produitService.addProduit(p);

        // When
        List<Produit> list = produitService.retrieveAllProduits();

        // Then
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() > 0);

        // Nettoyage
        produitService.deleteProduit(saved.getIdProduit());
    }

    @Test
    public void testDeleteProduit() {
        // Given
        Produit p = new Produit();
        p.setCodeProduit("PROD_DEL");
        p.setLibelleProduit("Delete Produit");
        Produit saved = produitService.addProduit(p);
        Long id = saved.getIdProduit();

        // When
        produitService.deleteProduit(id);

        // Then : Vérifier que le produit n'existe plus (selon votre implémentation, findById ou retrieve retourne null ou lance une exception)
        Produit deleted = produitService.retrieveProduit(id);
        Assertions.assertNull(deleted);
    }
}