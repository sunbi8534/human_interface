package com.human.face.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewExperimentResponse {
    private int id;
    private boolean meaw_first;

    public NewExperimentResponse(int id, boolean meaw_first) {
        this.id = id;
        this.meaw_first = meaw_first;
    }
}
