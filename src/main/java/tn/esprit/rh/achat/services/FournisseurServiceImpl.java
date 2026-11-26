package tn.esprit.rh.achat.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.rh.achat.entities.DetailFournisseur;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.entities.SecteurActivite;
import tn.esprit.rh.achat.repositories.DetailFournisseurRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.SecteurActiviteRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FournisseurServiceImpl implements IFournisseurService {

	@Autowired
	FournisseurRepository fournisseurRepository;
	@Autowired
	DetailFournisseurRepository detailFournisseurRepository;
	@Autowired
	SecteurActiviteRepository secteurActiviteRepository;

	@Override
	public List<Fournisseur> retrieveAllFournisseurs() {
		List<Fournisseur> fournisseurs = (List<Fournisseur>) fournisseurRepository.findAll();
		for (Fournisseur fournisseur : fournisseurs) {
			log.info(" fournisseur : {}", fournisseur);
		}
		return fournisseurs;
	}

	@Override
	public Fournisseur addFournisseur(Fournisseur f) {
		DetailFournisseur df = new DetailFournisseur();
		df.setDateDebutCollaboration(new Date());
		f.setDetailFournisseur(df);
		fournisseurRepository.save(f);
		return f;
	}
	
	private DetailFournisseur saveDetailFournisseur(Fournisseur f) {
		DetailFournisseur df = f.getDetailFournisseur();
		if (df != null) {
			detailFournisseurRepository.save(df);
		}
		return df;
	}

	@Override
	public Fournisseur updateFournisseur(Fournisseur f) {
		DetailFournisseur df = saveDetailFournisseur(f);
		f.setDetailFournisseur(df);
		fournisseurRepository.save(f);
		return f;
	}

	@Override
	public void deleteFournisseur(Long fournisseurId) {
		log.info("In method deleteFournisseur");
		fournisseurRepository.deleteById(fournisseurId);
	}

	@Override
	public Fournisseur retrieveFournisseur(Long fournisseurId) {
		return fournisseurRepository.findById(fournisseurId).orElse(null);
	}

	@Override
	public void assignSecteurActiviteToFournisseur(Long idSecteurActivite, Long idFournisseur) {
		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur).orElse(null);
		SecteurActivite secteurActivite = secteurActiviteRepository.findById(idSecteurActivite).orElse(null);
		if (fournisseur != null && secteurActivite != null && fournisseur.getSecteurActivites() != null) {
			fournisseur.getSecteurActivites().add(secteurActivite);
			fournisseurRepository.save(fournisseur);
		}
	}

}
