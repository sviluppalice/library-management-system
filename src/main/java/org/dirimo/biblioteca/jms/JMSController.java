package org.dirimo.biblioteca.jms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JMSController {

    private final JMSService JMSService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        JMSService.sendMessage(message);
        return "Message sent" + message;
    }
}