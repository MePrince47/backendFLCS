// entity/Endpruefung.java
package FLCS.GESTION.Models;

// import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "endpruefungen")
// @Getter @Setter @NoArgsConstructor @AllArgsConstructor
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
    
    @SuppressWarnings("unlikely-arg-type")
    @PrePersist
    @PreUpdate
    public void calculateMoyennes() {
        // Calcul de la moyenne de l'examen
        double somme = noteLesen + noteHoren + noteSchreiben + noteGrammatik + noteSprechen;
        this.moyenneExamen = somme / 5;
        
        // Calcul de la moyenne finale selon le niveau
        if (niveau != null) {
            Long nomNiveau = niveau.getId();
            if (nomNiveau.equals("A1") || nomNiveau.equals("A2")) {
                // A1/A2: 40% moyenne hebdo + 60% examen
                // Note: On suppose que la moyenne hebdo est calculée ailleurs
                this.moyenneFinale = this.moyenneExamen; // À adapter
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
}