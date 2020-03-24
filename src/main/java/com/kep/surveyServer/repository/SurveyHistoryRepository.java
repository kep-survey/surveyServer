package com.kep.surveyServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.SurveyHistory;
import com.kep.surveyServer.model.SurveyHistoryPK;

@Repository
public interface SurveyHistoryRepository extends JpaRepository<SurveyHistory, SurveyHistoryPK>{
	
	@Query(value = "select * from survey_history where survey_id=:survey_id", nativeQuery=true)
	List<SurveyHistory> findByIdSurveyId(@Param("survey_id") Long surveyId);
}
