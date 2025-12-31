// service/impl/EndpruefungServiceImpl.java
package FLCS.GESTION.Services.impl;

import FLCS.GESTION.Dtos.Request.EndpruefungRequest;
import FLCS.GESTION.Dtos.response.EndpruefungResponse;
import FLCS.GESTION.Models.Endpruefung;
import FLCS.GESTION.Models.Eleve;
import FLCS.GESTION.Models.Enseignant;
import FLCS.GESTION.Models.Niveau;
import FLCS.GESTION.Repository.EleveRepository;
import FLCS.GESTION.Repository.EndpruefungRepository;
import FLCS.GESTION.Repository.EnseignantRepository;
import FLCS.GESTION.Repository.NiveauRepository;

import FLCS.GESTION.Services.EndpruefungService;
import FLCS.GESTION.Services.EvaluationHebdomadaireService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RelationNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndpruefungServiceImpl implements EndpruefungService {

    private final EndpruefungRepository endpruefungRepository;
    private final EleveRepository eleveRepository;
    private final NiveauRepository niveauRepository;
    private final EnseignantRepository enseignantRepository;
    private final EvaluationHebdomadaireService evaluationHebdoService;

    @Override
    @Transactional
    public EndpruefungResponse creerEndpruefung(EndpruefungRequest request) {
        log.info("Création Endprüfung - Élève: {}, Niveau: {}",
                request.getEleveId(), request.getNiveauId());

        // Vérifier si un Endprüfung existe déjà pour cet élève dans ce niveau
        if (endpruefungRepository.findByEleveIdAndNiveauId(
                request.getEleveId(), request.getNiveauId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un examen final existe déjà pour cet élève dans ce niveau");
        }
        // Récupérer les entités
        Eleve eleve = null;
        try {
            eleve = eleveRepository.findById((Long) request.getEleveId())
                    .orElseThrow(() -> new RelationNotFoundException("Élève non trouvé"));
        } catch (RelationNotFoundException e) {

            e.printStackTrace();
        }
        Niveau niveau = null;
        try {
            niveau = niveauRepository.findById((Long) request.getNiveauId())
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé"));
        } catch (RelationNotFoundException e) {
            e.printStackTrace();
        }

        // Vérifier que l'élève est bien dans ce niveau
        if (!eleve.getNiveau().getId().equals(niveau.getId())) {
            throw new IllegalArgumentException(
                    "L'élève n'est pas inscrit dans le niveau spécifié");
        }

        // Créer l'Endprüfung
        Endpruefung endpruefung = new Endpruefung();
        endpruefung.setEleve(eleve);
        endpruefung.setNiveau(niveau);
        if (request.getObservateurId() != null) {
            Enseignant observateur = null;
            try {
                observateur = enseignantRepository.findById((Long) request.getObservateurId())
                        .orElseThrow(() -> new RelationNotFoundException("Observateur non trouvé"));
            } catch (RelationNotFoundException e) {

                e.printStackTrace();
            }
            endpruefung.setObservateur(observateur);
        }
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setHeureFin(request.getHeureDebut());
        endpruefung.setHeureFin(request.getHeureDebut());
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteGrammatik(request.getNoteGrammatik());
        endpruefung.setNoteLesen(request.getNoteLesen());
        // endpruefung.setCommentaire(request.getCommentaire());

        // Calculer les moyennes
        endpruefung.calculerMoyenneExamen();

        // Calculer la moyenne hebdo globale pour cet élève
        Double moyenneHebdoGlobale = evaluationHebdoService
                .calculerMoyenneHebdoGlobale(eleve.getId(), niveau.getId());

        // Calculer la moyenne finale
        endpruefung.calculerMoyenneFinale(moyenneHebdoGlobale);

        Endpruefung savedEndpruefung = endpruefungRepository.save(endpruefung);

        log.info("Endprüfung créé avec succès - ID: {}", savedEndpruefung.getId());
        return mapToResponse(savedEndpruefung);
    }

    @Override
    @Transactional
    public EndpruefungResponse mettreAJourEndpruefung(Long id, EndpruefungRequest request)
            throws RelationNotFoundException {
        Endpruefung endpruefung = endpruefungRepository.findById(id)
                .orElseThrow(() -> new RelationNotFoundException("Endprüfung non trouvé"));

        // Vérifier les nouvelles données
        if (!endpruefung.getEleve().getId().equals(request.getEleveId()) ||
                !endpruefung.getNiveau().getId().equals(request.getNiveauId())) {
        }

        // Mettre à jour les données de base
        if (!endpruefung.getEleve().getId().equals(request.getEleveId())) {
            Eleve nouvelEleve = eleveRepository.findById((Long) request.getEleveId())
                    .orElseThrow(() -> new RelationNotFoundException("Élève non trouvé"));
            endpruefung.setEleve(nouvelEleve);
        }

        if (!endpruefung.getNiveau().getId().equals(request.getNiveauId())) {
            Niveau nouveauNiveau = niveauRepository.findById((Long) request.getNiveauId())
                    .orElseThrow(() -> new RelationNotFoundException("Niveau non trouvé"));
            endpruefung.setNiveau(nouveauNiveau);
        }

        // Mettre à jour les autres champs
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setHeureFin(request.getHeureDebut());
        endpruefung.setHeureFin(request.getHeureDebut());
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setDateExamen(request.getDateExamen());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteLesen(request.getNoteLesen());
        endpruefung.setNoteGrammatik(request.getNoteGrammatik());
        endpruefung.setNoteLesen(request.getNoteLesen());
        // endpruefung.setCommentaire(request.getCommentaire());

        // Recalculer les moyennes
        endpruefung.calculerMoyenneExamen();
        Double moyenneHebdoGlobale = evaluationHebdoService
                .calculerMoyenneHebdoGlobale(endpruefung.getEleve().getId(), endpruefung.getNiveau().getId());
        endpruefung.calculerMoyenneFinale(moyenneHebdoGlobale);

        Endpruefung updatedEndpruefung = endpruefungRepository.save(endpruefung);
        log.info("Endprüfung mis à jour - ID: {}", id);

        return mapToResponse(updatedEndpruefung);
    }

    private EndpruefungResponse mapToResponse(Endpruefung updatedEndpruefung) {
        return new EndpruefungResponse();
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenByResultat(String resultat) {
        Endpruefung.Resultat resultatEnum;
        try {
            resultatEnum = Endpruefung.Resultat.valueOf(resultat.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Résultat invalide: " + resultat);
        }

        return endpruefungRepository.findByResultat(resultatEnum).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EndpruefungResponse validerEndpruefung(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'validerEndpruefung'");
    }

    @Override
    public EndpruefungResponse corrigerEndpruefung(Long id, EndpruefungRequest request) {

        throw new UnsupportedOperationException("Unimplemented method 'corrigerEndpruefung'");
    }

    @Override
    public void annulerEndpruefung(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'annulerEndpruefung'");
    }

    @Override
    public void supprimerEndpruefung(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'supprimerEndpruefung'");
    }

    @Override
    public EndpruefungResponse getEndpruefungById(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getEndpruefungById'");
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenByEleve(Long eleveId) {

        throw new UnsupportedOperationException("Unimplemented method 'getEndpruefungenByEleve'");
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenByNiveau(Long niveauId) {

        throw new UnsupportedOperationException("Unimplemented method 'getEndpruefungenByNiveau'");
    }

    @Override
    public EndpruefungResponse getEndpruefungByEleveAndNiveau(Long eleveId, Long niveauId) {

        throw new UnsupportedOperationException("Unimplemented method 'getEndpruefungByEleveAndNiveau'");
    }

  

    @Override
    public EndpruefungResponse proclamerResultat(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'proclamerResultat'");
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungensManquantes(Long niveauId, Integer semaine, Integer annee) {
//
        throw new UnsupportedOperationException("Unimplemented method 'getEndpruefungensManquantes'");
    }

    @Override
    public List<EndpruefungResponse> getEndpruefungenNonRemplies(Long niveauId, Integer semaine, Integer annee) {

        throw new UnsupportedOperationException("Unimplemented method 'getEndpruefungenNonRemplies'");
    }

    @Override
    public double calculerMoyenneFinale(Long endpruefungId, Long niveauId) {
        //
        throw new UnsupportedOperationException("Unimplemented method 'calculerMoyenneFinale'");
    }

}