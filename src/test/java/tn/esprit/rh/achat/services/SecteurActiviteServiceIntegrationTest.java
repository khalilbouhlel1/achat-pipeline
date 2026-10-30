package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.rh.achat.entities.SecteurActivite;

@SpringBootTest
@Transactional
public class SecteurActiviteServiceIntegrationTest {

    @Autowired
    private ISecteurActiviteService secteurActiviteService;

    @Test
    public void testCrudSecteurActiviteComplet() {
        // 1. CREATE (Ajout)
        SecteurActivite sa = new SecteurActivite();
        sa.setCodeSecteurActivite("SA-TEST");
        sa.setLibelleSecteurActivite("Libelle Test Initial");

        SecteurActivite savedSa = secteurActiviteService.addSecteurActivite(sa);
        assertNotNull(savedSa.getIdSecteurActivite());
        Long id = savedSa.getIdSecteurActivite();

        // 2. READ (Récupération par ID et liste)
        SecteurActivite foundSa = secteurActiviteService.retrieveSecteurActivite(id);
        assertNotNull(foundSa);
        assertEquals("Libelle Test Initial", foundSa.getLibelleSecteurActivite());
        assertTrue(secteurActiviteService.retrieveAllSecteurActivite().size() > 0);

        // 3. UPDATE (Modification)
        foundSa.setLibelleSecteurActivite("Libelle Test Modifié");
        SecteurActivite updatedSa = secteurActiviteService.updateSecteurActivite(foundSa);
        assertEquals("Libelle Test Modifié", updatedSa.getLibelleSecteurActivite());

        // 4. DELETE (Suppression)
        secteurActiviteService.deleteSecteurActivite(id);
        
        // Vérification finale que l'élément est bien supprimé
        SecteurActivite deletedSa = secteurActiviteService.retrieveSecteurActivite(id);
        assertNull(deletedSa);
    }
}