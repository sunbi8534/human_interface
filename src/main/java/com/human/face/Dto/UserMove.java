package com.human.face.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMove {
    private float ray_x;
    private float ray_y;
    private float ray_z;
    private boolean ray_reached;
    private boolean ray_selected;
    private long time_ms;
}
