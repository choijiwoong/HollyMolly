package com.holly.molly.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @AllArgsConstructor @Setter
public class NearRequestListElementDTO {
    Long id;
    Double distance;
    String address;
}
