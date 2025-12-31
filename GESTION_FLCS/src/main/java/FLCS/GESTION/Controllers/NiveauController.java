package FLCS.GESTION.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import FLCS.GESTION.Dtos.response.NiveauResponse;
import FLCS.GESTION.Services.NiveauService;
import jakarta.validation.Valid;

public class NiveauController {
     private final NiveauService niveauService;
    
    public NiveauController(NiveauService niveauService) {
        this.niveauService = niveauService;
    }
    
    @PostMapping
    public ResponseEntity<NiveauResponse> createNiveau(@Valid @RequestBody NiveauResponse NiveauResponse) {
        NiveauResponse createdNiveau = niveauService.createNiveau(NiveauResponse);
        return new ResponseEntity<>(createdNiveau, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NiveauResponse> getNiveauById(@PathVariable Long id) {
        NiveauResponse niveau = niveauService.getNiveauById(id);
        return ResponseEntity.ok(niveau);
    }
    
    @GetMapping
    public ResponseEntity<List<NiveauResponse>> getAllNiveaux() {
        List<NiveauResponse> niveaux = niveauService.getAllNiveaux();
        return ResponseEntity.ok(niveaux);
    }
    
    @GetMapping("/rentree/{rentreeId}")
    public ResponseEntity<List<NiveauResponse>> getNiveauxByRentree(@PathVariable Long rentreeId) {
        List<NiveauResponse> niveaux = niveauService.getNiveauxByRentree(rentreeId);
        return ResponseEntity.ok(niveaux);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<NiveauResponse> updateNiveau(@PathVariable Long id, @Valid @RequestBody NiveauResponse NiveauResponse) {
        NiveauResponse updatedNiveau = niveauService.updateNiveau(id, NiveauResponse);
        return ResponseEntity.ok(updatedNiveau);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        niveauService.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

}
