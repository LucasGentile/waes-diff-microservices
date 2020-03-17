package com.waes.diffservice.repository;

import com.waes.diffservice.model.Diff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiffRepository extends JpaRepository<Diff, Long> {
}