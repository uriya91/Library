package dao;

import com.library.models.Book;
import model.BorrowRecord;
import model.Customer;
import model.Reservation;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyDMFileImpl<T> implements IDao<T> {
    private String filePath;
    private Class<T> clazz;
    private String delimiter = ",";

    public MyDMFileImpl(String resourceName, Class<T> clazz) {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
        try {
            this.filePath = Paths.get(resource.toURI()).toAbsolutePath().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for resource: " + resourceName, e);
        }
        this.clazz = clazz;
    }

    @Override
    public void save(T data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(convertObjectToLine(data));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T retrieve(int id) {
        List<T> dataList = getAll();
        for (T data : dataList) {
            if (getIdFromObject(data) == id) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void delete(int id) {
        List<T> dataList = getAll();
        dataList.removeIf(data -> getIdFromObject(data) == id);
        writeToFile(dataList);
    }

    @Override
    public List<T> getAll() {
        List<T> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dataList.add(convertLineToObject(line));
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void writeToFile(List<T> dataList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T data : dataList) {
                writer.write(convertObjectToLine(data));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertObjectToLine(T data) {
        if (data instanceof Book) {
            Book book = (Book) data;
            return book.getId() + delimiter
                    + book.getTitle() + delimiter
                    + book.getDescription() + delimiter
                    + book.getAuthor() + delimiter
                    + book.getAvailableCopies();

        } else if (data instanceof Customer) {
            Customer customer = (Customer) data;
            return customer.getId() + delimiter
                    + customer.getName() + delimiter
                    + String.join(";", customer.getBorrowedBooks().stream()
                    .map(String::valueOf)
                    .toArray(String[]::new));

        } else if (data instanceof BorrowRecord) {
            BorrowRecord br = (BorrowRecord) data;
            return br.getBookId() + delimiter
                    + br.getCustomerId() + delimiter
                    + br.getBorrowDate().toString() + delimiter
                    + br.getReturnDate().toString();

        } else if (data instanceof Reservation) {
            Reservation r = (Reservation) data;
            return r.getCustomerId() + delimiter
                    + r.getBookId() + delimiter
                    + r.getReservationDate().toString();

        } else {
            throw new IllegalArgumentException("Unsupported type: " + data.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    private T convertLineToObject(String line) {
        String[] parts = line.split(delimiter);

        if (clazz.equals(Book.class)) {
            return (T) new Book(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    parts[2],
                    parts[3],
                    Integer.parseInt(parts[4])
            );

        } else if (clazz.equals(Customer.class)) {
            List<Integer> borrowedBooks = new ArrayList<>();
            if (parts.length > 2 && !parts[2].isEmpty()) {
                for (String bookId : parts[2].split(";")) {
                    borrowedBooks.add(Integer.parseInt(bookId));
                }
            }
            return (T) new Customer(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    borrowedBooks
            );

        } else if (clazz.equals(BorrowRecord.class)) {
            int bookId = Integer.parseInt(parts[0]);
            int custId = Integer.parseInt(parts[1]);
            return (T) new BorrowRecord(
                    bookId,
                    custId,
                    java.time.LocalDate.parse(parts[2]),
                    java.time.LocalDate.parse(parts[3])
            );

        } else if (clazz.equals(Reservation.class)) {
            int customerId = Integer.parseInt(parts[0]);
            int bookId = Integer.parseInt(parts[1]);
            return (T) new Reservation(
                    customerId,
                    bookId,
                    java.time.LocalDate.parse(parts[2])
            );

        } else {
            throw new IllegalArgumentException("Unsupported type: " + clazz);
        }
    }

    private int getIdFromObject(T data) {
        if (data instanceof Book) {
            return ((Book) data).getId();
        } else if (data instanceof Customer) {
            return ((Customer) data).getId();
        } else if (data instanceof BorrowRecord) {
            return ((BorrowRecord) data).getId();
        } else if (data instanceof Reservation) {
            return ((Reservation) data).getId();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + data.getClass());
        }
    }
}
