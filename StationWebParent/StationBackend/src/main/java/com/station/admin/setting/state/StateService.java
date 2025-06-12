package com.station.admin.setting.state;

import com.station.common.entity.Country;
import com.station.common.entity.State;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    @Transactional
    public State updateState(Integer id, String newName, Country country) {
        State existing = stateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("State not found"));

        existing.setName(newName);
        existing.setCountry(country);

        return stateRepository.save(existing);
    }

}
