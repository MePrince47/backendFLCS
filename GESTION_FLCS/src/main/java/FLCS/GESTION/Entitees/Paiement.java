package FLCS.GESTION.Entitees;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Représente un paiement effectué pour un élève.
 * Contient le montant, la référence, le mode et la date du paiement.
 */
@Entity
@Table(name = "paiements")
public class Paiement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partenaire_id")
    private Partenaire partenaire;

    @Column(unique = true, length = 50)
    private String reference;

    private Double montant;

    @Column(name = "date_paiement")
    private LocalDate datePaiement = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement", length = 30)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.VALIDE;

    public enum ModePaiement {
        ESPECES, CHEQUE, VIREMENT, MOBILE_MONEY
    }

    public enum Statut {
        VALIDE, ANNULE
    }

    // @PostPersist
    // @PostUpdate
    // public void updateElevePaiement() {
    // if (eleve != null && statut == Statut.VALIDE) {
    // double totalPaye = eleve.getPaiements().stream()
    // .filter(p -> p.getStatut() == Statut.VALIDE)
    // .mapToDouble(Paiement::getMontant)
    // .sum();

    // eleve.setMontantTotalPaye(totalPaye);

    // if (totalPaye >= eleve.getMontantTotalRequis()) {
    // eleve.setStatutPaiement(Eleve.StatutPaiement.PAYE);
    // } else if (totalPaye > 0) {
    // eleve.setStatutPaiement(Eleve.StatutPaiement.PARTIEL);
    // } else {
    // eleve.setStatutPaiement(Eleve.StatutPaiement.NON_PAYE);
    // }
    // }
    // // }
}