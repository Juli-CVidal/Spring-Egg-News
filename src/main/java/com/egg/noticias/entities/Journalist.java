/*
// Curso Egg FullStack
 */
package com.egg.noticias.entities;

// @author JulianCVidal
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "journalist")
public class Journalist{

    @Id
    @GeneratedValue(generator = "uuid")
    private String id;
    private String name;
    private String lastName;

    private String photo;

    private Boolean deleted;

    public Journalist() {
        this.deleted = false;
    }

    public Journalist(String id, String name, String lastName, String photo) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.photo = photo;
        this.deleted = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
