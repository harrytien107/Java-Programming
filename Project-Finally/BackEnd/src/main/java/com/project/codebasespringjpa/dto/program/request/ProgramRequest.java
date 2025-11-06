package com.project.codebasespringjpa.dto.program.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgramRequest {
    Long id;

    String title;
    String image;
    String address;
    LocalDate date;
    Integer hourse;
    Integer minus;
    String status;
    Long capacity;
    String description;
}
