package com.station.station;


import com.station.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationUserRepository userRepository;

    public List<User> stationTeam() {
        return userRepository.findAll();
    }
}
