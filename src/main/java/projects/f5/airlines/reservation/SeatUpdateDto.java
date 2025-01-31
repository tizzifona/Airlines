package projects.f5.airlines.reservation;

public class SeatUpdateDto {
    private int bookedSeats;

    public SeatUpdateDto(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}
