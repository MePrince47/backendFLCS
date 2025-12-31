
package FLCS.GESTION.Models;

// import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

import FLCS.GESTION.Dtos.response.EndpruefungResponse;

@Entity
@Table(name = "endpruefungen")
// @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Endpruefung extends BaseEntity {

    public static final String StatutEndpruefung = null;

    private static final String Mention = null;

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

    // @Enumerated(EnumType.STRING)
    // @Column(name = "mention", length = 50)
    // private Mention mention;
    // // private Object mention;

    public enum Resultat {
        ADMIS, AJOURNE, ABSENT
    }
    // public enum Mention {
    // TRES_BIEN, BIEN, ASSEZ_BIEN, PASSABLE, INSUFFISANT
    // }

    @PrePersist
    @PreUpdate
    public void calculateMoyennes() {
        // Calcul de la moyenne de l'examen
        double somme = noteLesen + noteHoren + noteSchreiben + noteGrammatik + noteSprechen;
        this.moyenneExamen = somme / 5;

        // Calcul de la moyenne finale selon le niveau (nom du niveau)
        String nomNiveau = (niveau != null) ? niveau.getNom() : null;
        if (nomNiveau != null) {
            if (nomNiveau.equals("A1") || nomNiveau.equals("A2")) {
                // A1/A2: moyenne finale dépendra de la moyenne hebdo (calculée ailleurs)
                this.moyenneFinale = this.moyenneExamen; // placeholder
            } else if (nomNiveau.equals("B1") || nomNiveau.equals("B2")) {
                // B1/B2: 100% examen
                this.moyenneFinale = this.moyenneExamen;
            }
        }

        // Déterminer le résultat
        if (moyenneFinale != null) {
            if (moyenneFinale >= 10) {
                this.resultat = Resultat.ADMIS;
            } else {
                this.resultat = Resultat.AJOURNE;
            }
        }
    }

    public void setEleve(Eleve eleve2) {
        this.eleve = eleve2;

    }
       public Eleve getEleve() {
        return this.eleve;
    }

    public void setNiveau(Niveau niveau2) {
        this.niveau = niveau2;
    }

    public void setDateExamen(Object dateExamen2) {
        this.dateExamen = (LocalDate) dateExamen2;
    }

    public void setHeureFin(Object heureDebut) {
        this.dateExamen = (LocalDate) heureDebut;
    }

    public void setNoteLesen(Object noteLesen2) {
        this.noteLesen = (Double) noteLesen2;
    }

    public void setNoteGrammatik(Object noteGrammatik2) {
        this.noteGrammatik = (Double) noteGrammatik2;
    }

    public void setObservateur(Enseignant observateur2) {
        this.observateur = observateur2;
    }

    public void calculerMoyenneExamen() {
        calculateMoyennes();

    }


  


    public void calculerMoyenneFinale(Double moyenneHebdoGlobale) {
        if (moyenneHebdoGlobale != null) {
            String nomNiveau = (niveau != null) ? niveau.getNom() : null;
            if (nomNiveau != null && (nomNiveau.equals("A1") || nomNiveau.equals("A2"))) {
                this.moyenneFinale = (moyenneHebdoGlobale * 0.4) + (this.moyenneExamen * 0.6);
            } else {
                this.moyenneFinale = this.moyenneExamen;
            }
        } else {
            this.moyenneFinale = this.moyenneExamen;
        }
    }

    public Niveau getNiveau() {
        return this.niveau;
       
    }

}