package com.human.face.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPosition {

    private String type;
    private int seq;
    private float position_x; // head, left, right, ray
    private float position_y;
    private float position_z;
    private long time_ms;
}
