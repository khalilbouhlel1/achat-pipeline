package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.ProduitRepository;
import tn.esprit.rh.achat.repositories.StockRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    ProduitRepository produitRepository;

    @Mock
    StockRepository stockRepository;

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

    @Test
    void testDeleteProduit() {
        Long productId = 1L;
        produitService.deleteProduit(productId);
        
        // Verifies the repository's deleteById method was called exactly once with the correct ID
        verify(produitRepository, times(1)).deleteById(productId);
    }

    @Test
    void testUpdateProduit() {
        Produit p = new Produit();
        Mockito.when(produitRepository.save(any(Produit.class))).thenReturn(p);

        Produit updatedProduit = produitService.updateProduit(p);
        
        assertNotNull(updatedProduit, "Updated product should not be null.");
    }

    @Test
    void testRetrieveProduit() {
        Produit p = new Produit();
        Mockito.when(produitRepository.findById(1L)).thenReturn(Optional.of(p));

        Produit result = produitService.retrieveProduit(1L);
        
        assertNotNull(result, "Retrieved product should not be null.");
    }

    @Test
    void testAssignProduitToStock() {
        Produit p = new Produit();
        Stock s = new Stock();
        
        Mockito.when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        Mockito.when(stockRepository.findById(2L)).thenReturn(Optional.of(s));
        Mockito.when(produitRepository.save(any(Produit.class))).thenReturn(p);

        produitService.assignProduitToStock(1L, 2L);
        
        // Verifies the product was saved after the stock was attached
        verify(produitRepository, times(1)).save(p);
        assertEquals(s, p.getStock(), "The stock should be successfully assigned to the product.");
    }
}