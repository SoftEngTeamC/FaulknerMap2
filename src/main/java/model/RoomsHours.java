package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ROOMSHOURS")
public class RoomsHours {
    private Long id;

    private String roomNum;

    private Date roomsHoursStart;
    private Date roomsHoursEnd;

    public RoomsHours() {
        // Left empty for hibernate
    }

    public RoomsHours(String roomNum, Date roomsHoursStart, Date roomsHoursEnd) {
        this.roomNum = roomNum;
        this.roomsHoursStart = roomsHoursStart;
        this.roomsHoursEnd = roomsHoursEnd;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ROOMNUM")
    public String getroomNum() {
        return roomNum;
    }

    public void setroomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    @Column(name = "ROOM_START")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getroomsHoursStart() {
        return roomsHoursStart;
    }

    public void setroomsHoursStart(Date roomsHoursStart) {
        this.roomsHoursStart = roomsHoursStart;
    }

    @Column(name = "ROOM_END")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getroomsHoursEnd() {
        return roomsHoursEnd;
    }

    public void setroomsHoursEnd(Date roomsHoursEnd) {
        this.roomsHoursEnd = roomsHoursEnd;
    }
}

