

package FLCS.GESTION.Entitees;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "endpruefungen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "eleve", "niveau", "observateur" })
public class Endpruefung extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "niveau_id", nullable = false)
    private Niveau niveau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "observateur_id")
    private Enseignant observateur;

    @Column(name = "date_examen", nullable = false)
    private LocalDate dateExamen;

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    // Notes (0-20)
    @Column(name = "note_lesen")
    private Double noteLesen;

    @Column(name = "note_horen")
    private Double noteHoren;

    @Column(name = "note_schreiben")
    private Double noteSchreiben;

    @Column(name = "note_grammatik")
    private Double noteGrammatik;

    @Column(name = "note_sprechen")
    private Double noteSprechen;

    @Column(name = "moyenne_examen")
    private Double moyenneExamen;

    @Column(name = "moyenne_finale")
    private Double moyenneFinale;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Resultat resultat;

    public enum Resultat {
        ADMIS, AJOURNE, ABSENT
    }

    @PrePersist
    @PreUpdate
    public void calculateMoyennes() {
        double l = Objects.requireNonNullElse(noteLesen, 0.0);
        double h = Objects.requireNonNullElse(noteHoren, 0.0);
        double s = Objects.requireNonNullElse(noteSchreiben, 0.0);
        double g = Objects.requireNonNullElse(noteGrammatik, 0.0);
        double sp = Objects.requireNonNullElse(noteSprechen, 0.0);

        double somme = l + h + s + g + sp;
        this.moyenneExamen = somme / 5.0;

        String nomNiveau = (niveau != null) ? niveau.getNom() : null;
        if (nomNiveau != null) {
            if (nomNiveau.equals("A1") || nomNiveau.equals("A2")) {
                // moyenne finale dépendra de la moyenne hebdo quand fournie
                if (this.moyenneFinale == null) {
                    this.moyenneFinale = this.moyenneExamen;
                }
            } else {
                this.moyenneFinale = this.moyenneExamen;
            }
        }

        if (moyenneFinale != null) {
            this.resultat = (moyenneFinale >= 10.0) ? Resultat.ADMIS : Resultat.AJOURNE;
        }
    }

    public void calculerMoyenneExamen() {
        calculateMoyennes();
    }

    public void calculerMoyenneFinale(Double moyenneHebdoGlobale) {
        if (moyenneHebdoGlobale != null) {
            String nomNiveau = (niveau != null) ? niveau.getNom() : null;
            if (nomNiveau != null && (nomNiveau.equals("A1") || nomNiveau.equals("A2"))) {
                this.moyenneFinale = (moyenneHebdoGlobale * 0.4)
                        + (Objects.requireNonNullElse(this.moyenneExamen, 0.0) * 0.6);
            } else {
                this.moyenneFinale = this.moyenneExamen;
            }
        } else {
            this.moyenneFinale = this.moyenneExamen;
        }
    }

}