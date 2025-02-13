package org.dirimo.biblioteca.zone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    // Get all zones
    public List<Zone> getAll() {
        return zoneRepository.findAll();
    }

    // Get a zone by ID
    public Optional<Zone> getById(Long id) {
        return zoneRepository.findById(id);
    }

    // Add a new zone
    public Zone create(Zone zone) {
        return zoneRepository.save(zone);
    }

    // Update a zone
    public Zone update(Long id, Zone zone) {
        zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona con id: " + id + " non trovata."));
        zone.setZoneId(id);
        return zoneRepository.save(zone);
    }

    //Delete a zone
    public void delete(Long id) {
        zoneRepository.deleteById(id);
    }

}