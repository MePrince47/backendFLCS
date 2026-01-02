package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.SERVICE.PartenaireService;
import FLCS.GESTION.DTO.PartenaireRequest;
import FLCS.GESTION.DTO.PartenaireResponse;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
public class PartenaireController {

    private final PartenaireService partenaireService;

    public PartenaireController(PartenaireService partenaireService) {
        this.partenaireService = partenaireService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PartenaireResponse> create(
            @RequestBody PartenaireRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partenaireService.create(request));
    }

    @GetMapping
    public List<PartenaireResponse> getAll() {
        return partenaireService.getAll();
    }

    @GetMapping("/{id}")
    public PartenaireResponse getById(@PathVariable Long id) {
        return partenaireService.getById(id);
    }
}


