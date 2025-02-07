package org.dirimo.biblioteca.zone;

import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.stock.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final StockRepository stockRepository;

    // Get all zones
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    // Get a zone by ID
    public Optional<Zone> getZoneById(Long id) {
        return zoneRepository.findById(id);
    }

    // Add a new zone
    public Zone saveZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    // Update a zone
    public Zone updateZone(Long id, Zone zoneDetails) {
        return zoneRepository.findById(id).map(zone -> {
            zone.setName(zoneDetails.getName());
            return zoneRepository.save(zone);
        }).orElseThrow(() -> new RuntimeException("Zone not found with id: " + id));
    }

    //Delete a zone
    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }

}