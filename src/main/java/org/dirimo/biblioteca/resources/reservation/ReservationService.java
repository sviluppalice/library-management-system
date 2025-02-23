package org.dirimo.biblioteca.resources.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
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

import java.io.StringWriter;
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
    private final VelocityEngine velocityEngine;

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

        // Get book
        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book b = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        // Get customer
        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        // Velocity context and merge
        VelocityContext context = new VelocityContext();
        context.put("r", r);
        context.put("b", b);
        context.put("c", c);
        StringWriter writer = new StringWriter();
        velocityEngine.getTemplate("templates/expiringReservationEmailTemplate.vm").merge(context, writer);

        String subject = "Reminder: Your Library Reservation Expires Soon! " + r.getResExpiryDate();

        // set mailProperties
        mailProperties.setBody(writer.toString());
        mailProperties.setTo(r.getCustomer().getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

    public MailProperties buildExpiredNoticeMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        // Get book
        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book b = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        // Get customer
        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        // Velocity context and merge
        VelocityContext context = new VelocityContext();
        context.put("r", r);
        context.put("b", b);
        context.put("c", c);
        StringWriter writer = new StringWriter();
        velocityEngine.getTemplate("templates/expiredReservationEmailTemplate.vm").merge(context, writer);

        String subject = "Notice: Your Library Reservation Expired on " + r.getResExpiryDate();

        // set mailProperties
        mailProperties.setBody(writer.toString());
        mailProperties.setTo(c.getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

    public MailProperties buildOpenReservationMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        // Get book
        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book b = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        // Get customer
        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        // Velocity context and merge
        VelocityContext context = new VelocityContext();
        context.put("r", r);
        context.put("b", b);
        context.put("c", c);
        StringWriter writer = new StringWriter();
        velocityEngine.getTemplate("templates/openReservationEmailTemplate.vm").merge(context, writer);

        String subject = "Book Reservation Confirmation: " + b.getTitle();

        // set mailProperties
        mailProperties.setBody(writer.toString());
        mailProperties.setTo(c.getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }

    public MailProperties buildCloseReservationMailProperties(Reservation r) {
        MailProperties mailProperties = new MailProperties();

        // Get book
        Optional<Book> bookOptional = bookService.getBookById(r.getBook().getBookId());
        Book b = bookOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + r.getBook().getBookId() + " non trovato.")
        );

        // Get customer
        Optional<Customer> customerOptional = customerService.getById(r.getCustomer().getId());
        Customer c = customerOptional.orElseThrow(() ->
                new RuntimeException("Customer con id: " + r.getCustomer().getId() + " non trovato.")
        );

        // Velocity context and merge
        VelocityContext context = new VelocityContext();
        context.put("r", r);
        context.put("b", b);
        context.put("c", c);
        StringWriter writer = new StringWriter();
        velocityEngine.getTemplate("templates/closeReservationEmailTemplate.vm").merge(context, writer);

        String subject = "Book Reservation Confirmation: " + b.getTitle();

        // set mailProperties
        mailProperties.setBody(writer.toString());
        mailProperties.setTo(c.getEmail());
        mailProperties.setSubject(subject);

        return mailProperties;
    }
}