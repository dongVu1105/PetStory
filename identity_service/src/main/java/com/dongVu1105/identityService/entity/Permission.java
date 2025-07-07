package com.dongVu1105.identityService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Permission {
    @Id
    String name;
    String description;
}
