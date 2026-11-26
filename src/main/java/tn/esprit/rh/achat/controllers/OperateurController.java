package tn.esprit.rh.achat.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.services.IOperateurService;

import java.util.List;

@RestController
@Tag(name = "Gestion des opérateurs")
@RequestMapping("/operateur")
@CrossOrigin("*")
public class OperateurController {

	@Autowired
	IOperateurService operateurService;
	
	@GetMapping("/retrieve-all-operateurs")
	public List<Operateur> getOperateurs() {
		return operateurService.retrieveAllOperateurs();
	}

	@GetMapping("/retrieve-operateur/{operateur-id}")
	public Operateur retrieveOperateur(@PathVariable("operateur-id") Long operateurId) {
		return operateurService.retrieveOperateur(operateurId);
	}

	@PostMapping("/add-operateur")
	public Operateur addOperateur(@RequestBody Operateur op) {
		return operateurService.addOperateur(op);
	}

	@DeleteMapping("/remove-operateur/{operateur-id}")
	public void removeOperateur(@PathVariable("operateur-id") Long operateurId) {
		operateurService.deleteOperateur(operateurId);
	}

	@PutMapping("/modify-operateur")
	public Operateur modifyOperateur(@RequestBody Operateur operateur) {
		return operateurService.updateOperateur(operateur);
	}

}
