/*
// Curso Egg FullStack
 */
package com.egg.noticias.entities;

// @author JulianCVidal
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

//    @Column(columnDefinition ="MEDIUMTEXT")
//    private String photo;
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @ManyToOne
    private Journalist journalist;

    private boolean deleted;

    @OneToOne
    private Image image;
}
