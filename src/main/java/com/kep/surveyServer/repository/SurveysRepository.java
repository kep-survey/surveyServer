package com.kep.surveyServer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kep.surveyServer.model.Registers;
import com.kep.surveyServer.model.Surveys;

@Repository
public interface SurveysRepository extends JpaRepository<Surveys, Long>{
	List<Surveys> findByRegisters(Registers registers);
	
	@Transactional
    @Modifying
	void deleteByIdAndRegisters(Long surveyId, Registers registers);

	Optional<Surveys> findByIdAndRegisters(Long surveyId, Registers register);
}
