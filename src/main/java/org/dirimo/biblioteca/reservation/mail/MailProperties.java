package org.dirimo.biblioteca.reservation.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailProperties {

    private String to;
    private String body;
    private String subject;
}
