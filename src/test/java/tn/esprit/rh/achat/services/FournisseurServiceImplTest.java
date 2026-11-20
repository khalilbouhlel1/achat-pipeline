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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        verify(fournisseurRepository, times(1)).findAll();
    }

    @Test
    void testAddFournisseur() {
        Fournisseur f = new Fournisseur();
        Mockito.when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.addFournisseur(f);

        assertNotNull(result, "Saved fournisseur should not be null.");
        assertNotNull(result.getDetailFournisseur(), "DetailFournisseur should be initialized and attached by the service.");
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testUpdateFournisseur() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);
        DetailFournisseur df = new DetailFournisseur();
        f.setDetailFournisseur(df);

        when(detailFournisseurRepository.save(any(DetailFournisseur.class))).thenReturn(df);
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.updateFournisseur(f);

        assertNotNull(result);
        assertEquals(df, result.getDetailFournisseur());
        verify(detailFournisseurRepository, times(1)).save(df);
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testUpdateFournisseurWithoutDetail() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);

        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        Fournisseur result = fournisseurService.updateFournisseur(f);

        assertNotNull(result);
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testDeleteFournisseur() {
        doNothing().when(fournisseurRepository).deleteById(1L);

        fournisseurService.deleteFournisseur(1L);

        verify(fournisseurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveFournisseurFound() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));

        Fournisseur result = fournisseurService.retrieveFournisseur(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdFournisseur());
        verify(fournisseurRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveFournisseurNotFound() {
        when(fournisseurRepository.findById(2L)).thenReturn(Optional.empty());

        Fournisseur result = fournisseurService.retrieveFournisseur(2L);

        assertNull(result);
        verify(fournisseurRepository, times(1)).findById(2L);
    }

    @Test
    void testAssignSecteurActiviteToFournisseur() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);
        f.setSecteurActivites(new HashSet<>());

        SecteurActivite sa = new SecteurActivite();
        sa.setIdSecteurActivite(10L);

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));
        when(secteurActiviteRepository.findById(10L)).thenReturn(Optional.of(sa));
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        fournisseurService.assignSecteurActiviteToFournisseur(10L, 1L);

        assertTrue(f.getSecteurActivites().contains(sa));
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testAssignSecteurActiviteToFournisseurWithNullSet() {
        Fournisseur f = new Fournisseur();
        f.setIdFournisseur(1L);
        f.setSecteurActivites(null);

        SecteurActivite sa = new SecteurActivite();
        sa.setIdSecteurActivite(10L);

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(f));
        when(secteurActiviteRepository.findById(10L)).thenReturn(Optional.of(sa));
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(f);

        fournisseurService.assignSecteurActiviteToFournisseur(10L, 1L);

        assertNotNull(f.getSecteurActivites());
        assertTrue(f.getSecteurActivites().contains(sa));
        verify(fournisseurRepository, times(1)).save(f);
    }

    @Test
    void testAssignSecteurActiviteNotFound() {
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.empty());
        when(secteurActiviteRepository.findById(10L)).thenReturn(Optional.empty());

        fournisseurService.assignSecteurActiviteToFournisseur(10L, 1L);

        verify(fournisseurRepository, never()).save(any());
    }
}
