package org.dirimo.biblioteca.resources.template;

import lombok.RequiredArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final VelocityEngine velocityEngine;
    private final TemplateRepository templateRepository;

    // Get all
    public List<Template> findAll() {
        return templateRepository.findAll();
    }

    // Get by name
    public Optional<Template> getByName(String name) {
        return templateRepository.findByName(name);
    }

    // Create template
    public Template create(Template template) {
        return templateRepository.save(template);
    }

    // Update template
    public Template update(Long id, Template template) {
        templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VelocityTemplate con id: " + id + " non trovata"));
        return templateRepository.save(template);
    }

    // Delete template
    public void delete(Long id) {
        templateRepository.deleteById(id);
    }

    // Puts together template and data
    public String render(String templateName, Map<String, Object> model) {
        Template template = templateRepository.findByName(templateName)
                .orElseThrow(() -> new RuntimeException("Template con nome: "+templateName+" non trovato."));

        String templateBody = template.getBody();

        VelocityContext context = new VelocityContext(model);
        model.forEach(context::put);

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, "VelocityTemplate", new StringReader(templateBody));

        return writer.toString();
    }
}
