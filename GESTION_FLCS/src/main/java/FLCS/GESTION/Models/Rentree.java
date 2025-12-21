// entity/Rentree.java
package FLCS.GESTION.Models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rentrees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"niveaux", "eleves"})
public class Rentree extends BaseEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    private String description;
    
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;
    
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;
    
    @Column(name = "nombre_places_max")
    private Integer nombrePlacesMax = 30;
    
    @Column(name = "nombre_places_prises")
    private Integer nombrePlacesPrises = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut = Statut.PLANIFIEE;
    
    @OneToMany(mappedBy = "rentree", cascade = CascadeType.ALL)
    private List<Niveau> niveaux = new ArrayList<>();
    
    @OneToMany(mappedBy = "rentree", cascade = CascadeType.ALL)
    private List<Eleve> eleves = new ArrayList<>();
    
    public enum Statut {
        PLANIFIEE, EN_COURS, TERMINEE, ANNULEE
    }
    
    @PrePersist
    @PreUpdate
    public void validateDates() {
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
    }
}