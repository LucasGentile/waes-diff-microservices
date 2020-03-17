package com.waes.apigateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.waes.apigateway.client.DiffClient;
import com.waes.apigateway.data.Diff;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

import static com.waes.apigateway.enums.DiffType.NOT_FOUND;

@AllArgsConstructor
@RestController
@RequestMapping("/v1")
class DiffController {

    private final DiffClient diffClient;

    @GetMapping("/diffs")
    @CrossOrigin
    @HystrixCommand(fallbackMethod = "getCollectionFallback")
    public Collection<Diff> allDiffs() {
        return diffClient.getAllDiffs().getBody();
    }

    @GetMapping("/diff/{id}")
    @CrossOrigin
    @HystrixCommand(fallbackMethod = "getEntityFallback")
    public Diff getDiff(@PathVariable Long id) {
        return diffClient.getDiff(id).getBody();
    }

    @PostMapping(path = "/diff/{id}/left", consumes = MediaType.TEXT_PLAIN_VALUE)
    @HystrixCommand(fallbackMethod = "saveLeftSideEntityFallback")
    public Diff saveLeftSide(@PathVariable Long id, @RequestBody String diffContent) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setLeftSide(diffContent);

        return diffClient.saveDiff(diff).getBody();
    }

    @PostMapping(path = "/diff/{id}/right", consumes = MediaType.TEXT_PLAIN_VALUE)
    @HystrixCommand(fallbackMethod = "saveRightSideEntityFallback")
    public Diff saveRightSide(@PathVariable Long id, @RequestBody String diffContent) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setRightSide(diffContent);

        return diffClient.saveDiff(diff).getBody();
    }

    private Collection<Diff> getCollectionFallback() {
        return new ArrayList<>();
    }

    private Diff getEntityFallback(Long id) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setType(NOT_FOUND);
        return diff;
    }

    private Diff saveLeftSideEntityFallback(Long id, String leftSide) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setLeftSide(leftSide);
        return diff;
    }

    private Diff saveRightSideEntityFallback(Long id, String leftSide) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setRightSide(leftSide);
        return diff;
    }
}
