package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.repositories.ProduitRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    ProduitRepository produitRepository;

    @InjectMocks
    ProduitServiceImpl produitService;

    @Test
    void testRetrieveAllProduits() {
        Produit p1 = new Produit();
        Produit p2 = new Produit();
        Mockito.when(produitRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Produit> result = produitService.retrieveAllProduits();
        
        assertEquals(2, result.size(), "Should return exactly 2 products.");
    }

    @Test
    void testAddProduit() {
        Produit p = new Produit();
        Mockito.when(produitRepository.save(any(Produit.class))).thenReturn(p);

        Produit savedProduit = produitService.addProduit(p);
        
        assertNotNull(savedProduit, "Saved product should not be null.");
    }
}