package com.example.finalproject.repositories;

import com.example.finalproject.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin (String login);

   @Modifying
   @Query(value = "UPDATE person SET password = ?2 WHERE id= ?1", nativeQuery = true)
   void updatePersonById(int id, String password);

}
