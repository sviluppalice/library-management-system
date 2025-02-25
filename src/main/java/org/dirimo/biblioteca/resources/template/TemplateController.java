package org.dirimo.biblioteca.resources.template;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("Template")
public class TemplateController {

    private final TemplateService templateService;

    // Get all
    @GetMapping("/")
    public List<Template> getAll() {
        return templateService.findAll();
    }

    // Get template by name
    @GetMapping("/{name}")
    public Template getByName(@PathVariable String name) {
        return templateService.getByName(name)
                .orElseThrow(() -> new RuntimeException("Template con nome " + name + " non trovata."));
    }

    // Add a new template
    @PostMapping("/")
    public Template create(@RequestBody Template template) {
        return templateService.create(template);
    }

    // Update a template
    @PutMapping("/{id}")
    public Template update(@PathVariable Long id, @RequestBody Template template) {
        return templateService.update(id, template);
    }

    // Delete a template
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        templateService.delete(id);
    }
}
