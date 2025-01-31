package projects.f5.airlines.flight;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f " +
            "WHERE (:departureAirport IS NULL OR f.departureAirport.code = :departureAirport) " +
            "AND (:arrivalAirport IS NULL OR f.arrivalAirport.code = :arrivalAirport) " +
            "AND (:departureDate IS NULL OR DATE(f.departureTime) = DATE(:departureDate)) " +
            "AND (:requiredSeats IS NULL OR f.availableSeats >= :requiredSeats) " +
            "AND f.isAvailable = true")
    List<Flight> findAvailableFlights(
            String departureAirport,
            String arrivalAirport,
            LocalDateTime departureDate,
            Integer requiredSeats);

    @Modifying
    @Query("UPDATE Flight f SET f.isAvailable = false " +
            "WHERE (f.departureTime < :now OR f.availableSeats = 0) " +
            "AND f.isAvailable = true")
    void updateFlightStatus(LocalDateTime now);

}
