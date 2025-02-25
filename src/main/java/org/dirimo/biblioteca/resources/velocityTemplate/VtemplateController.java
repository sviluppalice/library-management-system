package org.dirimo.biblioteca.resources.velocityTemplate;

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
@RequestMapping("VelocityTemplate")
public class VtemplateController {

    private final VtemplateService vtemplateService;

    // Get all
    @GetMapping("/")
    public List<Vtemplate> getAll() {
        return vtemplateService.findAll();
    }

    // Get template by name
    @GetMapping("/{name}")
    public Vtemplate getByName(@PathVariable String name) {
        return vtemplateService.getByName(name)
                .orElseThrow(() -> new RuntimeException("Template con nome " + name + " non trovata."));
    }

    // Add a new template
    @PostMapping("/")
    public Vtemplate create(@RequestBody Vtemplate vtemplate) {
        return vtemplateService.create(vtemplate);
    }

    // Update a template
    @PutMapping("/{id}")
    public Vtemplate update(@PathVariable Long id, @RequestBody Vtemplate vtemplate) {
        return vtemplateService.update(id, vtemplate);
    }

    // Delete a template
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vtemplateService.delete(id);
    }
}
