package com.tq.hospitalequipmenttracking.repository;

import com.tq.hospitalequipmenttracking.model.EquipmentMovementHistory;
import com.tq.hospitalequipmenttracking.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
