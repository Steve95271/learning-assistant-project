package com.steve.learning_assisstant.repository;

import com.steve.learning_assisstant.model.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("""
                SELECT *
                FROM users
                WHERE email = :email
                AND deleted_at IS NULL
            """)
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
                SELECT *
                FROM users
                WHERE id = :id
                AND account_status = 'active'
                AND deleted_at IS NULL
            """)
    Optional<User> findActiveUserById(@Param("id") Long id);

}
