package com.waes.diffservice.service;

import com.waes.diffservice.converter.DiffDataConverter;
import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.model.Diff;
import com.waes.diffservice.repository.DiffRepository;
import com.waes.diffservice.validation.DiffServiceValidation;
import javassist.NotFoundException;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log
@Service
@NoArgsConstructor
public class DiffService {

    private DiffRepository diffRepository;
    private DiffInsightService diffInsightService;

    @Autowired
    public DiffService(DiffRepository diffRepository, DiffInsightService diffInsightService) {
        this.diffRepository = diffRepository;
        this.diffInsightService = diffInsightService;
    }

    public DiffData getDiff(Long id) throws NotFoundException {
        Optional<Diff> persistedDiff = diffRepository.findById(id);

        Diff diff = persistedDiff.orElseThrow(() -> new NotFoundException("Diff with id '" + id + "' not found."));

        DiffServiceValidation.validate(id, diff);

        DiffData diffData = DiffDataConverter.convert(diff);
        diffData = diffInsightService.provideDiffInsight(diffData);

        return diffData;
    }
}