package com.waes.apigateway.controller;

import com.waes.apigateway.client.DiffClient;
import com.waes.apigateway.data.Diff;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/v1")
class DiffController {

    private final DiffClient diffClient;

    @GetMapping("/diff/{id}")
    public Diff getDiff(@PathVariable Long id) {
        return diffClient.getDiff(id).getBody();
    }

    @PostMapping(path = "/diff/{id}/left", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Diff saveLeftSide(@PathVariable Long id, @RequestBody String diffContent) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setLeftSide(diffContent);

        return diffClient.saveDiff(diff).getBody();
    }

    @PostMapping(path = "/diff/{id}/right", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Diff saveRightSide(@PathVariable Long id, @RequestBody String diffContent) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setRightSide(diffContent);

        return diffClient.saveDiff(diff).getBody();
    }
}
