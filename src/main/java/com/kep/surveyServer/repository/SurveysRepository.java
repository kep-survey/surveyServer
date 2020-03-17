package com.kep.surveyServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Surveys;

@Repository
public interface SurveysRepository extends JpaRepository<Surveys, Long>{

}
