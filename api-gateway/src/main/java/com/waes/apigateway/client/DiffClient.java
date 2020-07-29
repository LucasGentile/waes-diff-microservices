package com.waes.apigateway.client;

import com.waes.apigateway.data.Diff;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("diff-service")
public interface DiffClient {

    @GetMapping("/diffs/{id}")
    ResponseEntity<Diff> getDiff(@PathVariable Long id);

    @PostMapping("/diffs")
    ResponseEntity<Diff> saveDiff(@RequestBody Diff diff);

}
