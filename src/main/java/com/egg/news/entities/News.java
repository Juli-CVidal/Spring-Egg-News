/*
// Curso Egg FullStack
 */
package com.egg.news.entities;

// @author JulianCVidal
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account creator;

    private boolean deleted;

    @OneToOne
    private Image image;
}
