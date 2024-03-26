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
            BrandRepository brandRepository,
            ProductRepository productRepository,
            ProductColorRepository productColorRepository,
            ProductDetailsRepository productDetailsRepository,
            SupplierRepository supplierRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            CategoryRepository categoryRepository,
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
            account.setEmail("manhnv291201@gmail.com");
            account.setEnabled(true);
            account.setAccountNonLocked(true);
            accountRepository.save(account);

            Employee employee = new Employee();
            employee.setId(1111111111111L);
            employee.setName("ADMINISTRATOR");
            employee.setAccount(account);
            employee.setCreatedAt(new Date());
            employeeRepository.save(employee);

            // Tạo customer mặc định
            Account customerAccount = new Account();
            customerAccount.setId(2L);
            customerAccount.setPassword(bCryptPasswordEncoder.encode("123456"));
            customerAccount.setRoles(Collections.singletonList(roleRepository.findByName(ROLE_USER)));
            customerAccount.setEmail("manonguyen123@gmail.com");
            customerAccount.setEnabled(true);
            customerAccount.setAccountNonLocked(true);
            accountRepository.save(customerAccount);

            Customer customer = new Customer();
            customer.setId(2222222222222L);
            customer.setName("CUSTOMER");
            customer.setAccount(customerAccount);
            customerRepository.save(customer);

            Cart cart = new Cart();
            cart.setId(1L);
            cart.setCustomer(customer);
            cartRepository.save(cart);

            customer.setCart(cart);
            customerRepository.save(customer);

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
            categories.add(new Category(2L, "Nữ", true, "nu", null));
            categories.add(new Category(3L, "Unisex", true, "unisex", null));
            categories.add(new Category(4L, "Bé trai", true, "be-trai", null));
            categories.add(new Category(5L, "Bé gái", true, "be-gai", null));
            categoryRepository.saveAll(categories);

            // Thêm dữ liệu mẫu cho Brand
            List<Brand> brands = new ArrayList<>();
            brands.add(new Brand(1L, "Nike", "nike", true, "nike.jpg", null));
            brands.add(new Brand(2L, "Adidas", "adidas", true, "adidas.webp", null));
            brands.add(new Brand(3L, "Puma", "puma", true, "puma.jpg", null));
            brands.add(new Brand(4L, "Reebok", "reebok", true, "reebok.webp", null));
            brands.add(new Brand(5L, "Under Armour", "under-armour", true, "under-armour.jpg", null));
            brands.add(new Brand(6L, "Converse", "converse", true, "converse.png", null));
            brands.add(new Brand(7L, "Vans", "vans", true, "vans.jpg", null));
            brands.add(new Brand(8L, "New Balance", "new-balance", true, "new-balance.png", null));
            brands.add(new Brand(9L, "Balenciaga", "balenciaga", true, "balenciaga.webp", null));
            brands.add(new Brand(10L, "Gucci", "gucci", true, "gucci.png", null));
            brands.add(new Brand(11L, "Louis Vuitton", "louis-vuitton", true, "louis-vuitton.webp", null));
            brandRepository.saveAll(brands);

            // Thêm dữ liệu mẫu cho Product
            List<Product> products = new ArrayList<>();
            for (int i = 1; i <= 25; i++) {
                Product product = new Product();
                product.setName("Product Demo " + i);
                product.setSlug("product-demo-" + i);
                product.setPrice(100000L + i * 2000);
                product.setEnabled(true);
                product.setBrand(brands.get(i % brands.size()));
                product.setCategory(categories.get(i % categories.size()));
                product.setThumbnail("product-demo-" + i + ".webp");
                List<String> images = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    images.add("product-demo-" + i + ".webp");
                }
                product.setImages(images);

                // Lưu trữ Product trước
                Product savedProduct = productRepository.save(product);

                List<ProductColor> productColors = new ArrayList<>();

                //ran dom số nguyên từ 1 đến 5
                Random random = new Random();
                int randomInt = random.nextInt(5) + 1;

                for (int j = 1; j <= randomInt; j++) {
                    ProductColor productColor = new ProductColor();
                    productColor.setColor("Color " + j);

                    // Thiết lập ProductColor trước khi lưu ProductDetails
                    productColor.setProduct(savedProduct);
                    productColor = productColorRepository.save(productColor);

                    ProductColor finalProductColor = productColor;
                    List<ProductDetails> productDetailsList = PRODUCT_SIZE.stream()
                            .map(size -> {
                                ProductDetails productDetails = new ProductDetails();
                                productDetails.setSize(size);
                                productDetails.setQuantity(0);
                                // Thiết lập ProductDetails trước khi lưu
                                productDetails.setProductColor(finalProductColor);
                                return productDetails;
                            })
                            .collect(Collectors.toList());

                    // Lưu trữ ProductDetails sau khi được thiết lập
                    productDetailsRepository.saveAll(productDetailsList);
                    productColor.setProductDetails(productDetailsList);
                    productColors.add(productColor);
                }
                // Lưu trữ ProductColor sau khi tất cả ProductDetails đã được lưu
                productColorRepository.saveAll(productColors);
                products.add(savedProduct);
            }
        };
    }
}
