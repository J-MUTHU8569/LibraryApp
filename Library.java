import java.io.*;
import java.util.*;

class Book implements Serializable {
    private String title;
    private String author;
    private boolean isCheckedOut;
    private Date dueDate;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isCheckedOut = false;
        this.dueDate = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void checkOut(Date dueDate) {
        this.isCheckedOut = true;
        this.dueDate = dueDate;
    }

    public void returnBook() {
        this.isCheckedOut = false;
        this.dueDate = null;
    }

    public boolean isOverdue() {
        if (isCheckedOut && dueDate != null) {
            return new Date().after(dueDate);
        }
        return false;
    }

    @Override
    public String toString() {
        return title + " by " + author + (isCheckedOut ? " (Checked out, due: " + dueDate + ")" : " (Available)");
    }
}

class Patron implements Serializable {
    private String name;
    private List<Book> borrowedBooks;

    public Patron(String name) {
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book, Date dueDate) {
        book.checkOut(dueDate);
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        book.returnBook();
        borrowedBooks.remove(book);
    }

    @Override
    public String toString() {
        return "Patron: " + name + ", Borrowed Books: " + borrowedBooks;
    }
}

class Library {
    private List<Book> books;
    private List<Patron> patrons;
    private static final String BOOKS_FILE = "books.dat";
    private static final String PATRONS_FILE = "patrons.dat";

    public Library() {
        books = loadBooks();
        patrons = loadPatrons();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    public void addPatron(Patron patron) {
        patrons.add(patron);
        savePatrons();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Patron> getPatrons() {
        return patrons;
    }

    public void checkoutBook(Patron patron, Book book, Date dueDate) {
        patron.borrowBook(book, dueDate);
        saveBooks();
        savePatrons();
    }

    public void returnBook(Patron patron, Book book) {
        patron.returnBook(book);
        saveBooks();
        savePatrons();
    }

    private void saveBooks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            out.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePatrons() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATRONS_FILE))) {
            out.writeObject(patrons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Book> loadBooks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            return (List<Book>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private List<Patron> loadPatrons() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATRONS_FILE))) {
            return (List<Patron>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
