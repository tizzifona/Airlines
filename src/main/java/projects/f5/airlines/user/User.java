package projects.f5.airlines.user;

import jakarta.persistence.*;
import projects.f5.airlines.reservation.Reservation;
import projects.f5.airlines.role.UserRole;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String profileImage;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservations;

    public User(Long id, String username, String password, String profileImage, Set<UserRole> roles,
            Set<Reservation> reservations) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.roles = roles;
        this.reservations = reservations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

}
