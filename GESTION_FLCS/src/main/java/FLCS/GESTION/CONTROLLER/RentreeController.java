package FLCS.GESTION.CONTROLLER;

import FLCS.GESTION.ENTITEES.Rentree;
import FLCS.GESTION.SERVICE.RentreeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentrees")
public class RentreeController {

    private final RentreeService service;

    public RentreeController(RentreeService service) {
        this.service = service;
    }

    @PostMapping
    public Rentree create(@RequestBody Rentree rentree) {
        return service.createRentree(rentree);
    }

    @GetMapping
    public List<Rentree> findAll() {
        return service.getAllRentrees();
    }
}
