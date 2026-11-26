package tn.esprit.rh.achat.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.repositories.DetailFactureRepository;
import tn.esprit.rh.achat.repositories.FactureRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.OperateurRepository;
import tn.esprit.rh.achat.repositories.ProduitRepository;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class FactureServiceImpl implements IFactureService {

	@Autowired
	FactureRepository factureRepository;
	@Autowired
	OperateurRepository operateurRepository;
	@Autowired
	DetailFactureRepository detailFactureRepository;
	@Autowired
	FournisseurRepository fournisseurRepository;
	@Autowired
	ProduitRepository produitRepository;
	@Autowired
	ReglementServiceImpl reglementService;
	
	@Override
	public List<Facture> retrieveAllFactures() {
		List<Facture> factures = factureRepository.findAll();
		for (Facture facture : factures) {
			log.info(" facture : {}", facture);
		}
		return factures;
	}

	@Override
	public Facture addFacture(Facture f) {
		return factureRepository.save(f);
	}

	@Override
	public void cancelFacture(Long factureId) {
		Facture facture = factureRepository.findById(factureId).orElse(null);
		if (facture != null) {
			facture.setArchivee(true);
			factureRepository.save(facture);
		}
		factureRepository.updateFacture(factureId);
	}

	@Override
	public Facture retrieveFacture(Long factureId) {
		Facture facture = factureRepository.findById(factureId).orElse(null);
		log.info("facture : {}", facture);
		return facture;
	}

	@Override
	public List<Facture> getFacturesByFournisseur(Long idFournisseur) {
		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur).orElse(null);
		if (fournisseur != null && fournisseur.getFactures() != null) {
			return new ArrayList<>(fournisseur.getFactures());
		}
		return Collections.emptyList();
	}

	@Override
	public void assignOperateurToFacture(Long idOperateur, Long idFacture) {
		Facture facture = factureRepository.findById(idFacture).orElse(null);
		Operateur operateur = operateurRepository.findById(idOperateur).orElse(null);
		if (operateur != null && facture != null && operateur.getFactures() != null) {
			operateur.getFactures().add(facture);
			operateurRepository.save(operateur);
		}
	}

	@Override
	public float pourcentageRecouvrement(Date startDate, Date endDate) {
		float totalFacturesEntreDeuxDates = factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate);
		if (totalFacturesEntreDeuxDates == 0f) {
			return 0f;
		}
		float totalRecouvrementEntreDeuxDates = reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate);
		return (totalRecouvrementEntreDeuxDates / totalFacturesEntreDeuxDates) * 100;
	}

}
