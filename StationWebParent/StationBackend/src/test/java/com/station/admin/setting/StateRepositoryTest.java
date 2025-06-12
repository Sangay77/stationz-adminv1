package com.station.admin.setting;

import com.station.admin.setting.state.StateRepository;
import com.station.common.entity.Country;
import com.station.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class StateRepositoryTest {

    @Autowired private StateRepository stateRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateStateInBhutan(){
        Integer countryId=3;
        Country country=entityManager.find(Country.class,countryId);
        stateRepository.save(new State("PERTH",country));
    }

}