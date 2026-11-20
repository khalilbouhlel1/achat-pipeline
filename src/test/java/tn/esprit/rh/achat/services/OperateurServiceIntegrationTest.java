package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.rh.achat.entities.Operateur;

@SpringBootTest
@Transactional
public class OperateurServiceIntegrationTest {

    @Autowired
    private IOperateurService operateurService;

    @Test
    public void testAddAndRetrieveOperateurSansMock() {
        // 1. Création et insertion d'un opérateur réel en base
        Operateur op = new Operateur();
        op.setNom("TestNomOp");
        op.setPrenom("TestPrenomOp");
        op.setPassword("password123");

        Operateur savedOp = operateurService.addOperateur(op);
        
        assertNotNull(savedOp.getIdOperateur());

        // 2. Test de la récupération par ID
        Operateur foundOp = operateurService.retrieveOperateur(savedOp.getIdOperateur());
        assertNotNull(foundOp);

        // 3. Test de la récupération de la liste complète
        assertTrue(operateurService.retrieveAllOperateurs().size() > 0);

        // 4. Nettoyage (Suppression)
        operateurService.deleteOperateur(savedOp.getIdOperateur());
    }
}