package com.nvm.shoestoreapi;

import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
            BrandRepository brandRepository,
            SupplierRepository supplierRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            CategoryRepository categoryRepository) {
        return args -> {
            // Tạo danh sách role mặc định
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(1L, "ROLE_ADMIN"));
            roles.add(new Role(2L, "ROLE_USER"));
            roleRepository.saveAll(roles);

            Account account = new Account();
            account.setPassword(bCryptPasswordEncoder.encode("123456"));
            account.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_ADMIN")));
            account.setEmail("manhnv291201@gmail.com");

            String randomCode = RandomStringUtils.randomNumeric(6);

            account.setVerificationCode(randomCode);
            account.setEnabled(true);
            account.setAccountNonLocked(true);
            account.setVerificationCodeExpirationDate(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
            accountRepository.save(account);

            // Thêm dữ liệu mẫu cho Supplier
            List<Supplier> suppliers = new ArrayList<>();
            suppliers.add(new Supplier(1L, "Nike Inc", "0123456789", "Hà Nội", "nike@example.com", null));
            suppliers.add(new Supplier(2L, "Adidas Inc", "0987654321", "Hồ Chí Minh", "adidas@example.com", null));
            suppliers.add(new Supplier(3L, "Puma Inc", "0345678901", "Đà Nẵng", "puma@example.com", null));
            suppliers.add(new Supplier(4L, "Reebok Inc", "0765432109", "Hải Phòng", "reebok@example.com", null));
            suppliers.add(new Supplier(5L, "Under Armour Inc", "0567890123", "Cần Thơ", "underarmour@example.com", null));
            supplierRepository.saveAll(suppliers);

            // Thêm dữ liệu mẫu cho Category
            List<Category> categories = new ArrayList<>();
            categories.add(new Category(1L, "Nam", false, "nam", null));
            categories.add(new Category(2L, "Nữ", true,"nu", null));
            categories.add(new Category(3L, "Unisex", true,"unisex", null));
            categories.add(new Category(4L, "Bé trai", true,"be-trai", null));
            categories.add(new Category(5L, "Bé gái", true,"be-gai", null));
            categoryRepository.saveAll(categories);
        };
    }
}
