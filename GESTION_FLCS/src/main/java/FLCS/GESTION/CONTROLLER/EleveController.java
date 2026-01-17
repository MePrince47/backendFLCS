package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Eleve;
import FLCS.GESTION.SERVICE.EleveService;
import FLCS.GESTION.DTO.SearchResponse;
import FLCS.GESTION.DTO.EleveResponse;
import FLCS.GESTION.DTO.EleveRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;



import java.util.List;

@RestController
@RequestMapping("/api/eleves")
@CrossOrigin(origins = "http://localhost:4200")
public class EleveController {

    private final EleveService eleveService;

    public EleveController(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PostMapping
    public ResponseEntity<EleveResponse> create(
            @Valid @RequestBody EleveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eleveService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping
    public List<EleveResponse> getAll() {
        return eleveService.getAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/{id}")
    public EleveResponse getById(@PathVariable Long id) {
        return eleveService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @PutMapping("/{id}")
    public ResponseEntity<EleveResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EleveRequest request
    ) {
        return ResponseEntity.ok(eleveService.update(id, request));
    }


    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eleveService.delete(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/search")
    public ResponseEntity<SearchResponse<EleveResponse>> rechercheAvancee(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String niveauScolaire,
            @RequestParam(required = false) String rentree,
            @RequestParam(required = false) String niveauLangue,
            @RequestParam(required = false) String partenaire
    ) {
        return ResponseEntity.ok(
                eleveService.rechercheAvancee(
                        nom, niveauScolaire, rentree, niveauLangue, partenaire
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/niveau/{niveauId}")
    public List<EleveResponse> getByNiveau(@PathVariable Long niveauId) {
        return eleveService.getByNiveau(niveauId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SECRETAIRE','ENSEIGNANT')")
    @GetMapping("/rentree/{rentreeId}")
    public List<EleveResponse> getByRentree(@PathVariable Long rentreeId) {
        return eleveService.getByRentree(rentreeId);
    }



}
