package FLCS.GESTION.DTO;

import FLCS.GESTION.ENTITEES.Role;

public record UtilisateurResponse(
    Long id,
    String username,
    Role role
) {}
