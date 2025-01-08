package com.db.gourmetguide.repository;

import com.db.gourmetguide.exception.UserNotFoundException;
import com.db.gourmetguide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username) throws UserNotFoundException;
    User getUserById(int id);
}
