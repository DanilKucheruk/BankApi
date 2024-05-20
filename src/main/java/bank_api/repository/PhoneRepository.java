package bank_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import bank_api.entity.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findById(Long id);

    Optional<Phone> findByPhoneNumber(String phone);
}
