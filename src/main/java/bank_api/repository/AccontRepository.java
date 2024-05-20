package bank_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank_api.entity.Account;

@Repository
public interface AccontRepository extends JpaRepository<Account, Long> {

}
