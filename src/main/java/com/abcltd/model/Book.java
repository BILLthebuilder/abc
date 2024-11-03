package com.abcltd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE book SET status=false WHERE id=?")
public class Book implements Serializable,ParentEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String title;
    private String author;
    private int years;

    @CreationTimestamp
    private Date dateCreated;

    @UpdateTimestamp
    private Date dateUpdated;

    private boolean status;

    @Override
    public boolean getStatus() {
        return this.status;
    }
}
