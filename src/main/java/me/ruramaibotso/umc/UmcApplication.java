package me.ruramaibotso.umc;

import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Section;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.SectionsRepository;
import me.ruramaibotso.umc.user.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication
public class UmcApplication {
	static UserRepository userRepository;
	static  PasswordEncoder passwordEncoder;
	static  LocalsRepository localsRepository;
	static SectionsRepository sectionsRepository;


	public UmcApplication(UserRepository userRepository, PasswordEncoder passwordEncoder, LocalsRepository localsRepository, SectionsRepository sectionsRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.localsRepository = localsRepository;
		this.sectionsRepository = sectionsRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(UmcApplication.class, args);

		if(localsRepository.count()== 0 && userRepository.count() ==0 && sectionsRepository.count() == 0){
			Locals locals = new Locals();
			locals.setName("St Andrews");
			locals.setLocation("Makoni");
			localsRepository.save(locals);

			Section section = new Section();
			section.setName("P1");
			section.setLocals(locals);
			sectionsRepository.save(section);

			User user = new User();
			user.setFirstname("Ruramai");
			user.setLastname("Botso");
			user.setOrganisation(Organisation.UMYF);
			user.setRole(Role.Super_Admin);
			user.setDateOfBirth(LocalDate.of(2001, Month.of(8), 10));
			user.setGender(Gender.Male);
			user.setMembershipStatus(Membership.Full);
			user.setId(0);
			user.setMembershipNumber("SN0000");
			user.setPassword(passwordEncoder.encode("admin@umc"));
			user.setSection(section);
			user.setLocals(locals);
			userRepository.save(user);

		}

	}

}
