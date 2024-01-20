package com.sunbase.config;



import com.sunbase.entity.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sunbase.service.JwtRoleDetailsService;

@Configuration
public class WebConfig {
    
	@Autowired
	private JwtRoleDetailsService roleDetailsService;

	
	@Bean
	public void addRoles() {
		RoleDTO role = new RoleDTO();	

		role = new RoleDTO();
		role.setRoleName("SITE_ADMIN");
		role.setDescription("SITE ADMIN");
		System.out.println("Role SITE_ADMIN created :" + roleDetailsService.save(role));

		role = new RoleDTO();
		role.setRoleName("SITE_USER");
		role.setDescription("SITE USER");
		System.out.println("Role SITE_USER created :" + roleDetailsService.save(role));

	}
		
	

	
}