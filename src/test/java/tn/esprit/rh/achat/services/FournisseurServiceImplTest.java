package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.repositories.FournisseurRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceImplTest {

    @Mock
    FournisseurRepository fournisseurRepository;

    @InjectMocks
    FournisseurServiceImpl fournisseurService;

    @Test
    void testRetrieveAllFournisseurs() {
        Fournisseur f1 = new Fournisseur();
        Fournisseur f2 = new Fournisseur();
        Mockito.when(fournisseurRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Fournisseur> result = fournisseurService.retrieveAllFournisseurs();

        assertEquals(2, result.size(), "Should return exactly 2 fournisseurs.");
    }

    @Test
    void testAddFournisseur() {
        Fournisseur f = new Fournisseur();
        Mockito.when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.addFournisseur(f);

        assertNotNull(result, "Saved fournisseur should not be null.");
        assertNotNull(result.getDetailFournisseur(), "DetailFournisseur should be initialized and attached by the service.");
    }
}