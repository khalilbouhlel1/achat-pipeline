package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Test
    void testUpdateFournisseur() {
        Fournisseur f = new Fournisseur();
        DetailFournisseur df = new DetailFournisseur();
        f.setDetailFournisseur(df);

        when(detailFournisseurRepository.save(any(DetailFournisseur.class))).thenReturn(df);
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.updateFournisseur(f);

        assertNotNull(result);
        verify(detailFournisseurRepository, times(1)).save(df);
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testDeleteFournisseur() {
        doNothing().when(fournisseurRepository).deleteById(1L);

        fournisseurService.deleteFournisseur(1L);

        verify(fournisseurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveFournisseur() {
        Fournisseur f = new Fournisseur();
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));

        Fournisseur result = fournisseurService.retrieveFournisseur(1L);

        assertNotNull(result);
        verify(fournisseurRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignSecteurActiviteToFournisseur() {
        Fournisseur f = new Fournisseur();
        f.setSecteurActivites(new HashSet<>());
        SecteurActivite sa = new SecteurActivite();

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));
        when(secteurActiviteRepository.findById(2L)).thenReturn(Optional.of(sa));
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        fournisseurService.assignSecteurActiviteToFournisseur(2L, 1L);

        assertTrue(f.getSecteurActivites().contains(sa));
        verify(fournisseurRepository, times(1)).save(f);
    }

    // --- updateFournisseur: df == null branch in saveDetailFournisseur ---
    @Test
    void testUpdateFournisseurNullDetailFournisseur() {
        Fournisseur f = new Fournisseur();
        f.setDetailFournisseur(null);

        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.updateFournisseur(f);

        assertNotNull(result);
        verify(detailFournisseurRepository, never()).save(any());
        verify(fournisseurRepository, times(1)).save(f);
    }

    // --- assignSecteurActiviteToFournisseur: fournisseur not found ---
    @Test
    void testAssignSecteurActiviteToFournisseurFournisseurNull() {
        when(fournisseurRepository.findById(99L)).thenReturn(Optional.empty());
        when(secteurActiviteRepository.findById(2L)).thenReturn(Optional.of(new SecteurActivite()));

        fournisseurService.assignSecteurActiviteToFournisseur(2L, 99L);

        verify(fournisseurRepository, never()).save(any());
    }

    // --- assignSecteurActiviteToFournisseur: secteur not found ---
    @Test
    void testAssignSecteurActiviteToFournisseurSecteurNull() {
        Fournisseur f = new Fournisseur();
        f.setSecteurActivites(new HashSet<>());
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));
        when(secteurActiviteRepository.findById(99L)).thenReturn(Optional.empty());

        fournisseurService.assignSecteurActiviteToFournisseur(99L, 1L);

        verify(fournisseurRepository, never()).save(any());
    }

    // --- assignSecteurActiviteToFournisseur: secteurActivites == null ---
    @Test
    void testAssignSecteurActiviteToFournisseurSecteurActivitesNull() {
        Fournisseur f = new Fournisseur();
        f.setSecteurActivites(null);
        SecteurActivite sa = new SecteurActivite();

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));
        when(secteurActiviteRepository.findById(2L)).thenReturn(Optional.of(sa));

        fournisseurService.assignSecteurActiviteToFournisseur(2L, 1L);

        verify(fournisseurRepository, never()).save(any());
    }
}
