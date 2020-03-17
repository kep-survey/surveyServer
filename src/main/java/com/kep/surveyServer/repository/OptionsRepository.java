package com.kep.surveyServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Options;

@Repository
public interface OptionsRepository extends JpaRepository<Options, Long>{

}
