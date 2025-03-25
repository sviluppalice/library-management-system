package org.dirimo.biblioteca.resources.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.jms.JMSService;
import org.dirimo.biblioteca.jms.enumerated.JMSQueueType;
import org.dirimo.biblioteca.mail.MailProperties;
import org.dirimo.biblioteca.mail.MailService;
import org.dirimo.biblioteca.resources.book.Book;
import org.dirimo.biblioteca.resources.book.BookRepository;
import org.dirimo.biblioteca.resources.book.BookService;
import org.dirimo.biblioteca.resources.customer.Customer;
import org.dirimo.biblioteca.resources.customer.CustomerRepository;
import org.dirimo.biblioteca.resources.customer.CustomerService;
import org.dirimo.biblioteca.resources.reservation.action.OpenReservationAction;
import org.dirimo.biblioteca.resources.reservation.enumerated.ReservationStatus;
import org.dirimo.biblioteca.resources.stock.Stock;
import org.dirimo.biblioteca.resources.stock.StockService;
import org.dirimo.biblioteca.resources.template.TemplateService;
import org.dirimo.commonlibrary.dto.BookDTO;
import org.dirimo.commonlibrary.dto.CustomerDTO;
import org.dirimo.commonlibrary.dto.ReservationDTO;
import org.dirimo.commonlibrary.event.EventType;
import org.dirimo.commonlibrary.event.GenericModuleEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final StockService stockService;
    private final BookService bookService;
    private final CustomerService customerService;
    private final ApplicationEventPublisher eventPublisher;
    private final TemplateService templateService;
    private final MailService mailService;
    private final JMSService jmsService;

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

    // Add in bulk
    public List<Reservation> createBulk(List<OpenReservationAction> actions) {
        List<Reservation> reservations = new ArrayList<>();
        for (OpenReservationAction action : actions) {
            Reservation savedReservation = open(action.getReservation(), action.getDate());
            reservations.add(savedReservation);
        }
        return reservations;
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
                new RuntimeException("Libro con id: " + book.getId() + " non trovato.")
        );
        stock.handleQuantity(1);

        // Publishes event
        eventPublisher.publishEvent(new GenericModuleEvent<>(this, EventType.CLOSED, reservation));

        return reservation;
    }

    public Reservation open(Reservation reservation, LocalDate date) {
        // Check book existence
        Book book = bookRepository.findById(reservation.getBook().getId())
                .orElseThrow(() ->
                        new RuntimeException("Libro con id: " + reservation.getBook().getId() + " non trovato."));
        // Check customer existence
        Customer customer = customerRepository.findById(reservation.getCustomer().getId())
                .orElseThrow(() ->
                        new RuntimeException("Customer con id: " + reservation.getCustomer().getId() + " non trovato."));

        // Check stock existence
        Optional<Stock> stockOptional = stockService.getByBook(book);
        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + book.getId() + " non trovato.")
        );

        // Check copies availability and if available, creates reservation
        if (stock.getAvailableCopies() <= 0) {
            throw new RuntimeException("Non ci sono copie del libro " + book.getId() + " disponibili al momento.");
        } else {
            stock.handleQuantity(-1);
            stockService.update(stock.getStockId(), stock);

            // Set start date, customer and book and save reservation
            reservation.setBook(book);
            reservation.setCustomer(customer);
            reservation.setResStartDate(date);

            Reservation savedReservation = reservationRepository.save(reservation);

            eventPublisher.publishEvent(new GenericModuleEvent<>(this, EventType.OPENED, savedReservation));

            // Publishes event
            //eventPublisher.publishEvent(new OpenReservationEvent(this, savedReservation));
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
    public void sendExpiringReminderMail(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getId()+" non trovato."));

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

        mailService.sendMail(new MailProperties(c.getEmail(), body, subject));
    }

    public void sendExpiredNoticeMail(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getId()+" non trovato."));

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

        mailService.sendMail(new MailProperties(c.getEmail(), body, subject));
    }

    public void sendOpenReservationMail(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getId()+" non trovato."));

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

        mailService.sendMail(new MailProperties(c.getEmail(), body, subject));
    }

    public void sendCloseReservationMail(Reservation r) {
        // Get book
        Book b = bookService.getBookById(r.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Libro con id: "+r.getBook().getId()+" non trovato."));

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

        mailService.sendMail(new MailProperties(c.getEmail(), body, subject));
    }

    public static ReservationDTO fromReservation(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        BookDTO bookDTO = null;
        if (reservation.getBook() != null) {
            bookDTO = new BookDTO(
                    reservation.getBook().getId(),
                    reservation.getBook().getIsbn(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getBook().getYear(),
                    reservation.getBook().getGenre(),
                    reservation.getBook().getPublisher(),
                    reservation.getBook().getLanguage(),
                    reservation.getBook().getDescription()
            );
        }

        CustomerDTO customerDTO = null;
        if (reservation.getCustomer() != null) {
            customerDTO = new CustomerDTO(
                    reservation.getCustomer().getId(),
                    reservation.getCustomer().getFirstName(),
                    reservation.getCustomer().getLastName(),
                    reservation.getCustomer().getEmail()
            );
        }

        return new ReservationDTO(
                reservation.getResId(),
                bookDTO,
                customerDTO,
                reservation.getResStartDate(),
                reservation.getResEndDate()
        );
    }

    public void sendOpenReservationJMS(Reservation payload) {
        // Set DTO
        ReservationDTO reservationDTO = ReservationService.fromReservation(payload);

        // Converts DTO in JSON message and sends
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String reservationJson = objectMapper.writeValueAsString(reservationDTO);
            jmsService.sendMessage(JMSQueueType.QUEUE_RESERVATIONS, reservationJson);
            jmsService.sendMessage(JMSQueueType.QUEUE_CATALOG, reservationJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}