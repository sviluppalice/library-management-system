package org.dirimo.biblioteca.resources.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dirimo.biblioteca.common.BaseEntity;
import org.dirimo.biblioteca.resources.reservation.Reservation;

import java.util.List;

@Entity
@Table(name="Customers")
@Getter

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Nome", nullable=false)
    private String firstName;

    @Column(name="Cognome", nullable=false)
    private String lastName;

    @Column(name="email", nullable=false)
    private String email;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Reservation> reservations;
}
