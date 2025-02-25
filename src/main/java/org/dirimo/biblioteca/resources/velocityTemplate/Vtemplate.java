package org.dirimo.biblioteca.resources.velocityTemplate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dirimo.biblioteca.common.BaseEntity;
import org.dirimo.biblioteca.resources.velocityTemplate.enumerated.VelocityTemplateType;


@Entity
@Table(name = "Templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vtemplate extends BaseEntity {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Lob
    @Column(name="BODY", columnDefinition = "TEXT")
    private String body;

    @Column(name="TYPE")
    private VelocityTemplateType type;
}
