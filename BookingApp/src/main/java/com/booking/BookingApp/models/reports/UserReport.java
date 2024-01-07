package com.booking.BookingApp.models.reports;

import com.booking.BookingApp.models.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "done = false")
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    public String userThatReports;


    public String userThatIsReported;

    public String reason;
    @Column(name="done",columnDefinition = "boolean default false")
    public Boolean done;


    public UserReport(Long id, String userThatReports, String userThatIsReported, String reason,Boolean done) {
        this.id = id;
        this.userThatReports = userThatReports;
        this.userThatIsReported = userThatIsReported;
        this.reason = reason;
        this.done=done;
    }
    public UserReport( String userThatReports, String userThatIsReported, String reason,Boolean done) {

        this.userThatReports = userThatReports;
        this.userThatIsReported = userThatIsReported;
        this.reason = reason;
        this.done=done;
    }

    public UserReport() {

    }

//    public UserReport() {
//
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserThatReports() {
        return userThatReports;
    }

    public void setUserThatReports(String userThatReports) {
        this.userThatReports = userThatReports;
    }

    public String getUserThatIsReported() {
        return userThatIsReported;
    }

    public void setUserThatIsReported(String userThatIsReported) {
        this.userThatIsReported = userThatIsReported;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
