package com.waes.diffservice.controller;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.service.DiffPersistenceService;
import com.waes.diffservice.service.DiffService;
import javassist.NotFoundException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Level;

@Log
@RestController
@RequestMapping("/diffs")
public class DiffController {

    private final DiffService diffService;
    private final DiffPersistenceService diffPersistenceService;

    @Autowired
    public DiffController(DiffService diffService, DiffPersistenceService diffPersistenceService) {
        this.diffService = diffService;
        this.diffPersistenceService = diffPersistenceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiff(@PathVariable Long id) {
        DiffData diffData;
        try {
            diffData = diffService.getDiff(id);
            return new ResponseEntity<>(diffData, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveOrUpdate(@RequestBody DiffData diffData, UriComponentsBuilder ucBuilder) {
        DiffData savedDiff;
        try {
            savedDiff = diffPersistenceService.save(diffData);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/diffs/{id}").buildAndExpand(savedDiff.getId()).toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
