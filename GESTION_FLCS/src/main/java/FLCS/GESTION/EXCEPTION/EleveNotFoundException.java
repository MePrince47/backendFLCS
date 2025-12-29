package FLCS.GESTION.EXCEPTION;

public class EleveNotFoundException extends RuntimeException {

    public EleveNotFoundException(Long id) {
        super("Élève avec l'id " + id + " introuvable");
    }
}
