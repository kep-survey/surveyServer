package com.kep.surveyServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kep.surveyServer.model.Questions;
import com.kep.surveyServer.model.Surveys;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long>{

	List<Questions> findBySurveysIdOrderByQuestionOrder(Long surveyId);

	@Transactional
    @Modifying
	void deleteBySurveys(Surveys survey);
}
