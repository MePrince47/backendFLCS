// entity/Partenaire.java
package FLCS.GESTION.Models;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partenaires")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "eleves")
public class Partenaire extends BaseEntity {
    
    @Column(nullable = false, length = 200)
    private String nom;
    
    @Column(length = 20)
    private String telephone;
    
    private String email;
    
    private String adresse;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Type type;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.ACTIF;
    
    @OneToMany(mappedBy = "partenaire")
    private List<Eleve> eleves = new ArrayList<>();
    
    public enum Type {
        INSTITUTION, ENTREPRISE, INDIVIDU
    }
    
    public enum Statut {
        ACTIF, INACTIF
    }

   
}