package com.station.common.dto;

import com.station.common.entity.Role;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;


    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String photos;
    private boolean enabled;
    private Set<Role> roles = new HashSet<>();

    @Transient
    public String getPhotosImagePath() {
        if (photos == null || photos.isEmpty()) {
            return "/images/default-user.png";
        }
        return "/user-photos/" + this.id + "/" + this.photos;
    }

}

