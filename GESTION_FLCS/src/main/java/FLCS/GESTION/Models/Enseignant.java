// entity/Enseignant.java
package FLCS.GESTION.Models;
import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enseignants")
// @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"niveaux", "evaluations", "endpruefungen"})
public class Enseignant extends BaseEntity {
    
    @Column(unique = true, length = 20)
    private String matricule;
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    @Column(nullable = false, length = 100)
    private String prenom;
    
    @Column(unique = true)
    private String email;
    
    @Column(length = 20)
    private String telephone;
    
    private String matiere = "Allemand";
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.ACTIF;
    
    @OneToMany(mappedBy = "enseignant")
    private List<Niveau> niveaux = new ArrayList<>();
    
    @OneToMany(mappedBy = "enseignant")
    private List<EvaluationHebdomadaire> evaluations = new ArrayList<>();
    
    @OneToMany(mappedBy = "observateur")
    private List<Endpruefung> endpruefungen = new ArrayList<>();
    
    @OneToOne(mappedBy = "enseignant")
    private Utilisateur utilisateur;
    
    public enum Statut {
        ACTIF, INACTIF, CONGE
    }
}