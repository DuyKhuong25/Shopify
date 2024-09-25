package com.ecom.admin.reponsitory.user;

import com.ecom.admin.repository.user.UserRepository;
import com.ecom.common.entity.Role;
import com.ecom.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User user = new User("admin@gmail.com", "123456789", "Duy", "Khuong", true);
        user.addRole(roleAdmin);
        System.out.println(user);

        repo.save(user);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User user = new User("hoan@gmail.com", "hoang123", "Hoang", "Pham");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        user.addRole(roleEditor);
        user.addRole(roleAssistant);

        System.out.println(user.getRoles());

        System.out.println(user);

        User savedUser = repo.save(user);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User user = repo.findById(1).get();
        System.out.println(user);
    }

    @Test
    public void testUpdateUserDetails() {
        User user = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("ndkhuong@gmail.com");

        repo.save(user);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesperson);
        System.out.println(user);

        repo.save(user);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repo.deleteById(userId);
    }
}
