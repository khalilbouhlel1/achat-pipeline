package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.rh.achat.entities.SecteurActivite;
import tn.esprit.rh.achat.repositories.SecteurActiviteRepository;

@ExtendWith(MockitoExtension.class)
public class SecteurActiviteServiceImplTest {

    @Mock
    private SecteurActiviteRepository secteurActiviteRepository;

    @InjectMocks
    private SecteurActiviteServiceImpl secteurActiviteService;

    @Test
    public void testRetrieveAllSecteurActivite() {
        // Read (Liste)
        List<SecteurActivite> list = Arrays.asList(
            new SecteurActivite(1L, "SA1", "Informatique", null),
            new SecteurActivite(2L, "SA2", "Finance", null)
        );

        when(secteurActiviteRepository.findAll()).thenReturn(list);

        List<SecteurActivite> result = secteurActiviteService.retrieveAllSecteurActivite();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(secteurActiviteRepository, times(1)).findAll();
    }

    @Test
    public void testAddSecteurActivite() {
        // Create (Ajout)
        SecteurActivite sa = new SecteurActivite(1L, "SA1", "Informatique", null);

        when(secteurActiviteRepository.save(any(SecteurActivite.class))).thenReturn(sa);

        SecteurActivite savedSa = secteurActiviteService.addSecteurActivite(sa);

        assertNotNull(savedSa);
        assertEquals("Informatique", savedSa.getLibelleSecteurActivite());
        verify(secteurActiviteRepository, times(1)).save(sa);
    }

    @Test
    public void testRetrieveSecteurActivite() {
        // Read (Par ID)
        SecteurActivite sa = new SecteurActivite(1L, "SA1", "Informatique", null);

        when(secteurActiviteRepository.findById(1L)).thenReturn(Optional.of(sa));

        SecteurActivite result = secteurActiviteService.retrieveSecteurActivite(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdSecteurActivite());
        assertEquals("Informatique", result.getLibelleSecteurActivite());
        verify(secteurActiviteRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateSecteurActivite() {
        // Update (Modification)
        SecteurActivite sa = new SecteurActivite(1L, "SA1", "Informatique Modifié", null);

        when(secteurActiviteRepository.save(any(SecteurActivite.class))).thenReturn(sa);

        SecteurActivite updatedSa = secteurActiviteService.updateSecteurActivite(sa);

        assertNotNull(updatedSa);
        assertEquals("Informatique Modifié", updatedSa.getLibelleSecteurActivite());
        verify(secteurActiviteRepository, times(1)).save(sa);
    }

    @Test
    public void testDeleteSecteurActivite() {
        // Delete (Suppression)
        doNothing().when(secteurActiviteRepository).deleteById(1L);

        secteurActiviteService.deleteSecteurActivite(1L);

        verify(secteurActiviteRepository, times(1)).deleteById(1L);
    }
}