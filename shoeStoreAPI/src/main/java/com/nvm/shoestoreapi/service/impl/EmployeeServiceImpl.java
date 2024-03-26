package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.EmployeeRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Employee;
import com.nvm.shoestoreapi.repository.AccountRepository;
import com.nvm.shoestoreapi.repository.EmployeeRepository;
import com.nvm.shoestoreapi.repository.RoleRepository;
import com.nvm.shoestoreapi.service.EmployeeService;
import com.nvm.shoestoreapi.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> searchEmployee(String search, Pageable pageable) {
        return employeeRepository.findByNameContainingOrPhoneContainingOrAccount_EmailContaining(search, search, search, pageable);
    }

    @Override
    public Page<Employee> findByEnabled(boolean enabled, Pageable pageable) {
        return null;
    }

    @Override
    public Employee create(EmployeeRequest employeeRequest) {
        if (accountRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new RuntimeException(DUPLICATE_EMAIL);
        }

        // Kiểm tra 18 tuổi
        LocalDate birthdayLocalDate = employeeRequest.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period age = Period.between(birthdayLocalDate, LocalDate.now());

        if (age.getYears() < 18) {
            throw new RuntimeException(EMPLOYEE_AGE_LESS_THAN_18);
        }

        Account account = new Account();
        account.setEmail(employeeRequest.getEmail());
        account.setEnabled(true);
        if (employeeRequest.getStatus().equalsIgnoreCase(WORKING))
            account.setAccountNonLocked(true);
        else if (employeeRequest.getStatus().equalsIgnoreCase(STOPPED_WORKING))
            account.setAccountNonLocked(false);
        account.setPassword(bCryptPasswordEncoder.encode("123456"));
        account.setRoles(List.of(roleRepository.findByName(ROLE_EMPLOYEE)));
        accountRepository.save(account);

        Employee employee = new Employee();
        modelMapper.map(employeeRequest, employee);
        employee.setId(new Date().getTime());
        employee.setAccount(account);
        employee.setAvatar(storageService.saveFile(employeeRequest.getFile()));
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(EmployeeRequest employeeRequest) {
        // Kiểm tra xem nhân viên có tồn tại không
        Employee existingEmployee = employeeRepository.findById(employeeRequest.getId())
                .orElseThrow(() -> new RuntimeException(EMPLOYEE_NOT_FOUND));

        if (existingEmployee.getId() == 1111111111111L)
            throw new RuntimeException(FORBIDDEN);

        // Kiểm tra tuổi của nhân viên
        LocalDate birthdayLocalDate = employeeRequest.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period age = Period.between(birthdayLocalDate, LocalDate.now());
        if (age.getYears() < 18) {
            throw new RuntimeException(EMPLOYEE_AGE_LESS_THAN_18);
        }

        if (!existingEmployee.getAccount().getEmail().equals(employeeRequest.getEmail())
                && accountRepository.existsByEmail(employeeRequest.getEmail()))
            throw new RuntimeException(DUPLICATE_EMAIL);

        Account account = existingEmployee.getAccount();
        account.setEmail(employeeRequest.getEmail());
        if (employeeRequest.getStatus().equalsIgnoreCase(WORKING))
            account.setAccountNonLocked(true);
        else if (employeeRequest.getStatus().equalsIgnoreCase(STOPPED_WORKING))
            account.setAccountNonLocked(false);

        // Cập nhật thông tin nhân viên từ request
        existingEmployee.setName(employeeRequest.getName());
        existingEmployee.setGender(employeeRequest.getGender());
        existingEmployee.setStatus(employeeRequest.getStatus());
        existingEmployee.setPhone(employeeRequest.getPhone());
        existingEmployee.setCity(employeeRequest.getCity());
        existingEmployee.setDistrict(employeeRequest.getDistrict());
        existingEmployee.setWard(employeeRequest.getWard());
        existingEmployee.setAddressDetail(employeeRequest.getAddressDetail());
        existingEmployee.setBirthday(employeeRequest.getBirthday());

        if (employeeRequest.getFile() != null && !employeeRequest.getFile().isEmpty()) {
            storageService.deleteFile(existingEmployee.getAvatar());
            existingEmployee.setAvatar(storageService.saveFile(employeeRequest.getFile()));
        }

        // Lưu thông tin nhân viên đã cập nhật
        return employeeRepository.save(existingEmployee);
    }


    @Override
    public long count() {
        return employeeRepository.count();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        if (employeeRepository.existsById(id)) {
            return employeeRepository.findById(id);
        }
        throw new RuntimeException(EMPLOYEE_NOT_FOUND);
    }
}
