package com.pfa.backend.controller;

import com.pfa.backend.DTO.FormeJuridiqueDTO;
import com.pfa.backend.entity.FormeJuridique;
import com.pfa.backend.service.FormeJuridiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/formesJuridiques")
public class FormeJuridiqueController {

    @Autowired
    private FormeJuridiqueService formeJuridiqueService;

    @GetMapping
    public List<FormeJuridiqueDTO> getAllFormesJuridiques() {
        return formeJuridiqueService.getAllFormesJuridiques();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormeJuridiqueDTO> getFormeJuridiqueById(@PathVariable Long id) {
        return formeJuridiqueService.getFormeJuridiqueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public FormeJuridique createFormeJuridique(@RequestBody FormeJuridique formeJuridique) {
        return formeJuridiqueService.createFormeJuridique(formeJuridique);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormeJuridique(@PathVariable Long id) {
        formeJuridiqueService.deleteFormeJuridique(id);
        return ResponseEntity.noContent().build();
    }
}
