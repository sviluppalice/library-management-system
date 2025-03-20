package org.dirimo.biblioteca.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    // Id - da fare successivamente
    /*  @Id
     *  @GeneratedValue(strategy = GenerationType.IDENTITY)
     *  private Long id;
     */

    @Column(name="created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Column(name="created_by", updatable = false)
    private String createdBy;

    @Column(name="updated_by")
    private String updatedBy;

    @PreUpdate
    private void onPreUpdate() {
        updatedAt = LocalDateTime.now();
        //updatedBy = "user";
    }

    @PrePersist
    private void onPrePersist() {
        createdAt = LocalDateTime.now();
        //createdBy = "user";
    }

    @Version
    @Column
    private long version;
}
