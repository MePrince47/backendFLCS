// entity/Utilisateur.java
package FLCS.GESTION.Models;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Utilisateur extends BaseEntity {
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.ACTIF;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;
    
    public enum Role {
        ADMIN, DIRECTION, ENSEIGNANT, SECRETAIRE
    }
    
    public enum Statut {
        ACTIF, INACTIF
    }
}