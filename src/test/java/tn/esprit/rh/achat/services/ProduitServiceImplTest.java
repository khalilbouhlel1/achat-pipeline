package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.repositories.ProduitRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    @Test
    public void testRetrieveAllProduits() {
        Produit p1 = new Produit();
        p1.setIdProduit(1L);
        p1.setCodeProduit("PROD01");
        p1.setLibelleProduit("PC Portable");

        List<Produit> list = Arrays.asList(p1);

        when(produitRepository.findAll()).thenReturn(list);

        List<Produit> result = produitService.retrieveAllProduits();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("PROD01", result.get(0).getCodeProduit());
        verify(produitRepository, times(1)).findAll();
    }

    @Test
    public void testAddProduit() {
        Produit p = new Produit();
        p.setIdProduit(1L);
        p.setCodeProduit("PROD02");
        p.setLibelleProduit("Imprimante");

        when(produitRepository.save(any(Produit.class))).thenReturn(p);

        Produit savedProduit = produitService.addProduit(p);

        Assertions.assertNotNull(savedProduit);
        Assertions.assertEquals("PROD02", savedProduit.getCodeProduit());
        verify(produitRepository, times(1)).save(p);
    }

    @Test
    public void testRetrieveProduit() {
        Long id = 1L;
        Produit p = new Produit();
        p.setIdProduit(id);
        p.setCodeProduit("PROD03");

        when(produitRepository.findById(id)).thenReturn(Optional.of(p));

        Produit result = produitService.retrieveProduit(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getIdProduit());
        verify(produitRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteProduit() {
        Long id = 1L;

        doNothing().when(produitRepository).deleteById(id);

        produitService.deleteProduit(id);

        verify(produitRepository, times(1)).deleteById(id);
    }
}