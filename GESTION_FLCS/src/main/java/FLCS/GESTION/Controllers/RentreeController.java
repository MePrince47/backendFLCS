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

import FLCS.GESTION.Dtos.Request.RentreeRequest;
import FLCS.GESTION.Dtos.response.ApiResponse;
import FLCS.GESTION.Dtos.response.RentreeResponse;
import FLCS.GESTION.Services.RentreeService;
import jakarta.validation.Valid;

public class RentreeController {

    private final RentreeService rentreeService = null;

    @PostMapping
    public ResponseEntity<ApiResponse<RentreeResponse>> createRentree(
            @Valid @RequestBody RentreeRequest request) {
        RentreeResponse response = rentreeService.createRentree(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rentrée créée avec succès", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentreeResponse>> updateRentree(
            @PathVariable Long id,
            @Valid @RequestBody RentreeRequest request) {
        RentreeResponse response = rentreeService.updateRentree(id, request);
        return ResponseEntity.ok(ApiResponse.success("Rentrée mise à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRentree(@PathVariable Long id) {
        rentreeService.deleteRentree(id);
        return ResponseEntity.ok(ApiResponse.success("Rentrée supprimée avec succès", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentreeResponse>> getRentreeById(@PathVariable Long id) {
        RentreeResponse response = rentreeService.getRentreeById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<RentreeResponse>> getRentreeByCode(@PathVariable String code) {
        RentreeResponse response = rentreeService.getRentreeByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RentreeResponse>>> getAllRentrees() {
        List<RentreeResponse> responses = rentreeService.getAllRentrees();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<ApiResponse<List<RentreeResponse>>> getRentreesByStatut(
            @PathVariable String statut) {
        List<RentreeResponse> responses = rentreeService.getRentreesByStatut(statut);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping("/{id}/niveaux")
    public ResponseEntity<ApiResponse<Void>> creerNiveaux(@PathVariable Long id) {
        rentreeService.creerNiveauxPourRentree(id);
        return ResponseEntity.ok(ApiResponse.success("Niveaux créés avec succès", null));
    }

}
