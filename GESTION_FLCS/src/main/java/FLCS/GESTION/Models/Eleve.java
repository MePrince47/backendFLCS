// entity/Eleve.java
package FLCS.GESTION.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eleves")
// @Getter @Setter @NoArgsConstructor @AllArgsConstructor
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

    public enum StatutPaiement {
        NON_PAYE, PARTIEL, PAYE
    }

    public enum Statut {
        INSCRIT, EN_FORMATION, EN_PROCEDURE, VISA_OBTENU, ABANDON
    }

    public Double getSoldeDu() {
        return montantTotalRequis - montantTotalPaye;
    }

    public Double getPourcentagePaiement() {
        if (montantTotalRequis == 0)
            return 0.0;
        return (montantTotalPaye / montantTotalRequis) * 100;
    }

}