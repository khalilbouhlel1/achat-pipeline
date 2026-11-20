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

import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.repositories.OperateurRepository;

@ExtendWith(MockitoExtension.class)
public class OperateurServiceImplTest {

    @Mock
    private OperateurRepository operateurRepository;

    @InjectMocks
    private OperateurServiceImpl operateurService;

    @Test
    public void testRetrieveAllOperateurs() {
        List<Operateur> list = Arrays.asList(
            new Operateur(1L, "Ali", "Ben Salah", "pwd123", null),
            new Operateur(2L, "Sami", "Trabelsi", "pwd456", null)
        );

        when(operateurRepository.findAll()).thenReturn(list);

        List<Operateur> result = operateurService.retrieveAllOperateurs();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(operateurRepository, times(1)).findAll();
    }

    @Test
    public void testAddOperateur() {
        Operateur op = new Operateur(1L, "Ali", "Ben Salah", "pwd123", null);

        when(operateurRepository.save(any(Operateur.class))).thenReturn(op);

        Operateur savedOp = operateurService.addOperateur(op);

        assertNotNull(savedOp);
        assertEquals("Ali", savedOp.getNom());
        verify(operateurRepository, times(1)).save(op);
    }

    @Test
    public void testRetrieveOperateur() {
        Operateur op = new Operateur(1L, "Ali", "Ben Salah", "pwd123", null);

        when(operateurRepository.findById(1L)).thenReturn(Optional.of(op));

        Operateur result = operateurService.retrieveOperateur(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdOperateur());
        assertEquals("Ali", result.getNom());
    }

    @Test
    public void testDeleteOperateur() {
        doNothing().when(operateurRepository).deleteById(1L);

        operateurService.deleteOperateur(1L);

        verify(operateurRepository, times(1)).deleteById(1L);
    }
}