package org.dirimo.biblioteca.resources.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.mail.MailProperties;
import org.dirimo.biblioteca.mail.MailService;
import org.dirimo.biblioteca.resources.book.Book;
import org.dirimo.biblioteca.resources.book.BookService;
import org.dirimo.biblioteca.resources.customer.Customer;
import org.dirimo.biblioteca.resources.customer.CustomerService;
import org.dirimo.biblioteca.resources.reservation.enumerated.ReservationStatus;
import org.dirimo.biblioteca.resources.reservation.event.CloseReservationEvent;
import org.dirimo.biblioteca.resources.reservation.event.OpenReservationEvent;
import org.dirimo.biblioteca.resources.stock.Stock;
import org.dirimo.biblioteca.resources.stock.StockService;
import org.dirimo.biblioteca.resources.template.TemplateService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final TemplateService templateService;
    private final MailService mailService;

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


    // email senders
    public void sendExpiringReservationMail(Reservation r) {
        MailProperties mailProperties = buildExpiringReminderMailProperties(r);
        mailService.sendMail(mailProperties);
    }

    public void sendExpiredReservationMail(Reservation r) {
        MailProperties mailProperties = buildExpiredNoticeMailProperties(r);
        mailService.sendMail(mailProperties);
    }

    // email builders
    public MailProperties buildExpiringReminderMailProperties(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getBookId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getBookId()+" non trovato."));

        // Get customer
        Customer c = customerService.getById(r.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer con id: "+r.getCustomer().getId()+" non trovato."));

        // Velocity context
        Map<String, Object> model = new HashMap<>();
        model.put("r", r);
        model.put("b", b);
        model.put("c", c);

        String body = templateService.render("expiringReservation", model);
        String subject = "Reminder: Your Library Reservation Expires Soon! " +r.getResExpiryDate();

        return new MailProperties(c.getEmail(), body, subject);
    }

    public MailProperties buildExpiredNoticeMailProperties(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getBookId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getBookId()+" non trovato."));

        // Get customer
        Customer c = customerService.getById(r.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer con id: "+r.getCustomer().getId()+" non trovato."));

        // Velocity context
        Map<String, Object> model = new HashMap<>();
        model.put("r", r);
        model.put("b", b);
        model.put("c", c);

        String body = templateService.render("expiringReservation", model);
        String subject = "Notice: Your Library Reservation Expired on "+r.getResExpiryDate();

        return new MailProperties(c.getEmail(), body, subject);
    }

    public MailProperties buildOpenReservationMailProperties(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getBookId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getBookId()+" non trovato."));

        // Get customer
        Customer c = customerService.getById(r.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer con id: "+r.getCustomer().getId()+" non trovato."));

        // Velocity context
        Map<String, Object> context = new HashMap<>();
        context.put("r", r);
        context.put("b", b);
        context.put("c", c);

        String body = templateService.render("openReservation", context);
        String subject = "Book Reservation Confirmation: "+b.getTitle();

        return new MailProperties(c.getEmail(), body, subject);
    }

    public MailProperties buildCloseReservationMailProperties(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getBookId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getBookId()+" non trovato."));

        // Get customer
        Customer c = customerService.getById(r.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer con id: "+r.getCustomer().getId()+" non trovato."));

        // Velocity context
        Map<String, Object> context = new HashMap<>();
        context.put("r", r);
        context.put("b", b);
        context.put("c", c);

        String body = templateService.render("closeReservation", context);
        String subject = "Book Reservation Confirmation: "+b.getTitle();

        return new MailProperties(c.getEmail(), body, subject);
    }

}