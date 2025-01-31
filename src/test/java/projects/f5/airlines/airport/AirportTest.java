package projects.f5.airlines.airport;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AirportTest {
    @Test
    void testGetCity() {
        Airport airport = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        assertEquals("New York", airport.getCity());
    }

    @Test
    void testGetCode() {
        Airport airport = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        assertEquals("JFK", airport.getCode());
    }

    @Test
    void testGetCountry() {
        Airport airport = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        assertEquals("USA", airport.getCountry());
    }

    @Test
    void testGetId() {
        Airport airport = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        assertEquals(1L, airport.getId());
    }

    @Test
    void testGetName() {
        Airport airport = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        assertEquals("John F. Kennedy International Airport", airport.getName());
    }

    @Test
    void testSetCity() {
        Airport airport = new Airport();
        airport.setCity("Los Angeles");
        assertEquals("Los Angeles", airport.getCity());
    }

    @Test
    void testSetCode() {
        Airport airport = new Airport();
        airport.setCode("LAX");
        assertEquals("LAX", airport.getCode());
    }

    @Test
    void testSetCountry() {
        Airport airport = new Airport();
        airport.setCountry("Canada");
        assertEquals("Canada", airport.getCountry());
    }

    @Test
    void testSetId() {
        Airport airport = new Airport();
        airport.setId(2L);
        assertEquals(2L, airport.getId());
    }

    @Test
    void testSetName() {
        Airport airport = new Airport();
        airport.setName("Los Angeles International Airport");
        assertEquals("Los Angeles International Airport", airport.getName());
    }
}