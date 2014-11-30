package org.rickosborne.badger.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotNull
    private String name;

    private String colorHex;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

}