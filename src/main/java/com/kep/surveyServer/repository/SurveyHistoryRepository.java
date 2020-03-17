package com.kep.surveyServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.surveyServer.model.SurveyHistory;
import com.kep.surveyServer.model.SurveyHistoryPK;

@Repository
public interface SurveyHistoryRepository extends JpaRepository<SurveyHistory, SurveyHistoryPK>{

}
