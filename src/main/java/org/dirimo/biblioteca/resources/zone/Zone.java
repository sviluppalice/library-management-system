package org.dirimo.biblioteca.resources.zone;

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
import org.dirimo.biblioteca.resources.shelf.Shelf;

import java.util.List;

@Entity
@Table(name="Zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;

    @Column(name = "zone_name")
    private String zoneName;

    @OneToMany(mappedBy = "zone")
    @JsonIgnore
    private List<Shelf> shelves;
}