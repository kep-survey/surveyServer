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
	
	@Query(value = "select * from survey_history where survey_id=:survey_id and question_order=:sum_questions", nativeQuery=true)
	List<SurveyHistory> findHistory(@Param("survey_id") Long surveyId, @Param("sum_questions") int sumQuestions);
	
	List<SurveyHistory> findByIdSurveyIdAndQuestionOrderIs(Long surveyId, int sumQuestions);
}
