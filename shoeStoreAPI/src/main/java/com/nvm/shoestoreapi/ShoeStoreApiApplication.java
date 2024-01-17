package com.nvm.shoestoreapi;

import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.BrandRepository;
import com.nvm.shoestoreapi.repository.CategoryRepository;
import com.nvm.shoestoreapi.repository.RoleRepository;
import com.nvm.shoestoreapi.repository.SubCategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
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
            BrandRepository brandRepository,
            CategoryRepository categoryRepository) {
        return args -> {
            // Tạo danh sách role mặc định
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(1L, "ROLE_ADMIN"));
            roles.add(new Role(2L, "ROLE_USER"));
            roleRepository.saveAll(roles);

            // Thêm dữ liệu mẫu cho Brand
            List<Brand> brands = new ArrayList<>();
            brands.add(new Brand(1L, "Nike", "nike", null, null));
            brands.add(new Brand(2L, "Adidas", "adidas", null, null));
            brands.add(new Brand(3L, "Puma", "puma", null, null));
            brands.add(new Brand(4L, "Reebok", "reebok", null, null));
            brands.add(new Brand(5L, "New Balance", "new-balance", null, null));
            brandRepository.saveAll(brands);

            // Thêm dữ liệu mẫu cho Category
            List<Category> categories = new ArrayList<>();
            categories.add(new Category(1L, "Nam", true, "nam", null));
            categories.add(new Category(2L, "Nữ", true,"nu", null));
            categories.add(new Category(3L, "Unisex", true,"unisex", null));
            categories.add(new Category(4L, "Bé trai", true,"be-trai", null));
            categories.add(new Category(5L, "Bé gái", true,"be-gai", null));
            categoryRepository.saveAll(categories);
        };
    }
}
