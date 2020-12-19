package UnitTests.Domain.User;

import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.User.Credentials;
import Model.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestInMemoryITradeRepository {

    User userA = new User(new Credentials("user", "pw"));

    IUserRepository userRepository = new InMemoryUserRepository();

    @BeforeEach()
    void cleanUpRepo() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    @DisplayName("Create User")
    void testCreateUser() {
        // arrange
        // act
        Long id = userRepository.persistEntity(userA);

        // assert
        assertEquals(1, id);
    }

    @Test
    @DisplayName("Find User")
    void testFindUser() {
        // arrange
        userRepository.persistEntity(userA);
        // act
        User foundUser = userRepository.findEntity(1L);

        // assert
        assertEquals(userA, foundUser);
    }

    @Test
    @DisplayName("Update User")
    void testUpdateUser() {
        // arrange
        userRepository.persistEntity(userA);
        userA.getCredentials().setUsername("newName");
        // act
        boolean works = userRepository.updateEntity(userA);
        User foundUser = userRepository.findEntity(1L);

        // assert
        assertTrue(works);
        assertEquals(userA.getCredentials().getUsername(), foundUser.getCredentials().getUsername());

    }

    @Test
    @DisplayName("Update User Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        userA.getCredentials().setUsername("newName");
        // act
        boolean works = userRepository.updateEntity(userA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete User")
    void testDeleteUser() {
        // arrange
        Long id = userRepository.persistEntity(userA);

        // act
        boolean works = userRepository.deleteEntity(id);
        User foundUser = userRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Delete User Wrong ID")
    void testDeleteUserWrongId() {
        // arrange

        // act
        boolean works = userRepository.deleteEntity(0L);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Login")
    void testLogin() {
        // arrange
        userRepository.persistEntity(userA);
        // act
        String token = userRepository.loginUser(userA.getCredentials());

        // assert
        assertEquals("Basic " + userA.getCredentials().getUsername() + "-mtcgToken", token);
    }

    @Test
    @DisplayName("Login Wrong Credentials")
    void testLoginWrongCredentials() {
        // arrange
        // act
        String token = userRepository.loginUser(userA.getCredentials());

        // assert
        assertNull(token);
    }


    @Test
    @DisplayName("get User With Token")
    void testGetUserWithToken() {
        userRepository.persistEntity(userA);
        // act
        String token = userRepository.loginUser(userA.getCredentials());
        User user = userRepository.getUserWithToken("Basic " + userA.getCredentials().getUsername() + "-mtcgToken");

        // assert
        assertEquals(userA, user);
    }

    @Test
    @DisplayName("get User With Username")
    void testGetUserWithUsername() {
        userRepository.persistEntity(userA);
        // act
        User user = userRepository.getUserWithUsername("user");

        // assert
        assertEquals(userA, user);
    }

}