package com.kep.surveyServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Surveys;

@Repository
public interface SurveysRepository extends JpaRepository<Surveys, Long>{
	
	List<Surveys> findByOpenTrueAndRegistersId(Long registerId);
}
