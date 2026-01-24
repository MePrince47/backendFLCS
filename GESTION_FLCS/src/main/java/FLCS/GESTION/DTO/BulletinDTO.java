package FLCS.GESTION.DTO;

import java.util.List;

public record BulletinDTO(

    EleveResponse eleve,
    String niveau,
    String rentree,

    List<NoteResponse> notes,   // TOUTES les notes

    Double soutenance,        // null si A1 / B1
    Double moyenneGenerale,
    boolean admis
) {}
