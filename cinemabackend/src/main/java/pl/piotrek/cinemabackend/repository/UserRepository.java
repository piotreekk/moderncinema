package pl.piotrek.cinemabackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrek.cinemabackend.model.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findAllByRoleAndEnabledTrue(String role);
    List<User> findAllByRole(String role);

}
