package projects.f5.airlines.flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import projects.f5.airlines.airport.Airport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FlightTest {
    private Flight flight;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private Boolean isAvailable;
    private BigDecimal price;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport();
        arrivalAirport = new Airport();
        departureTime = LocalDateTime.now();
        arrivalTime = departureTime.plusHours(2);
        availableSeats = 150;
        isAvailable = true;
        price = BigDecimal.valueOf(199.99);

        flight = new Flight(1L, departureAirport, arrivalAirport, departureTime, arrivalTime, availableSeats,
                isAvailable, price);
    }

    @Test
    void testGetId() {
        assertThat(flight.getId()).isEqualTo(1L);
    }

    @Test
    void testSetId() {
        flight.setId(2L);
        assertThat(flight.getId()).isEqualTo(2L);
    }

    @Test
    void testGetDepartureAirport() {
        assertThat(flight.getDepartureAirport()).isEqualTo(departureAirport);
    }

    @Test
    void testSetDepartureAirport() {
        Airport newAirport = new Airport();
        flight.setDepartureAirport(newAirport);
        assertThat(flight.getDepartureAirport()).isEqualTo(newAirport);
    }

    @Test
    void testGetArrivalAirport() {
        assertThat(flight.getArrivalAirport()).isEqualTo(arrivalAirport);
    }

    @Test
    void testSetArrivalAirport() {
        Airport newAirport = new Airport();
        flight.setArrivalAirport(newAirport);
        assertThat(flight.getArrivalAirport()).isEqualTo(newAirport);
    }

    @Test
    void testGetDepartureTime() {
        assertThat(flight.getDepartureTime()).isEqualTo(departureTime);
    }

    @Test
    void testSetDepartureTime() {
        LocalDateTime newTime = departureTime.plusDays(1);
        flight.setDepartureTime(newTime);
        assertThat(flight.getDepartureTime()).isEqualTo(newTime);
    }

    @Test
    void testGetArrivalTime() {
        assertThat(flight.getArrivalTime()).isEqualTo(arrivalTime);
    }

    @Test
    void testSetArrivalTime() {
        LocalDateTime newTime = arrivalTime.plusDays(1);
        flight.setArrivalTime(newTime);
        assertThat(flight.getArrivalTime()).isEqualTo(newTime);
    }

    @Test
    void testGetAvailableSeats() {
        assertThat(flight.getAvailableSeats()).isEqualTo(availableSeats);
    }

    @Test
    void testSetAvailableSeats() {
        flight.setAvailableSeats(200);
        assertThat(flight.getAvailableSeats()).isEqualTo(200);
    }

    @Test
    void testGetIsAvailable() {
        assertThat(flight.getIsAvailable()).isTrue();
    }

    @Test
    void testSetIsAvailable() {
        flight.setIsAvailable(false);
        assertThat(flight.getIsAvailable()).isFalse();
    }

    @Test
    void testGetPrice() {
        assertThat(flight.getPrice()).isEqualTo(price);
    }

    @Test
    void testSetPrice() {
        BigDecimal newPrice = BigDecimal.valueOf(299.99);
        flight.setPrice(newPrice);
        assertThat(flight.getPrice()).isEqualTo(newPrice);
    }
}