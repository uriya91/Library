package Tests;

import com.library.algorithms.RabinKarpSearchAlgorithm;
import com.library.models.Book;
import dao.IDao;
import dao.MyDMFileImpl;
import model.BorrowRecord;
import model.Customer;
import model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.LibraryService;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    private LibraryService libraryService;
    private IDao<Customer> customerDao;
    private IDao<Book> bookDao;

    @BeforeEach
    public void setUp() {
        bookDao = new MyDMFileImpl<>("books.txt", Book.class);
        customerDao = new MyDMFileImpl<>("customers.txt", Customer.class);
        IDao<BorrowRecord> borrowRecordDao = new MyDMFileImpl<>("borrowRecords.txt", BorrowRecord.class);
        IDao<Reservation> reservationDao = new MyDMFileImpl<>("reservations.txt", Reservation.class);


        libraryService = new LibraryService(bookDao, customerDao, borrowRecordDao, reservationDao,new RabinKarpSearchAlgorithm());
    }

    @Test
    public void testBorrowBookSuccess() {
        libraryService.borrowBook(1, 2);
        Customer customer = customerDao.retrieve(1);
        assertTrue(customer.getBorrowedBooks().contains(2));
    }

    @Test
    public void testReturnBookSuccess() {
        libraryService.borrowBook(1, 1);
        libraryService.returnBook(1, 1);
        Customer customer = customerDao.retrieve(1);
        assertFalse(customer.getBorrowedBooks().contains(1));
    }

    @Test
    public void testSearchBooks() {
        List<Book> results = libraryService.searchBooks("example");
        assertNotNull(results);
    }

    @Test
    public void testReserveBook() {
        boolean result = libraryService.reserveBook(1, 1);
        assertTrue(result);
    }
}
