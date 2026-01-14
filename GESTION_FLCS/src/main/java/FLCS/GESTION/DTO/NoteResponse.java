package FLCS.GESTION.DTO;

import FLCS.GESTION.ENTITEES.TypeNote;

public record NoteResponse(

    Long eleveId,
    String nomEleve,

    Long niveauId,
    String niveauCode,

    Integer semaine,          // null pour ENDPRUFUNG
    TypeNote type,           // HEBDO | ENDPRUFUNG
    Double les,
    Double hor,
    Double schreib,
    Double gramm,
    Double spre
) {}
