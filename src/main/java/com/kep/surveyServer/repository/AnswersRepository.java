package com.kep.surveyServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Answers;

@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long>{
	
	Answers findByBotUserIdAndQuestionId(String botUserId, long questionId);

}
