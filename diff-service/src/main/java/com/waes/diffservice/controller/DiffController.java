package com.waes.diffservice.controller;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.service.DiffService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/diffs")
public class DiffController {

    private DiffService diffService;

    @Autowired
    public DiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiff(@PathVariable Long id) {
        DiffData diffData;
        try {
            diffData = diffService.getDiff(id);
            return new ResponseEntity<>(diffData, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveOrUpdate(@RequestBody DiffData diff, UriComponentsBuilder ucBuilder) {
        DiffData savedDiff;
        try {
            savedDiff = diffService.save(diff.getId(), diff);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/diffs/{id}").buildAndExpand(savedDiff.getId()).toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
