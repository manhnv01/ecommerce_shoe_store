package com.nvm.shoestoreapi;

import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;

@SpringBootApplication
@EnableJpaAuditing
public class ShoeStoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoeStoreApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner dataLoader(
            RoleRepository roleRepository,
            AccountRepository accountRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            CartRepository cartRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository) {
        return args -> {
            // Tạo danh sách role mặc định
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(1L, ROLE_ADMIN));
            roles.add(new Role(2L, ROLE_EMPLOYEE));
            roles.add(new Role(3L, ROLE_USER));
            roleRepository.saveAll(roles);

            // Tạo tài khoản admin mặc định
            Account account = new Account();
            account.setId(1L);
            account.setPassword(bCryptPasswordEncoder.encode("123456"));
            account.setRoles(Collections.singletonList(roleRepository.findByName(ROLE_ADMIN)));
            account.setEmail("admin@gmail.com");
            account.setEnabled(true);
            account.setAccountNonLocked(true);
            accountRepository.save(account);

            Employee employee = new Employee();
            employee.setId(1111111111111L);
            employee.setName("Nguyễn Văn Mạnh");
            employee.setAccount(account);
            employee.setCreatedAt(new Date());
            employeeRepository.save(employee);
        };
    }
}
