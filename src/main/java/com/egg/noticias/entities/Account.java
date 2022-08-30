/*
// Curso Egg FullStack
 */
package com.egg.noticias.entities;

// @author JulianCVidal
import com.egg.noticias.enums.Roles;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    
    @Column(unique = true)
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles accountType;

    @OneToOne
    private Image image;
}
