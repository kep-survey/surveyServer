package com.kep.surveyServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Answers;

@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long>{
	
	Answers findByUsersBotUserIdAndQuestionsId(String botUserId, long questionId);

	List<Answers> findByQuestionsIdAndAnswerEquals(Long questionId, String answer);
	
	List<Answers> findByQuestionsId(Long questionId);	
}
