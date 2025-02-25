package org.dirimo.biblioteca.resources.velocityTemplate;

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
public class VtemplateService {

    private final VelocityEngine velocityEngine;
    private final VtemplateRepository vtemplateRepository;

    // Get all
    public List<Vtemplate> findAll() {
        return vtemplateRepository.findAll();
    }

    // Get by name
    public Optional<Vtemplate> getByName(String name) {
        return vtemplateRepository.findByName(name);
    }

    // Create template
    public Vtemplate create(Vtemplate vtemplate) {
        return vtemplateRepository.save(vtemplate);
    }

    // Update template
    public Vtemplate update(Long id, Vtemplate vtemplate) {
        vtemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VelocityTemplate con id: " + id + " non trovata"));
        return vtemplateRepository.save(vtemplate);
    }

    // Delete template
    public void delete(Long id) {
        vtemplateRepository.deleteById(id);
    }

    // Puts together template and data
    public String render(String templateName, Map<String, Object> model) {
        Vtemplate template = vtemplateRepository.findByName(templateName)
                .orElseThrow(() -> new RuntimeException("Template con nome: "+templateName+" non trovato."));

        String templateBody = template.getBody();

        VelocityContext context = new VelocityContext(model);
        model.forEach(context::put);

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, "VelocityTemplate", new StringReader(templateBody));

        return writer.toString();
    }
}
