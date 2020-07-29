package com.waes.diffservice.service;

import com.waes.diffservice.converter.DiffDataConverter;
import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.model.Diff;
import com.waes.diffservice.repository.DiffRepository;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log
@Service
@NoArgsConstructor
public class DiffPersistenceService {

    private DiffRepository diffRepository;

    @Autowired
    public DiffPersistenceService(DiffRepository diffRepository) {
        this.diffRepository = diffRepository;
    }

    /**
     * @param diffData Diff object to be saved
     * @return Saved Diff data object
     */
    @Transactional
    public DiffData save(DiffData diffData) {
        DiffDataValidation.validate(diffData);
        Optional<Diff> persistedDiff = diffRepository.findById(diffData.getId());

        Diff diff = persistedDiff.orElseGet(() -> new Diff(diffData.getId()));

        if (diffData.getLeftSide() != null) {
            diff.setLeftSide(diffData.getLeftSide());
        }
        if (diffData.getRightSide() != null) {
            diff.setRightSide(diffData.getRightSide());
        }

        return DiffDataConverter.convert(diffRepository.save(diff));
    }
}
