package com.station.admin.user;

import static org.junit.jupiter.api.Assertions.*;

import com.station.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void TestCreateRole() {
        Role ADMIN = new Role();
        ADMIN.setName("ADMIN");
        ADMIN.setDescription("Manage Everything");
        Role savedRole = roleRepository.save(ADMIN);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    public void testCreateRestRoles() {
        Role r2 = new Role("SalesPerson", "Manage Sales");
        Role r3 = new Role("Editor", "Edit Sales");
        Role r4 = new Role("Assistant", "Manage reviews");

        roleRepository.saveAll(Arrays.asList(r2, r3, r4));

    }
}
