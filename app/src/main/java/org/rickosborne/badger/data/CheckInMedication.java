package org.rickosborne.badger.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@IdClass(CheckInMedication.PK.class)
public class CheckInMedication {

    public static class PK implements Serializable {
        public CheckIn checkIn;
        public Medication medication;
    }

    @Id @NotNull @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "checkInId", nullable = false) private CheckIn checkIn;
    @Id @NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "medicationId", nullable = false) private Medication medication;
    @NotNull private boolean taken = false;

    public void setCheckIn(CheckIn checkIn) { this.checkIn = checkIn; }
    @JsonIgnore public CheckIn getCheckIn() { return checkIn; }
    public void setMedication(Medication medication) { this.medication = medication; }
    public Medication getMedication() { return medication; }
    public void setTaken(boolean taken) { this.taken = taken; }
    public boolean getTaken() { return taken; }

}
