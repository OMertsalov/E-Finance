package com.efinance.expenses.category;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @Column(name = "description", length = 200)
    private final String description;

    @Column(length = 45)
    private final String name;

}

