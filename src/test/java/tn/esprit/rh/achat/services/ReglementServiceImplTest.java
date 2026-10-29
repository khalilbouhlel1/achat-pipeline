package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.rh.achat.entities.Reglement;
import tn.esprit.rh.achat.repositories.FactureRepository;
import tn.esprit.rh.achat.repositories.ReglementRepository;

@ExtendWith(MockitoExtension.class)
public class ReglementServiceImplTest {

    @Mock
    private ReglementRepository reglementRepository;

    @Mock
    private FactureRepository factureRepository;

    @InjectMocks
    private ReglementServiceImpl reglementService;

    @Test
    public void testRetrieveAllReglements() {
        // Read (Liste complète)
        List<Reglement> list = Arrays.asList(
            new Reglement(1L, 100.0f, 0.0f, true, new Date(), null),
            new Reglement(2L, 250.0f, 50.0f, false, new Date(), null)
        );

        when(reglementRepository.findAll()).thenReturn(list);

        List<Reglement> result = reglementService.retrieveAllReglements();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reglementRepository, times(1)).findAll();
    }

    @Test
    public void testAddReglement() {
        // Create (Ajout)
        Reglement r = new Reglement(1L, 100.0f, 0.0f, true, new Date(), null);

        when(reglementRepository.save(any(Reglement.class))).thenReturn(r);

        Reglement savedReglement = reglementService.addReglement(r);

        assertNotNull(savedReglement);
        assertEquals(100.0f, savedReglement.getMontantPaye());
        verify(reglementRepository, times(1)).save(r);
    }

    @Test
    public void testRetrieveReglement() {
        // Read (Par ID)
        Reglement r = new Reglement(1L, 150.0f, 0.0f, true, new Date(), null);

        when(reglementRepository.findById(1L)).thenReturn(Optional.of(r));

        Reglement result = reglementService.retrieveReglement(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdReglement());
        assertEquals(150.0f, result.getMontantPaye());
        verify(reglementRepository, times(1)).findById(1L);
    }

    @Test
    public void testRetrieveReglementByFacture() {
        // Méthode spécifique liée aux factures
        List<Reglement> list = Arrays.asList(
            new Reglement(1L, 100.0f, 0.0f, true, new Date(), null)
        );

        when(reglementRepository.retrieveReglementByFacture(5L)).thenReturn(list);

        List<Reglement> result = reglementService.retrieveReglementByFacture(5L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reglementRepository, times(1)).retrieveReglementByFacture(5L);
    }

    @Test
    public void testGetChiffreAffaireEntreDeuxDate() {
        // Méthode de calcul métier
        Date startDate = new Date();
        Date endDate = new Date();

        when(reglementRepository.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(500.0f);

        float chiffreAffaire = reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate);

        assertEquals(500.0f, chiffreAffaire);
        verify(reglementRepository, times(1)).getChiffreAffaireEntreDeuxDate(startDate, endDate);
    }
}