package org.dirimo.biblioteca.resources.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableAsync
@AllArgsConstructor
@Component
public class BookScheduler {

    public final BookService bookService;

    @Scheduled(cron = "0 0 21 * * *")
    public void updateCatalog() {
        bookService.sendCatalog();
    }
}
