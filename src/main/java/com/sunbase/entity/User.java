package com.sunbase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
                 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	@Column
	private String username;
        
	@Column
	private String password;
	@Column
	private String email;    
        
	private String firstname;
	@Column
	private String lastname;
	
	@Column
	private LocalDate CREATED_DATE;
	
        
	@Column
	private LocalDate LAST_MODIFIED_DATE;
	
	
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> role;
	


}
