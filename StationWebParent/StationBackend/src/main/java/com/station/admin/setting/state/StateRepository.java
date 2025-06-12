package com.station.admin.setting.state;

import com.station.common.entity.Country;
import com.station.common.entity.State;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);
}
