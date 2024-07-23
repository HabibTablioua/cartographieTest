package com.pfa.backend.controller;

import com.pfa.backend.DTO.SecteurDactiviteDTO;
import com.pfa.backend.entity.SecteurDactivite;
import com.pfa.backend.service.SecteurDactiviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secteursDactivite")
@CrossOrigin(origins = "http://localhost:3000")
public class SecteurDactiviteController {

    @Autowired
    private SecteurDactiviteService secteurDactiviteService;

    @GetMapping
    public List<SecteurDactiviteDTO> getAllSecteursDactivite() {
        return secteurDactiviteService.getAllSecteursDactivite();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecteurDactiviteDTO> getSecteurDactiviteById(@PathVariable Long id) {
        return secteurDactiviteService.getSecteurDactiviteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public SecteurDactivite createSecteurDactivite(@RequestBody SecteurDactivite secteurDactivite) {
        return secteurDactiviteService.createSecteurDactivite(secteurDactivite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecteurDactivite(@PathVariable Long id) {
        secteurDactiviteService.deleteSecteurDactivite(id);
        return ResponseEntity.noContent().build();
    }
}
