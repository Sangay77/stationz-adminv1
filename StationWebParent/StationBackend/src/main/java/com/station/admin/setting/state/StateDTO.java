package com.station.admin.setting.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StateDTO {

    private Integer id;
    private String name;

    public StateDTO(Integer id, String name) {
        this.id=id;
        this.name=name;
    }
}
