package bank_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import bank_api.entity.Client;

import java.util.Optional;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>,JpaSpecificationExecutor<Client> {
    Optional<Client> findByLogin(String login);

    List<Client> findAll();
}