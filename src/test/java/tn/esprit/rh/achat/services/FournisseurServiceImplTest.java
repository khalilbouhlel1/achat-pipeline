package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.DetailFournisseur;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.entities.SecteurActivite;
import tn.esprit.rh.achat.repositories.DetailFournisseurRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.SecteurActiviteRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceImplTest {

    @Mock
    FournisseurRepository fournisseurRepository;

    @Mock
    DetailFournisseurRepository detailFournisseurRepository;

    @Mock
    SecteurActiviteRepository secteurActiviteRepository;

    @InjectMocks
    FournisseurServiceImpl fournisseurService;

    @Test
    void testRetrieveAllFournisseurs() {
        Fournisseur f1 = new Fournisseur();
        Fournisseur f2 = new Fournisseur();
        when(fournisseurRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Fournisseur> result = fournisseurService.retrieveAllFournisseurs();

        assertEquals(2, result.size(), "Should return exactly 2 fournisseurs.");
    }

    @Test
    void testAddFournisseur() {
        Fournisseur f = new Fournisseur();
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.addFournisseur(f);

        assertNotNull(result, "Saved fournisseur should not be null.");
        assertNotNull(result.getDetailFournisseur(), "DetailFournisseur should be initialized and attached by the service.");
    }

    @Test
    void testRetrieveFournisseur() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));

        Fournisseur result = fournisseurService.retrieveFournisseur(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdFournisseur());
    }

    @Test
    void testDeleteFournisseur() {
        doNothing().when(fournisseurRepository).deleteById(1L);

        fournisseurService.deleteFournisseur(1L);

        verify(fournisseurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateFournisseur() {
        Fournisseur f = new Fournisseur();
        DetailFournisseur df = new DetailFournisseur();
        f.setDetailFournisseur(df);

        when(detailFournisseurRepository.save(any(DetailFournisseur.class))).thenReturn(df);
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.updateFournisseur(f);

        assertNotNull(result);
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testAssignSecteurActiviteToFournisseur() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);
        f.setSecteurActivites(new HashSet<>());
        SecteurActivite sa = new SecteurActivite();
        sa.setIdSecteurActivite(2L);

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));
        when(secteurActiviteRepository.findById(2L)).thenReturn(Optional.of(sa));

        fournisseurService.assignSecteurActiviteToFournisseur(2L, 1L);

        assertTrue(f.getSecteurActivites().contains(sa));
        verify(fournisseurRepository, times(1)).save(f);
    }
}
