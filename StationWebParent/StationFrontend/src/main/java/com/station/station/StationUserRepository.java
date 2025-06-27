package com.station.station;

import com.station.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationUserRepository extends JpaRepository<User, Integer> {
}
