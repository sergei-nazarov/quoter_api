package ru.sergeiproject.quoter.data;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteDto {
    @Size(min = 1, message = "content can't be empty")
    private String content;
}
