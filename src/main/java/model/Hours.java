package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HOURS")
public class Hours {
    private Long id;

    private String name;

    private Date visitingHoursMorningStart;
    private Date visitingHoursMorningEnd;

    private Date visitingHoursEveningStart;
    private Date visitingHoursEveningEnd;

    public Hours() {
        // Left empty for hibernate
    }

    public Hours(String name, Date visitingHoursMorningStart, Date visitingHoursMorningEnd, Date visitingHoursEveningStart, Date visitingHorusEveningEnd) {
        this.name = name;
        this.visitingHoursMorningStart = visitingHoursMorningStart;
        this.visitingHoursMorningEnd = visitingHoursMorningEnd;
        this.visitingHoursEveningStart = visitingHoursEveningStart;
        this.visitingHoursEveningEnd = visitingHorusEveningEnd;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "MORNING_START")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getVisitingHoursMorningStart() {
        return visitingHoursMorningStart;
    }

    public void setVisitingHoursMorningStart(Date visitingHoursMorningStart) {
        this.visitingHoursMorningStart = visitingHoursMorningStart;
    }

    @Column(name = "MORNING_END")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getVisitingHoursMorningEnd() {
        return visitingHoursMorningEnd;
    }

    public void setVisitingHoursMorningEnd(Date visitingHoursMorningEnd) {
        this.visitingHoursMorningEnd = visitingHoursMorningEnd;
    }

    @Column(name = "EVENING_START")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getVisitingHoursEveningStart() {
        return visitingHoursEveningStart;
    }

    public void setVisitingHoursEveningStart(Date visitingHoursEveningStart) {
        this.visitingHoursEveningStart = visitingHoursEveningStart;
    }

    @Column(name = "EVENING_END")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getVisitingHoursEveningEnd() {
        return visitingHoursEveningEnd;
    }

    public void setVisitingHoursEveningEnd(Date visitingHorusEveningEnd) {
        this.visitingHoursEveningEnd = visitingHorusEveningEnd;
    }
/*
    public boolean isTimeInRange(){
        Date currentTime = new Date();
        if((currentTime.after(visitingHoursMorningStart) && currentTime.before(visitingHoursMorningEnd)) ||
                (currentTime.after(visitingHoursEveningStart) && currentTime.before(visitingHoursEveningEnd))){
            return true;
        }
        return false;
    }
*/
}
