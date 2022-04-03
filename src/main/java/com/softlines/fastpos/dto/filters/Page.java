package com.softlines.fastpos.dto.filters;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Page<T> {
    int size;
    List<T> elements;
    @Nullable
    Long totalPages;


    public  boolean isEmpty(){
        return  elements.isEmpty();
    }


    public <R> Page<R> mapToPage(Function<List<T>, List<R>> mapper) {
        return new Page<>(size,mapper.apply(elements), totalPages);
    }
}
