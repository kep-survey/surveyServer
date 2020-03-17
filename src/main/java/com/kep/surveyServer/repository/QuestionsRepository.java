package com.kep.surveyServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.Questions;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long>{

}
