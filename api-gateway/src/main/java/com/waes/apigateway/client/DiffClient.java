package com.waes.apigateway.client;

import com.waes.apigateway.data.Diff;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@FeignClient("diff-service")
public interface DiffClient {

    @GetMapping("/diffs")
    @CrossOrigin
    ResponseEntity<Collection<Diff>> getAllDiffs();

    @GetMapping("/diffs/{id}")
    @CrossOrigin
    ResponseEntity<Diff> getDiff(@PathVariable Long id);

    @PostMapping("/diffs")
    ResponseEntity<Diff> saveDiff(@RequestBody Diff diff);

}
