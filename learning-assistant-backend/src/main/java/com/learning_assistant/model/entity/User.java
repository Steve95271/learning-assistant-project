package com.learning_assistant.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Persistable<Long> {

    @Id
    private Long id;
    private String email;
    private String passwordHash;
    private String displayName;
    private String profilePictureUrl;
    private AccountStatus accountStatus;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    /*
        Using save method with Spring Data JDBC
        when custom primary key is presented
        we need to mark this is a new object
        otherwise Spring Data JDBC will treat save as update
    */
    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markNew() {
        this.isNew = true;
    }

    public enum AccountStatus {
        active,
        suspended,
        deleted
    }

}
