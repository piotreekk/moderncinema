package pl.piotrek.cinemabackend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.repository.UserRepository;
//import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository employeeRepository;

    @Test
    public void addUserTest(){
        User user = new User();
        user.setFirstName("Piotrek");
        user.setLastName("Strumnik");
        user.setEmail("piotrekstrumnik@gmail.com");
        user.setPassword("piotrek123");

        User persisted = entityManager.persist(user);

        assertEquals("User persisted and user are not the same", persisted.getEmail(), user.getEmail());
    }
}
