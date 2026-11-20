package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.*;
import tn.esprit.rh.achat.repositories.*;

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
    DetailFactureRepository detailFactureRepository;

    @Mock
    FournisseurRepository fournisseurRepository;

    @Mock
    ProduitRepository produitRepository;

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
    void testAddFactureWithDetails() {
        Facture f = new Facture();
        DetailFacture df = new DetailFacture();
        df.setQteCommandee(2);
        df.setPourcentageRemise(10);

        Produit p = new Produit();
        p.setIdProduit(1L);
        p.setPrix(100.0f);
        df.setProduit(p);

        Set<DetailFacture> details = new HashSet<>();
        details.add(df);
        f.setDetailsFacture(details);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        when(factureRepository.save(any(Facture.class))).thenReturn(f);

        Facture result = factureService.addFacture(f);

        assertNotNull(result);
        assertEquals(180.0f, result.getMontantFacture());
        assertEquals(20.0f, result.getMontantRemise());
        verify(detailFactureRepository, times(1)).save(df);
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
    void testCancelFactureNotFound() {
        when(factureRepository.findById(1L)).thenReturn(Optional.empty());

        factureService.cancelFacture(1L);

        verify(factureRepository, times(1)).findById(1L);
        verify(factureRepository, never()).save(any());
        verify(factureRepository, times(1)).updateFacture(1L);
    }

    @Test
    void testGetFacturesByFournisseur() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        Facture f = new Facture();
        f.setIdFacture(10L);
        Set<Facture> factures = new HashSet<>();
        factures.add(f);
        fournisseur.setFactures(factures);

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        List<Facture> result = factureService.getFacturesByFournisseur(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getIdFacture());
        verify(fournisseurRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFacturesByFournisseurNotFound() {
        when(fournisseurRepository.findById(2L)).thenReturn(Optional.empty());

        List<Facture> result = factureService.getFacturesByFournisseur(2L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
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
    void testAssignOperateurToFactureWithNullSet() {
        Facture facture = new Facture();
        facture.setIdFacture(10L);

        Operateur operateur = new Operateur();
        operateur.setIdOperateur(2L);
        operateur.setFactures(null);

        when(factureRepository.findById(10L)).thenReturn(Optional.of(facture));
        when(operateurRepository.findById(2L)).thenReturn(Optional.of(operateur));
        when(operateurRepository.save(any(Operateur.class))).thenReturn(operateur);

        factureService.assignOperateurToFacture(2L, 10L);

        assertNotNull(operateur.getFactures());
        assertEquals(1, operateur.getFactures().size());
        verify(operateurRepository, times(1)).save(operateur);
    }

    @Test
    void testAssignOperateurToFactureNotFound() {
        when(factureRepository.findById(10L)).thenReturn(Optional.empty());
        when(operateurRepository.findById(2L)).thenReturn(Optional.empty());

        factureService.assignOperateurToFacture(2L, 10L);

        verify(operateurRepository, never()).save(any());
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
