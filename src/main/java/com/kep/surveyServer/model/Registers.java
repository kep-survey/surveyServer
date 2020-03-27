package com.kep.surveyServer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="REGISTERS")
public class Registers {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=300, nullable=false)
	private String pw;
	
	@Column(length=20, nullable=false)
	private String name;
	
	@Column(length=20, nullable=false)
	private String organization;
	
	@OneToMany(mappedBy="registers", cascade=CascadeType.REMOVE)
	private List<Surveys> surveys = new ArrayList<Surveys>();
	
	public Registers(String name, String organization) {
		this.name = name;
		this.organization = organization;
	}
}