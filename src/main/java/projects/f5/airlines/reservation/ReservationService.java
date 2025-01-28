package projects.f5.airlines.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import projects.f5.airlines.airport.Airport;
import projects.f5.airlines.flight.Flight;
import projects.f5.airlines.flight.FlightDto;
import projects.f5.airlines.flight.FlightService;
import projects.f5.airlines.user.User;
import projects.f5.airlines.user.UserService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightService flightService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, FlightService flightService,
            UserService userService) {
        this.reservationRepository = reservationRepository;
        this.flightService = flightService;
        this.userService = userService;
    }

    public List<Reservation> findAllByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public ReservationDto create(Long userId, Long flightId, ReservationDto reservationDto) {
        FlightDto flightDto = flightService.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Flight flight = convertToEntity(flightDto);

        if (flight.getAvailableSeats() < reservationDto.seatsReserved()) {
            throw new RuntimeException("Not enough available seats");
        }

        flightService.updateSeats(flightId, new SeatUpdateDto(reservationDto.seatsReserved()));

        Reservation reservation = new Reservation(
                user,
                flight,
                reservationDto.seatsReserved(),
                BigDecimal.ZERO,
                ReservationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15));

        reservation = reservationRepository.save(reservation);
        return convertToDto(reservation);
    }

    private Flight convertToEntity(FlightDto flightDto) {
        Airport departureAirport = new Airport();
        departureAirport.setCode(flightDto.departureAirport());

        Airport arrivalAirport = new Airport();
        arrivalAirport.setCode(flightDto.arrivalAirport());

        return new Flight(
                flightDto.id(),
                departureAirport,
                arrivalAirport,
                flightDto.departureTime(),
                flightDto.arrivalTime(),
                flightDto.availableSeats(),
                flightDto.isAvailable(),
                flightDto.price());
    }

    public void confirmReservation(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        });
    }

    private ReservationDto convertToDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getFlight().getId(),
                reservation.getSeatsReserved());
    }

}
