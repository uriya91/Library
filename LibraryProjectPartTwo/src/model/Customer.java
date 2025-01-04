package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Identifiable, Serializable {
    private int id;
    private String name;
    private List<Integer> borrowedBooks;

    public Customer(int id, String name, List<Integer> borrowedBooks) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Integer> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
