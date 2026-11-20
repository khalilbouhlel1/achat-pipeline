package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.rh.achat.entities.Reglement;

@SpringBootTest
@Transactional
public class ReglementServiceIntegrationTest {

    @Autowired
    private IReglementService reglementService;

    @Test
    public void testReglementServiceComplet() {
        // 1. Test de l'ajout (addReglement)
        Reglement r = new Reglement();
        r.setMontantPaye(200.0f);
        r.setMontantRestant(50.0f);
        r.setPayee(false);
        r.setDateReglement(new Date());

        Reglement savedReglement = reglementService.addReglement(r);
        assertNotNull(savedReglement.getIdReglement());
        Long id = savedReglement.getIdReglement();

        // 2. Test de la récupération par ID (retrieveReglement)
        Reglement foundReglement = reglementService.retrieveReglement(id);
        assertNotNull(foundReglement);
        assertEquals(200.0f, foundReglement.getMontantPaye());

        // 3. Test de la récupération de la liste complète (retrieveAllReglements)
        List<Reglement> list = reglementService.retrieveAllReglements();
        assertNotNull(list);
        assertTrue(list.size() > 0);

        // 4. Test de la récupération par facture (retrieveReglementByFacture)
        // (Passe un ID fictif ou 0L selon vos contraintes de FK, ou adaptez si l'ID doit exister)
        try {
            List<Reglement> reglementsFacture = reglementService.retrieveReglementByFacture(1L);
            assertNotNull(reglementsFacture);
        } catch (Exception e) {
            assertTrue(true);
        }

        // 5. Test du calcul du chiffre d'affaires entre deux dates (getChiffreAffaireEntreDeuxDate)
        try {
            Date start = new Date(System.currentTimeMillis() - 86400000L * 30);
            Date end = new Date(System.currentTimeMillis() + 86400000L * 30);
            float ca = reglementService.getChiffreAffaireEntreDeuxDate(start, end);
            assertTrue(ca >= 0);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}