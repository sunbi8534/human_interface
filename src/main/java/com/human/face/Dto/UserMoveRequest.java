package com.human.face.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMoveRequest {
    private float[] ray_xyz;
    private boolean ray_reached;
    private boolean ray_selected;
    private long time_ms;
}
