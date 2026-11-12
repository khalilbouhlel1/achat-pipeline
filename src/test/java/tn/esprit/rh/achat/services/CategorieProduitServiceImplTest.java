package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.repositories.CategorieProduitRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategorieProduitServiceImplTest {

    @Mock
    private CategorieProduitRepository categorieProduitRepository;

    @InjectMocks
    private CategorieProduitServiceImpl categorieProduitService;

    @Test
    public void testRetrieveAllCategorieProduits() {
        CategorieProduit cp1 = new CategorieProduit();
        cp1.setIdCategorieProduit(1L);
        cp1.setCodeCategorie("CAT01");
        cp1.setLibelleCategorie("Alimentaire");

        List<CategorieProduit> list = Arrays.asList(cp1);
        when(categorieProduitRepository.findAll()).thenReturn(list);

        List<CategorieProduit> result = categorieProduitService.retrieveAllCategorieProduits();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("CAT01", result.get(0).getCodeCategorie());
        verify(categorieProduitRepository, times(1)).findAll();
    }

    @Test
    public void testAddCategorieProduit() {
        CategorieProduit cp = new CategorieProduit();
        cp.setIdCategorieProduit(1L);
        cp.setCodeCategorie("CAT02");
        cp.setLibelleCategorie("Electroménager");

        when(categorieProduitRepository.save(any(CategorieProduit.class))).thenReturn(cp);

        CategorieProduit savedCategorie = categorieProduitService.addCategorieProduit(cp);

        Assertions.assertNotNull(savedCategorie);
        Assertions.assertEquals("CAT02", savedCategorie.getCodeCategorie());
        verify(categorieProduitRepository, times(1)).save(cp);
    }

    @Test
    public void testRetrieveCategorieProduit() {
        Long id = 1L;
        CategorieProduit cp = new CategorieProduit();
        cp.setIdCategorieProduit(id);
        cp.setCodeCategorie("CAT03");

        when(categorieProduitRepository.findById(id)).thenReturn(Optional.of(cp));

        CategorieProduit result = categorieProduitService.retrieveCategorieProduit(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getIdCategorieProduit());
        verify(categorieProduitRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteCategorieProduit() {
        Long id = 1L;
        doNothing().when(categorieProduitRepository).deleteById(id);

        categorieProduitService.deleteCategorieProduit(id);

        verify(categorieProduitRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateCategorieProduit() {
        CategorieProduit cp = new CategorieProduit();
        cp.setIdCategorieProduit(1L);
        cp.setCodeCategorie("CAT-MODIF");

        when(categorieProduitRepository.save(any(CategorieProduit.class))).thenReturn(cp);

        CategorieProduit updated = categorieProduitService.updateCategorieProduit(cp);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals("CAT-MODIF", updated.getCodeCategorie());
        verify(categorieProduitRepository, times(1)).save(cp);
    }
}