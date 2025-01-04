package model;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowRecord implements Identifiable, Serializable {
    private int bookId;
    private int customerId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(int bookId, int customerId, LocalDate borrowDate, LocalDate returnDate) {
        this.bookId = bookId;
        this.customerId = customerId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    @Override
    public int getId() {
        return bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
