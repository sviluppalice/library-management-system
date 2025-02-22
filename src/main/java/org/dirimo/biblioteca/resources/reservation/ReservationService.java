package org.dirimo.biblioteca.resources.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.mail.MailProperties;
import org.dirimo.biblioteca.resources.book.Book;
import org.dirimo.biblioteca.resources.book.BookService;
import org.dirimo.biblioteca.resources.customer.Customer;
import org.dirimo.biblioteca.resources.customer.CustomerService;
import org.dirimo.biblioteca.resources.reservation.enumerated.ReservationStatus;
import org.dirimo.biblioteca.resources.reservation.event.CloseReservationEvent;
import org.dirimo.biblioteca.resources.reservation.event.OpenReservationEvent;
import org.dirimo.biblioteca.resources.stock.Stock;
import org.dirimo.biblioteca.resources.stock.StockService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StockService stockService;
    private final BookService bookService;
    private final CustomerService customerService;
    private final ApplicationEventPublisher eventPublisher;

    // Get all reservations
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    // Get a reservation by ID
    public Optional<Reservation> getById(Long id) {
        return reservationRepository.findById(id);
    }

    // Add a new reservation
    public Reservation create(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Update a reservation
    public Reservation update(Long id, Reservation reservation) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        return reservationRepository.save(reservation);
    }

    // Delete a reservation by ID
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    // Close a reservation
    public Reservation close(Long id, LocalDate date) {
        // Updates reservation
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        reservation.setStatus(ReservationStatus.CLOSED);
        reservation.setResEndDate(date);

        // Updates stock
        Book book = reservation.getBook();
        Optional<Stock> stockOptional = stockService.getByBook(book);
        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + book.getBookId() + " non trovato.")
        );
        stock.handleQuantity(1);

        // Publishes event
        eventPublisher.publishEvent(new CloseReservationEvent(this, reservation));

        return reservation;
    }

    public Reservation open(Reservation reservation, LocalDate date) {
        // Check stock existence
        Book book = reservation.getBook();
        Optional<Stock> stockOptional = stockService.getByBook(book);
        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + book.getBookId() + " non trovato.")
        );

        // Check copies availability and if available, creates reservation
        if (stock.getAvailableCopies() <= 0) {
            throw new RuntimeException("Non ci sono copie del libro " + book.getBookId() + " disponibili al momento.");
        } else {
            stock.handleQuantity(-1);
            stockService.update(stock.getStockId(), stock);

            // Set start date and save reservation
            reservation.setResStartDate(date);
            Reservation savedReservation = reservationRepository.save(reservation);

            // Publishes event
            eventPublisher.publishEvent(new OpenReservationEvent(this, savedReservation));
            return savedReservation;
        }
    }

    public List<Reservation> getExpiring(ReservationStatus status, LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findExpiring(status, startDate, endDate);
    }

    public List<Reservation> getExpired(ReservationStatus status, LocalDate today) {
        return reservationRepository.findExpired(status, today);
    }

    // email builders
    public MailProperties buildExpiringReminderMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book book = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        String body = """
            <div style='font-family: Arial, sans-serif; padding: 15px; background-color: #f9f9f9;
            border: 1px solid #ddd; border-radius: 8px; max-width: 600px;'>
                <h2 style='color: #2c3e50;'>📅 Your Reservation is Expiring Soon!</h2>
                <p>Dear <b>%s %s</b>,</p>
                <p>Your reservation <b>(ID: %d)</b> will expire on <b style='color: #e74c3c;'>%s</b>.</p>
                <p>Please return the book to the library, or contact us to make any necessary adjustments.</p>
                <hr style='border: 1px solid #ddd;'>
                <h3 style='color: #2980b9;'>📖 Book Details</h3>
                <ul>
                    <li><b>Title:</b> %s</li>
                    <li><b>Author:</b> %s</li>
                    <li><b>Publisher:</b> %s</li>
                    <li><b>Year:</b> %s</li>
                </ul>
                <p>Thank you,<br><i>The Library Team</i></p>
            </div>
            """.formatted(
                c.getFirstName(), c.getLastName(),
                r.getResId(), r.getResExpiryDate(),
                book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getYear()
        );

        String subject = "Reminder: Your Library Reservation Expires Soon! " + r.getResExpiryDate();

        mailProperties.setBody(body);
        mailProperties.setTo(r.getCustomer().getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

    public MailProperties buildExpiredNoticeMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book book = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        String body = """
            <div style='font-family: Arial, sans-serif; padding: 15px; background-color: #f9f9f9;
            border: 1px solid #ddd; border-radius: 8px; max-width: 600px;'>
                <h2 style='color: #c0392b;'>⚠️ Your Reservation Has Expired!</h2>
                <p>Dear <b>%s %s</b>,</p>
                <p>We regret to inform you that your reservation <b>(ID: %d)</b> has already expired on <b style='color: #e74c3c;'>%s</b>.</p>
                <p>To avoid any late fees or issues, please return the book to the library in three days, or contact us to make any necessary adjustments.</p>
                <hr style='border: 1px solid #ddd;'>
                <h3 style='color: #2980b9;'>📖 Book Details</h3>
                <ul>
                    <li><b>Title:</b> %s</li>
                    <li><b>Author:</b> %s</li>
                    <li><b>Publisher:</b> %s</li>
                    <li><b>Year:</b> %d</li>
                </ul>
                <p>If you have any questions, feel free to reach out to us. <br>Thank you for your attention,<br><i>The Library Team</i></p>
            </div>
        """.formatted(
                c.getFirstName(), c.getLastName(),
                r.getResId(), r.getResExpiryDate(),
                book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getYear() // No need for String.valueOf() anymore
        );

        String subject = "Notice: Your Library Reservation Expired on " + r.getResExpiryDate();

        mailProperties.setBody(body);
        mailProperties.setTo(c.getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

    public MailProperties buildCreatedReservationMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book book = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        String body = """
            <div style='font-family: Arial, sans-serif; padding: 15px; background-color: #f9f9f9;
            border: 1px solid #ddd; border-radius: 8px; max-width: 600px;'>
                <h2 style='color: #27ae60;'>✅ Your Reservation Has Been Created!</h2>
                <p>Dear <b>%s %s</b>,</p>
                <p>Your reservation <b>(ID: %d)</b> has been successfully created on <b style='color: #2c3e50;'>%s</b>.</p>
                <p>Please remember to return your book before <b style='color: #e67e22;'>%s</b> to avoid late fees.</p>
                <hr style='border: 1px solid #ddd;'>
                <h3 style='color: #2980b9;'>📖 Book Details</h3>
                <ul>
                    <li><b>Title:</b> %s</li>
                    <li><b>Author:</b> %s</li>
                    <li><b>Publisher:</b> %s</li>
                    <li><b>Year:</b> %d</li>
                </ul>
              <p>If you have any questions, feel free to reach out to us. <br>Thank you for using our library services!
              <br><i>The Library Team</i></p>
            </div>
        """.formatted(
                c.getFirstName(), c.getLastName(),
                r.getResId(), r.getResStartDate(), r.getResExpiryDate(),
                book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getYear()
        );

        String subject = "Book Reservation Confirmation: " + book.getTitle();

        mailProperties.setBody(body);
        mailProperties.setTo(c.getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

    public MailProperties buildClosedReservationMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book book = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        String body = """
            <div style='font-family: Arial, sans-serif; padding: 15px; background-color: #f9f9f9;
            border: 1px solid #ddd; border-radius: 8px; max-width: 600px;'>
                <h2 style='color: #27ae60;'>✅ Your Reservation Has Been Closed Successfully!</h2>
                <p>Dear <b>%s %s</b>,</p>
                <p>Your reservation <b>(ID: %d)</b> has been successfully closed on <b style='color: #2c3e50;'>%s</b>.</p>
                <hr style='border: 1px solid #ddd;'>
                <h3 style='color: #2980b9;'>📖 Book Details</h3>
                <ul>
                    <li><b>Title:</b> %s</li>
                    <li><b>Author:</b> %s</li>
                    <li><b>Publisher:</b> %s</li>
                    <li><b>Year:</b> %d</li>
                </ul>
              <p>If you have any questions, feel free to reach out to us. <br>Thank you for using our library services!
              <br><i>The Library Team</i></p>
            </div>
        """.formatted(
                c.getFirstName(), c.getLastName(),
                r.getResId(), r.getResEndDate(),
                book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getYear()
        );

        String subject = "Book Reservation Confirmation: " + book.getTitle();

        mailProperties.setBody(body);
        mailProperties.setTo(c.getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

}