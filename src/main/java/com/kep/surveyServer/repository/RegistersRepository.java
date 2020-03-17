package com.kep.surveyServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Registers;

@Repository
public interface RegistersRepository extends JpaRepository<Registers, Long>{

}
