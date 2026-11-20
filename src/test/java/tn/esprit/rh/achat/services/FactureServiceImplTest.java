package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.repositories.FactureRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.OperateurRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactureServiceImplTest {

    @Mock
    FactureRepository factureRepository;

    @Mock
    OperateurRepository operateurRepository;

    @Mock
    FournisseurRepository fournisseurRepository;

    @Mock
    ReglementServiceImpl reglementService;

    @InjectMocks
    FactureServiceImpl factureService;

    @Test
    void testAddFacture() {
        Facture f = new Facture();
        Mockito.when(factureRepository.save(any(Facture.class))).thenReturn(f);

        Facture result = factureService.addFacture(f);

        assertNotNull(result, "Saved facture should not be null.");
    }

    @Test
    void testRetrieveFacture() {
        Facture f = new Facture();
        Mockito.when(factureRepository.findById(1L)).thenReturn(Optional.of(f));

        Facture result = factureService.retrieveFacture(1L);

        assertNotNull(result, "Facture should be retrieved successfully.");
    }

    @Test
    void testRetrieveAllFactures() {
        Facture f1 = new Facture();
        Facture f2 = new Facture();
        when(factureRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Facture> result = factureService.retrieveAllFactures();

        assertEquals(2, result.size());
        verify(factureRepository, times(1)).findAll();
    }

    @Test
    void testCancelFacture() {
        Facture f = new Facture();
        f.setIdFacture(1L);
        f.setArchivee(false);
        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));

        factureService.cancelFacture(1L);

        assertTrue(f.getArchivee());
        verify(factureRepository, times(1)).save(f);
        verify(factureRepository, times(1)).updateFacture(1L);
    }

    @Test
    void testGetFacturesByFournisseur() {
        Fournisseur fournisseur = new Fournisseur();
        Facture f = new Facture();
        Set<Facture> set = new HashSet<>(Collections.singletonList(f));
        fournisseur.setFactures(set);
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        List<Facture> result = factureService.getFacturesByFournisseur(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testAssignOperateurToFacture() {
        Facture facture = new Facture();
        facture.setIdFacture(1L);
        Operateur operateur = new Operateur();
        operateur.setIdOperateur(2L);
        operateur.setFactures(new HashSet<>());

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));
        when(operateurRepository.findById(2L)).thenReturn(Optional.of(operateur));

        factureService.assignOperateurToFacture(2L, 1L);

        assertTrue(operateur.getFactures().contains(facture));
        verify(operateurRepository, times(1)).save(operateur);
    }

    @Test
    void testPourcentageRecouvrement() {
        Date start = new Date();
        Date end = new Date();
        when(factureRepository.getTotalFacturesEntreDeuxDates(start, end)).thenReturn(1000f);
        when(reglementService.getChiffreAffaireEntreDeuxDate(start, end)).thenReturn(500f);

        float result = factureService.pourcentageRecouvrement(start, end);

        assertEquals(50f, result, 0.001);
    }
}
