package com.ecobill.ecobill.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Customer")
public class CustomerEntity {

    @Id
    @Column(name = "phone_number")
    private Long phoneNumber;

    private String name;

    private String username;

    private String password;

    private String email;

}
