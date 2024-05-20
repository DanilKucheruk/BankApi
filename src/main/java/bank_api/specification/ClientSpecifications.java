package bank_api.specification;

import org.springframework.data.jpa.domain.Specification;

import bank_api.entity.Client;
import bank_api.entity.Email;
import bank_api.entity.Phone;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ClientSpecifications {

    public static Specification<Client> likeFullName(String fullName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
    }

    public static Specification<Client> greaterThanBirthDate(LocalDate birthDate) {
        return (root, query, cb) -> cb.greaterThan(root.get("birthDate"), birthDate);
    }

    public static Specification<Client> equalPhone(String phone) {
        return (root, query, cb) -> {
            Join<Client, Phone> phones = root.join("phones");
            return cb.equal(phones.get("phoneNumber"), phone);
        };
    }

    public static Specification<Client> equalEmail(String email) {
        return (root, query, cb) -> {
            Join<Client, Email> emails = root.join("emails");
            return cb.equal(emails.get("emailAddress"), email);
        };
    }
}