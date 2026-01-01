package FLCS.GESTION.Entitees;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un élève inscrit dans le système.
 * <p>
 * Cette entité contient les informations personnelles (nom, prénom, contacts),
 * les informations de scolarité (niveau, rentrée) et les relations vers
 * les évaluations, paiements et examens finaux.
 * </p>
 */
@Entity
@Table(name = "eleves")
@Getter
@Setter
@ToString(exclude = { "rentree", "niveau", "partenaire", "evaluations", "endpruefungen", "paiements" })
public class Eleve extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String matricule;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(length = 20)
    private String telephone;

    /** Téléphone du parent ou tuteur principal. */
    @Column(name = "telephone_parent", length = 20)
    private String telephoneParent;

    private String email;

    @Column(name = "niveau_scolaire")
    private String niveauScolaire;

    @Column(name = "type_procedure")
    private String typeProcedure;

    @Column(name = "niveau_actuel", length = 10)
    private String niveauActuel = "A1";

    @Column(name = "montant_total_requis")
    private Double montantTotalRequis = 0.0;

    @Column(name = "montant_total_paye")
    private Double montantTotalPaye = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement", length = 20)
    private StatutPaiement statutPaiement = StatutPaiement.NON_PAYE;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.EN_FORMATION;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rentree_id")
    private Rentree rentree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "niveau_id")
    private Niveau niveau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partenaire_id")
    private Partenaire partenaire;

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<EvaluationHebdomadaire> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Endpruefung> endpruefungen = new ArrayList<>();

    @OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)
    private List<Paiement> paiements = new ArrayList<>();

    /** Statut du paiement pour un élève. */
    public enum StatutPaiement {
        NON_PAYE, PARTIEL, PAYE
    }

    public enum Statut {
        INSCRIT, EN_FORMATION, EN_PROCEDURE, VISA_OBTENU, ABANDON
    }

    /**
     * Retourne le solde restant dû par l'élève.
     */
    public Double getSoldeDu() {
        return montantTotalRequis - montantTotalPaye;
    }

    /**
     * Retourne le pourcentage payé par rapport au montant requis (0-100).
     */
    public Double getPourcentagePaiement() {
        if (montantTotalRequis == null || montantTotalRequis == 0)
            return 0.0;
        return (montantTotalPaye / montantTotalRequis) * 100;
    }

}