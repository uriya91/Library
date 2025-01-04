package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Identifiable, Serializable {
    private int customerId;
    private int bookId;
    private LocalDate reservationDate;

    public Reservation(int customerId, int bookId, LocalDate reservationDate) {
        this.customerId = customerId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
    }

    @Override
    public int getId() {
        return bookId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
}
