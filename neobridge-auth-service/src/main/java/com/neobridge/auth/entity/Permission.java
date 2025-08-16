package com.neobridge.auth.entity;

import com.neobridge.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Permission entity representing system permissions in the NeoBridge platform.
 * Permissions define what actions users can perform on specific resources.
 */
@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permissions_name", columnList = "name"),
    @Index(name = "idx_permissions_resource", columnList = "resource"),
    @Index(name = "idx_permissions_action", columnList = "action")
})
public class Permission extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotBlank
    @Size(max = 100)
    @Column(name = "resource", nullable = false)
    private String resource;

    @NotBlank
    @Size(max = 50)
    @Column(name = "action", nullable = false)
    private String action;

    // Constructors
    public Permission() {}

    public Permission(String name, String description, String resource, String action) {
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
    }

    // Helper methods
    public String getFullPermission() {
        return resource + ":" + action;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
