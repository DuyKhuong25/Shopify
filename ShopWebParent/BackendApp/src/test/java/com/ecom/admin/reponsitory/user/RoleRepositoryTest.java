package com.ecom.admin.reponsitory.user;

import com.ecom.admin.repository.user.RoleRepository;
import com.ecom.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole(){
        Role admin = new Role("Admin", "Manage everything");
        Role sale = new Role("Sale", "Sale person");
        Role editor = new Role("Editor", "Editor content");
        Role shipper = new Role("Shipper", "Shipper order");
        Role assistant = new Role("Assistant", "Assistant questions & reviews");

        roleRepository.saveAll(List.of(admin, sale, editor, shipper, assistant));
    }
}
