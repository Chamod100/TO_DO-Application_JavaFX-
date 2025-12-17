package org.example.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private int id;
    private String title;
    private String date;
    private String description;
}
