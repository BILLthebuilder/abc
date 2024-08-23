package com.abcltd.dto;

import jakarta.validation.constraints.NotEmpty;

public record BookDto(
        @NotEmpty(message = "title cannot be blank")
         String title,
        @NotEmpty(message = "author cannot be blank")
        String author,

        Integer yearz

) {
}
