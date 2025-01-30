package projects.f5.airlines.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import projects.f5.airlines.flight.Flight;
import projects.f5.airlines.flight.FlightRepository;
import projects.f5.airlines.user.User;
import projects.f5.airlines.user.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public ReservationService(ReservationRepository reservationRepository, FlightRepository flightRepository,
            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public List<Reservation> findAllByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public ResponseEntity<String> createReservation(ReservationDto reservationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user = userOptional.get();
        Optional<Flight> flightOptional = flightRepository.findById(reservationDto.flightId());

        if (flightOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Flight not found");
        }

        Flight flight = flightOptional.get();
        if (flight.getAvailableSeats() < reservationDto.seatsReserved()) {
            return ResponseEntity.badRequest().body("Not enough seats available");
        }

        BigDecimal totalPrice = getRandomPricePerSeat().multiply(BigDecimal.valueOf(reservationDto.seatsReserved()));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedExpirationTime = expirationTime.format(formatter);

        Reservation reservation = new Reservation(user, flight, reservationDto.seatsReserved(), totalPrice,
                ReservationStatus.PENDING, now, expirationTime);
        reservationRepository.save(reservation);

        flight.setAvailableSeats(flight.getAvailableSeats() - reservationDto.seatsReserved());
        flightRepository.save(flight);

        return ResponseEntity.status(201)
                .body("Reservation created successfully. It will expire at: " + formattedExpirationTime);
    }

    @SuppressWarnings("deprecation")
    public BigDecimal getRandomPricePerSeat() {
        double price = 50 + (random.nextDouble() * (500 - 50));
        return BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void confirmReservation(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        });
    }

}
