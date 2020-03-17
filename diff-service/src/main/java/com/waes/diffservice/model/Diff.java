package com.waes.diffservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diff {
    @Id
    private Long id;

    @Lob
    private String leftSide;

    @Lob
    private String rightSide;
}
