package bank_api.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "fullname", unique = false, nullable = false)
        private String fullName;

        @Column(name = "login", unique = true, nullable = false)
        private String login;

        @Column(name = "birthDate", nullable = false)
        private LocalDate birthDate;

        @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Phone> phones;

        @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Email> emails;

        @Column(name ="password", nullable = false)
        private String password;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "account_id", referencedColumnName = "id")
        private Account account;
}
