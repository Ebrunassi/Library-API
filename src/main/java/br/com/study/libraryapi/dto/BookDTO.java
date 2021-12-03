package br.com.study.libraryapi.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String isbn;
}
