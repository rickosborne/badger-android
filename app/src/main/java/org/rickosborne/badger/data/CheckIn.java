package org.rickosborne.badger.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "userId", nullable = false)
    @NotNull private User user;
    @NotNull private Date dateSubmitted;
    @NotNull private short painScale;
    @NotNull private short consumeScale;
    private Date dateLastConsumed;
    @OneToMany(fetch = FetchType.EAGER) @JoinColumn(name = "checkInId") private List<CheckInMedication> medications;

    @JsonIgnore public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    @JsonIgnore public long getUserId() { return this.user.getId(); }
    public Date getDateSubmitted() { return dateSubmitted; }
    public void setDateSubmitted(Date dateSubmitted) { this.dateSubmitted = dateSubmitted; }
    public short getPainScale() { return painScale; }
    public void setPainScale(short painScale) { this.painScale = painScale; }
    public short getConsumeScale() { return this.consumeScale; }
    public void setConsumeScale(short consumeScale) { this.consumeScale = consumeScale; }
    public Date getDateLastConsumed() { return dateLastConsumed; }
    public void setDateLastConsumed(Date dateLastConsumed) { this.dateLastConsumed = dateLastConsumed; }

    public void setMedications(List<CheckInMedication> medications) { this.medications = medications; }
    public List<CheckInMedication> getMedications() { return medications; }

}
