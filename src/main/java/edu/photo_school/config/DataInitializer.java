package edu.photo_school.config;

import edu.photo_school.user.domain.Role;
import edu.photo_school.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        List<String> seedRoles = List.of("ROLE_STUDENT", "ROLE_TEACHER", "ROLE_ADMIN", "ROLE_EQUIPMENT_MANAGER");
        seedRoles.forEach(roleName -> roleRepository.findByName(roleName).orElseGet(() -> {
            logger.info("Seeding role: {}", roleName);
            return roleRepository.save(new Role(roleName, "Predefined system role"));
        }));
    }

}
