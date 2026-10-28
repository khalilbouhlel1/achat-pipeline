package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.repositories.FactureRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FactureServiceImplTest {

    @Mock
    FactureRepository factureRepository;

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
}