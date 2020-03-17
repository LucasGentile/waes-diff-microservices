package com.waes.diffservice.controller;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.service.DiffService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/diffs")
@AllArgsConstructor
public class DiffController {

    private DiffService diffService;

    @GetMapping
    public Collection<DiffData> getAll() {
        return diffService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiffData> getDiff(@PathVariable Long id) {
        DiffData diffData = null;
        try {
            diffData = diffService.getDiff(id);
            return new ResponseEntity<>(diffData, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(diffData, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<DiffData> saveOrUpdate(@RequestBody DiffData diff) {
        DiffData savedDiff = null;
        try {
            savedDiff = diffService.save(diff.getId(), diff);

            return new ResponseEntity<>(savedDiff, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(savedDiff, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
