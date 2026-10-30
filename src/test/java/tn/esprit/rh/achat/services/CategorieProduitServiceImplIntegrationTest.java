package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.rh.achat.entities.CategorieProduit;

import java.util.List;

@SpringBootTest
class CategorieProduitServiceWithoutMockTest {

    @Autowired
    private ICategorieProduitService categorieProduitService; // Assurez-vous d'injecter l'interface ou l'implémentation

    @Test
    public void testAddAndRetrieveCategorieProduit() {
        // Given
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT_TEST");
        cp.setLibelleCategorie("Test Libelle");

        // When (Add)
        CategorieProduit savedCategorie = categorieProduitService.addCategorieProduit(cp);

        // Then (Assert Add)
        Assertions.assertNotNull(savedCategorie);
        Assertions.assertNotNull(savedCategorie.getIdCategorieProduit());
        Assertions.assertEquals("CAT_TEST", savedCategorie.getCodeCategorie());

        // When (Retrieve by ID)
        CategorieProduit foundCategorie = categorieProduitService.retrieveCategorieProduit(savedCategorie.getIdCategorieProduit());

        // Then (Assert Retrieve)
        Assertions.assertNotNull(foundCategorie);
        Assertions.assertEquals(savedCategorie.getIdCategorieProduit(), foundCategorie.getIdCategorieProduit());

        // Nettoyage (optionnel selon votre configuration de base de données)
        categorieProduitService.deleteCategorieProduit(savedCategorie.getIdCategorieProduit());
    }

    @Test
    public void testRetrieveAllCategorieProduits() {
        // Given
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT_ALL");
        cp.setLibelleCategorie("Libelle All");
        categorieProduitService.addCategorieProduit(cp);

        // When
        List<CategorieProduit> list = categorieProduitService.retrieveAllCategorieProduits();

        // Then
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    public void testUpdateCategorieProduit() {
        // Given
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT_OLD");
        cp.setLibelleCategorie("Old Libelle");
        CategorieProduit saved = categorieProduitService.addCategorieProduit(cp);

        // When
        saved.setCodeCategorie("CAT_NEW");
        saved.setLibelleCategorie("New Libelle");
        CategorieProduit updated = categorieProduitService.updateCategorieProduit(saved);

        // Then
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("CAT_NEW", updated.getCodeCategorie());
        Assertions.assertEquals("New Libelle", updated.getLibelleCategorie());

        // Cleanup
        categorieProduitService.deleteCategorieProduit(saved.getIdCategorieProduit());
    }

    @Test
    public void testDeleteCategorieProduit() {
        // Given
        CategorieProduit cp = new CategorieProduit();
        cp.setCodeCategorie("CAT_DEL");
        cp.setLibelleCategorie("Delete Libelle");
        CategorieProduit saved = categorieProduitService.addCategorieProduit(cp);
        Long id = saved.getIdCategorieProduit();

        // When
        categorieProduitService.deleteCategorieProduit(id);

        // Then (Vérification qu'elle n'existe plus - selon l'implémentation de retrieve, cela peut lancer une exception ou retourner null/Empty)
        // Par exemple, si retrieve lance une exception ou retourne null si non trouvé :
        CategorieProduit deleted = categorieProduitService.retrieveCategorieProduit(id);
        Assertions.assertNull(deleted); // Ou vérifier via findById si géré
    }
}