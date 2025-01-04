package services;

import com.library.algorithms.RabinKarpSearchAlgorithm;
import com.library.models.Book;
import dao.IDao;
import model.BorrowRecord;
import model.Customer;
import model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class LibraryService {
    private IDao<Book> bookDao;
    private IDao<Customer> customerDao;
    private IDao<BorrowRecord> borrowRecordDao;
    private IDao<Reservation> reservationDao;
    private RabinKarpSearchAlgorithm searchAlgorithm;

    public LibraryService(IDao<Book> bookDao, IDao<Customer> customerDao, IDao<BorrowRecord> borrowRecordDao, IDao<Reservation> reservationDao, RabinKarpSearchAlgorithm searchAlgorithm) {
        this.bookDao = bookDao;
        this.customerDao = customerDao;
        this.borrowRecordDao = borrowRecordDao;
        this.reservationDao = reservationDao;
        this.searchAlgorithm = searchAlgorithm;
    }

    public List<Book> searchBooks(String query) {
        List<Book> books = bookDao.getAll();
        return searchAlgorithm.search(books, query);
    }

    public boolean borrowBook(int customerId, int bookId) {
        Customer customer = customerDao.retrieve(customerId);
        Book book = bookDao.retrieve(bookId);
        if (customer != null && book != null && book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            customer.getBorrowedBooks().add(bookId);
            bookDao.save(book);
            customerDao.save(customer);
            borrowRecordDao.save(new BorrowRecord(bookId, customerId, LocalDate.now(), LocalDate.now().plusWeeks(2)));
            return true;
        }
        return false;
    }

    public boolean returnBook(int customerId, int bookId) {
        Customer customer = customerDao.retrieve(customerId);
        Book book = bookDao.retrieve(bookId);
        if (customer != null && book != null && customer.getBorrowedBooks().contains(bookId)) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            customer.getBorrowedBooks().remove(bookId);
            bookDao.save(book);
            customerDao.save(customer);
            return true;
        }
        return false;
    }

    public boolean reserveBook(int customerId, int bookId) {
        Book book = bookDao.retrieve(bookId);
        if (book != null && book.getAvailableCopies() == 0) {
            reservationDao.save(new Reservation(customerId, bookId, LocalDate.now()));
            return true;
        }
        return false;
    }

    public void deleteBook(int bookId) {
        Book book = bookDao.retrieve(bookId);
        if (book != null) {
            bookDao.delete(bookId);
        }
    }

    public void setSearchAlgorithm(RabinKarpSearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }
}
