import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LibraryApp {
    private Library library;
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField bookTitleField;
    private JTextField bookAuthorField;
    private JTextField patronNameField;
    private JTextField dueDateField;
    private JComboBox<Book> bookComboBox;
    private JComboBox<Patron> patronComboBox;

    public LibraryApp() {
        library = new Library();
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Input panel with GridBagLayout for flexible grid control
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Book Title
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Book Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        bookTitleField = new JTextField();
        inputPanel.add(bookTitleField, gbc);

        // Row 2: Book Author
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Book Author:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        bookAuthorField = new JTextField();
        inputPanel.add(bookAuthorField, gbc);

        // Row 3: Add Book Button (spans 2 columns)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> addBook());
        inputPanel.add(addBookButton, gbc);

        // Row 4: Patron Name
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Patron Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        patronNameField = new JTextField();
        inputPanel.add(patronNameField, gbc);

        // Row 5: Add Patron Button (spans 2 columns)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton addPatronButton = new JButton("Add Patron");
        addPatronButton.addActionListener(e -> addPatron());
        inputPanel.add(addPatronButton, gbc);

        // Row 6: Select Book
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Select Book:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        bookComboBox = new JComboBox<>();
        inputPanel.add(bookComboBox, gbc);

        // Row 7: Select Patron
        gbc.gridx = 0; gbc.gridy = 6;
        inputPanel.add(new JLabel("Select Patron:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        patronComboBox = new JComboBox<>();
        inputPanel.add(patronComboBox, gbc);

        // Row 8: Due Date
        gbc.gridx = 0; gbc.gridy = 7;
        inputPanel.add(new JLabel("Due Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        dueDateField = new JTextField();
        inputPanel.add(dueDateField, gbc);

        // Row 9: Checkout and Return Buttons
        gbc.gridx = 0; gbc.gridy = 8;
        JButton checkoutBookButton = new JButton("Checkout Book");
        checkoutBookButton.addActionListener(e -> checkoutBook());
        inputPanel.add(checkoutBookButton, gbc);

        gbc.gridx = 1; gbc.gridy = 8;
        JButton returnBookButton = new JButton("Return Book");
        returnBookButton.addActionListener(e -> returnBook());
        inputPanel.add(returnBookButton, gbc);

        // Row 10: List Books and Patrons Buttons
        gbc.gridx = 0; gbc.gridy = 9;
        JButton listBooksButton = new JButton("List All Books");
        listBooksButton.addActionListener(e -> listBooks());
        inputPanel.add(listBooksButton, gbc);

        gbc.gridx = 1; gbc.gridy = 9;
        JButton listPatronsButton = new JButton("List All Patrons");
        listPatronsButton.addActionListener(e -> listPatrons());
        inputPanel.add(listPatronsButton, gbc);

        // Output area in the center of BorderLayout
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Adding panels to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Display the frame
        frame.add(mainPanel);
        frame.setVisible(true);

        updateComboBoxes();
    }

    private void addBook() {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        if (!title.isEmpty() && !author.isEmpty()) {
            library.addBook(new Book(title, author));
            outputArea.append("Book added: " + title + " by " + author + "\n");
            updateComboBoxes();
        } else {
            outputArea.append("Please enter both title and author.\n");
        }
    }

    private void addPatron() {
        String name = patronNameField.getText();
        if (!name.isEmpty()) {
            library.addPatron(new Patron(name));
            outputArea.append("Patron added: " + name + "\n");
            updateComboBoxes();
        } else {
            outputArea.append("Please enter the patron's name.\n");
        }
    }

    private void checkoutBook() {
        Patron selectedPatron = (Patron) patronComboBox.getSelectedItem();
        Book selectedBook = (Book) bookComboBox.getSelectedItem();
        String dueDateStr = dueDateField.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (selectedPatron != null && selectedBook != null && !dueDateStr.isEmpty()) {
            try {
                Date dueDate = dateFormat.parse(dueDateStr);
                library.checkoutBook(selectedPatron, selectedBook, dueDate);
                outputArea.append("Book checked out: " + selectedBook.getTitle() + " to " + selectedPatron.getName() + "\n");
                updateComboBoxes();
            } catch (Exception e) {
                outputArea.append("Invalid date format.\n");
            }
        } else {
            outputArea.append("Please select a book, patron, and enter a due date.\n");
        }
    }

    private void returnBook() {
        Patron selectedPatron = (Patron) patronComboBox.getSelectedItem();
        Book selectedBook = (Book) bookComboBox.getSelectedItem();

        if (selectedPatron != null && selectedBook != null) {
            library.returnBook(selectedPatron, selectedBook);
            outputArea.append("Book returned: " + selectedBook.getTitle() + " by " + selectedPatron.getName() + "\n");
            updateComboBoxes();
        } else {
            outputArea.append("Please select a book and patron.\n");
        }
    }

    private void listBooks() {
        outputArea.append("Listing all books:\n");
        for (Book book : library.getBooks()) {
            outputArea.append(book + "\n");
        }
    }

    private void listPatrons() {
        outputArea.append("Listing all patrons:\n");
        for (Patron patron : library.getPatrons()) {
            outputArea.append(patron + "\n");
        }
    }

    private void updateComboBoxes() {
        bookComboBox.removeAllItems();
        patronComboBox.removeAllItems();
        for (Book book : library.getBooks()) {
            bookComboBox.addItem(book);
        }
        for (Patron patron : library.getPatrons()) {
            patronComboBox.addItem(patron);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryApp::new);
    }
}
