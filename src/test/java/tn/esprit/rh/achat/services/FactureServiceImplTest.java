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
    FournisseurRepository fournisseurRepository;

    @Mock
    OperateurRepository operateurRepository;

    @Mock
    ReglementServiceImpl reglementService;

    @InjectMocks
    FactureServiceImpl factureService;

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
    void testAddFacture() {
        Facture f = new Facture();
        when(factureRepository.save(any(Facture.class))).thenReturn(f);

        Facture result = factureService.addFacture(f);

        assertNotNull(result, "Saved facture should not be null.");
        verify(factureRepository, times(1)).save(f);
    }

    @Test
    void testRetrieveFacture() {
        Facture f = new Facture();
        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));

        Facture result = factureService.retrieveFacture(1L);

        assertNotNull(result, "Facture should be retrieved successfully.");
        verify(factureRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveFactureNull() {
        when(factureRepository.findById(99L)).thenReturn(Optional.empty());

        Facture result = factureService.retrieveFacture(99L);

        assertNull(result);
    }

    @Test
    void testCancelFacture() {
        Facture f = new Facture();
        f.setIdFacture(1L);
        f.setArchivee(false);
        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));
        when(factureRepository.save(any(Facture.class))).thenReturn(f);
        doNothing().when(factureRepository).updateFacture(1L);

        factureService.cancelFacture(1L);

        assertTrue(f.getArchivee());
        verify(factureRepository, times(1)).save(f);
        verify(factureRepository, times(1)).updateFacture(1L);
    }

    @Test
    void testGetFacturesByFournisseur() {
        Fournisseur fournisseur = new Fournisseur();
        Facture f = new Facture();
        Set<Facture> factures = new HashSet<>(Collections.singletonList(f));
        fournisseur.setFactures(factures);

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        List<Facture> result = factureService.getFacturesByFournisseur(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetFacturesByFournisseurNotFound() {
        when(fournisseurRepository.findById(99L)).thenReturn(Optional.empty());

        List<Facture> result = factureService.getFacturesByFournisseur(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAssignOperateurToFacture() {
        Facture f = new Facture();
        Operateur op = new Operateur();
        op.setFactures(new HashSet<>());

        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));
        when(operateurRepository.findById(2L)).thenReturn(Optional.of(op));
        when(operateurRepository.save(any(Operateur.class))).thenReturn(op);

        factureService.assignOperateurToFacture(2L, 1L);

        assertTrue(op.getFactures().contains(f));
        verify(operateurRepository, times(1)).save(op);
    }

    @Test
    void testPourcentageRecouvrement() {
        Date start = new Date();
        Date end = new Date();

        when(factureRepository.getTotalFacturesEntreDeuxDates(start, end)).thenReturn(1000f);
        when(reglementService.getChiffreAffaireEntreDeuxDate(start, end)).thenReturn(500f);

        float pourcentage = factureService.pourcentageRecouvrement(start, end);

        assertEquals(50f, pourcentage, 0.001f);
    }

    @Test
    void testPourcentageRecouvrementZeroTotal() {
        Date start = new Date();
        Date end = new Date();

        when(factureRepository.getTotalFacturesEntreDeuxDates(start, end)).thenReturn(0f);

        float pourcentage = factureService.pourcentageRecouvrement(start, end);

        assertEquals(0f, pourcentage, 0.001f);
    }

    // --- cancelFacture: null facture branch ---
    @Test
    void testCancelFactureNotFound() {
        when(factureRepository.findById(99L)).thenReturn(Optional.empty());
        doNothing().when(factureRepository).updateFacture(99L);

        factureService.cancelFacture(99L);

        verify(factureRepository, never()).save(any());
        verify(factureRepository, times(1)).updateFacture(99L);
    }

    // --- getFacturesByFournisseur: fournisseur found but getFactures() == null ---
    @Test
    void testGetFacturesByFournisseurNullFactures() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setFactures(null);

        when(fournisseurRepository.findById(2L)).thenReturn(Optional.of(fournisseur));

        List<Facture> result = factureService.getFacturesByFournisseur(2L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- assignOperateurToFacture: operateur null ---
    @Test
    void testAssignOperateurToFactureOperateurNull() {
        Facture f = new Facture();
        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));
        when(operateurRepository.findById(99L)).thenReturn(Optional.empty());

        factureService.assignOperateurToFacture(99L, 1L);

        verify(operateurRepository, never()).save(any());
    }

    // --- assignOperateurToFacture: facture null ---
    @Test
    void testAssignOperateurToFactureFactureNull() {
        Operateur op = new Operateur();
        op.setFactures(new HashSet<>());
        when(factureRepository.findById(99L)).thenReturn(Optional.empty());
        when(operateurRepository.findById(1L)).thenReturn(Optional.of(op));

        factureService.assignOperateurToFacture(1L, 99L);

        verify(operateurRepository, never()).save(any());
    }

    // --- assignOperateurToFacture: operateur.getFactures() == null ---
    @Test
    void testAssignOperateurToFactureOperateurFacturesNull() {
        Facture f = new Facture();
        Operateur op = new Operateur();
        op.setFactures(null); // getFactures() returns null => condition false

        when(factureRepository.findById(1L)).thenReturn(Optional.of(f));
        when(operateurRepository.findById(2L)).thenReturn(Optional.of(op));

        factureService.assignOperateurToFacture(2L, 1L);

        verify(operateurRepository, never()).save(any());
    }
}
