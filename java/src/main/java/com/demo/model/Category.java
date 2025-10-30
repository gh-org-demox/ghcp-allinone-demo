package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Category model for organizing products.
 * See README.md DEMO 3 for step-by-step instructions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
