package main;

import com.library.algorithms.IBookSearchAlgorithm;
import com.library.algorithms.NaiveSearchAlgorithm;
import com.library.models.Book;
import dao.IDao;
import dao.MyDMFileImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IBookSearchAlgorithm algorithm = new NaiveSearchAlgorithm();
        Book book = new Book(1, "uriya avraham", "shoval haya po", "nisim barami", 5);
        List<Book> listOfBooks = new ArrayList<Book>();
        listOfBooks.add(book);
        System.out.println(algorithm.search(listOfBooks, "shov"));
    }
}