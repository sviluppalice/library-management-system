package org.dirimo.biblioteca.mail;

import lombok.AllArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

@Service
@AllArgsConstructor
public class VelocityTemplateService {
    private final VelocityEngine velocityEngine;

    public String render(String templateName, Map<String, Object> model) {
        Template template = velocityEngine.getTemplate("templates/" + templateName + ".vm");
        VelocityContext context = new VelocityContext(model);

        model.forEach(context::put);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }
}
