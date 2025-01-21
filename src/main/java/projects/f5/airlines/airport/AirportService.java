package projects.f5.airlines.airport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class AirportService {
    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<AirportDto> findAll() {
        return airportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<AirportDto> findById(Long id) {
        return airportRepository.findById(id)
                .map(this::convertToDto);
    }

    public AirportDto create(AirportDto airportDto) {
        Airport airport = convertToEntity(airportDto);
        Airport savedAirport = airportRepository.save(airport);
        return convertToDto(savedAirport);
    }

    public AirportDto update(Long id, AirportDto updatedAirportDto) {
        return airportRepository.findById(id)
                .map(existingAirport -> {
                    existingAirport.setCode(updatedAirportDto.code());
                    existingAirport.setName(updatedAirportDto.name());
                    existingAirport.setCity(updatedAirportDto.city());
                    existingAirport.setCountry(updatedAirportDto.country());
                    return convertToDto(airportRepository.save(existingAirport));
                }).orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    public void delete(Long id) {
        airportRepository.deleteById(id);
    }

    private AirportDto convertToDto(Airport airport) {
        return new AirportDto(
                airport.getId(),
                airport.getCode(),
                airport.getName(),
                airport.getCity(),
                airport.getCountry());
    }

    private Airport convertToEntity(AirportDto airportDto) {
        return new Airport(
                airportDto.id(),
                airportDto.code(),
                airportDto.name(),
                airportDto.city(),
                airportDto.country());
    }
}
