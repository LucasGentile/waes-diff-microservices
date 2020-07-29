package com.waes.diffservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiffInsightData {
    private Integer offset;
    private Integer size;
}
