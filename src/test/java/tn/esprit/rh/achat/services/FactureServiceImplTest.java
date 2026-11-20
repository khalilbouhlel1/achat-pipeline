package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.repositories.FactureRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.OperateurRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(factureRepository.save(any(Facture.class))).thenReturn(f);

        Facture result = factureService.addFacture(f);

        assertNotNull(result);
        verify(factureRepository, times(1)).save(f);
    }

    @Test
    void testRetrieveFacture() {
        Facture f = new Facture();
        f.setIdFacture(1L);
        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));

        Facture result = factureService.retrieveFacture(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdFacture());
    }

    @Test
    void testRetrieveFactureNotFound() {
        when(factureRepository.findById(99L)).thenReturn(Optional.empty());

        Facture result = factureService.retrieveFacture(99L);

        assertNull(result);
    }

    @Test
    void testRetrieveAllFactures() {
        Facture f1 = new Facture();
        Facture f2 = new Facture();
        when(factureRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Facture> result = factureService.retrieveAllFactures();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testCancelFacture() {
        Facture f = new Facture();
        f.setIdFacture(1L);
        f.setArchivee(false);
        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));
        when(factureRepository.save(any(Facture.class))).thenReturn(f);

        factureService.cancelFacture(1L);

        assertTrue(f.getArchivee());
        verify(factureRepository, times(1)).findById(1L);
        verify(factureRepository, times(1)).save(f);
        verify(factureRepository, times(1)).updateFacture(1L);
    }

    @Test
    void testGetFacturesByFournisseur() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        fournisseur.setFactures(new HashSet<>());

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        assertThrows(ClassCastException.class, () -> factureService.getFacturesByFournisseur(1L));
        verify(fournisseurRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignOperateurToFacture() {
        Facture facture = new Facture();
        facture.setIdFacture(10L);

        Operateur operateur = new Operateur();
        operateur.setIdOperateur(2L);
        operateur.setFactures(new HashSet<>());

        when(factureRepository.findById(10L)).thenReturn(Optional.of(facture));
        when(operateurRepository.findById(2L)).thenReturn(Optional.of(operateur));
        when(operateurRepository.save(any(Operateur.class))).thenReturn(operateur);

        factureService.assignOperateurToFacture(2L, 10L);

        assertEquals(1, operateur.getFactures().size());
        verify(operateurRepository, times(1)).save(operateur);
    }

    @Test
    void testPourcentageRecouvrement() {
        Date startDate = new Date();
        Date endDate = new Date();

        when(factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate)).thenReturn(200.0f);
        when(reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(150.0f);

        float result = factureService.pourcentageRecouvrement(startDate, endDate);

        assertEquals(75.0f, result);
    }
}