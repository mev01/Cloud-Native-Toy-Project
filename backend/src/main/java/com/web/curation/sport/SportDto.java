package com.web.curation.sport;

import lombok.*;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Valid
@Builder
public class SportDto {
    private int sportId;
    private String name;
}
