# Airlines

## ✅Project Description

This project aims to develop a comprehensive management system for an airline using Spring, Spring Boot, and Spring Security. The system will allow for the management of users, flights, bookings, and destinations, with advanced functionalities such as secure authentication via Basic Auth. The system will prevent flight selection if there are no available seats or if the booking date has passed. The project will be implemented using Java 21, Maven, and MySQL.

## ✅Project Goals:

- Strengthen concepts of API development.
- Apply database relationships.
- Solidify knowledge of login functionality with Spring Security and either Basic Auth.

## ✅Functional Requirements:

1. Client Management:
- Registration, authentication, and role management (ROLE_ADMIN and ROLE_USER).
- Generation and validation of session COOKIES if Basic Auth is used for secure sessions.

2. Flight Management:
- Flights must be generated automatically in the database upon compilation using an .sql file.
- The flight status should change to "false" when there are no available seats or when the flight date has passed.

3. Search Functionality:
- Users will be able to provide the departure and destination airports, date, and the number of seats to reserve.
- Cataloging the type of seats is not required.
  
4. Booking Management:
- Reservations can only be made if the selected flight route exists and there are available seats.
- The system must verify availability before confirming the booking.
- Once the booking process is initiated, the system will block the seats for 15 minutes to ensure seat availability.

5. Admin (ROLE_ADMIN) Authorized Actions:
- CRUD operations for airports.
- CRUD operations for flight routes.
- Summary list of reservations made by clients.
- Access to the reservation history of each user (ROLE_USER).
  
6. Client (ROLE_USER) Authorized Actions:
- Register as a new user.
- Upload a profile picture; if not uploaded, a default image will be shown.
- Login functionality.
- View a list of their bookings with flight details.
- Clients cannot make a reservation without logging in.
  
7. Exception Management:
- Handle exceptions with custom error messages and handling mechanisms.

## ✅Installation Steps
1. Clone the repository:
``git clone https://github.com/tizzifona/Airlines``

2. Navigate into the project directory:
``cd <project_directory>``

3. Install dependencies:
``mvn install``

4. Set up the database (MySQL) and configure the connection in application.properties.
5. Run the application:
``mvn spring-boot:run``

6. Access the system via the provided endpoint and login with the registered user credentials.

## ✅API Endpoints

🟢 1. Authentication 

- POST http://localhost:8080/api/login  - Login (USER, ADMIN)
- POST http://localhost:8080/api/logout  - Logout (USER, ADMIN)
- POST http://localhost:8080/api/register  - Registration ( Default- role USER)

🟢 2. Airports 

- GET http://localhost:8080/api/airports  - Get ALL Airports (ADMIN)
- GET http://localhost:8080/api/airports/{id}  - Get Airport by id (ADMIN)
- POST http://localhost:8080/api/airports  - Add new Airport (ADMIN)
- PUT http://localhost:8080/api/airports/{id}  - Update Airport (ADMIN)
- DEL http://localhost:8080/api/airports/{id}  - Delete Airport (ADMIN)

🟢 3. Flights 

- GET http://localhost:8080/api/flights  - Get ALL Flights (ADMIN)
- GET http://localhost:8080/api/flights/{id}  - Get Flight by id (ADMIN)
- GET http://localhost:8080/api/flights/search?departureCode={departureCode}   - Search Flight by Departure Airport (ADMIN, USER)
- GET http://localhost:8080/api/flights/search?departureCode={departureCode}&arrivalCode={arrivalCode}   - Search Flight by Departure and Arrival Airport (ADMIN, USER)
- GET http://localhost:8080/api/flights/search?departureCode={departureCode}&arrivalCode={arrivalCode}&departureDate={departureDate}&numberOfSeats={numberOfSeats}   - Search Flight by Departure Airport, Arrival Airport, Departure Date, Number of Seats (ADMIN, USER)
- GET http://localhost:8080/api/flights/search?departureDate={departureDate}&numberOfSeats={numberOfSeats}  - Search Flight by Departure Date and Number of Seats (ADMIN, USER)
- POST http://localhost:8080/api/flights  - Add new Flight (ADMIN)
- PUT http://localhost:8080/api/flights/{id}/seats  - Update Booked Seats on the Flight (ADMIN) PUT http://localhost:8080/api/flights/{id}/availability  - Update Availability of Flight (ADMIN)
- DEL http://localhost:8080/api/flights/{id}  - Delete Flight (ADMIN)

🟢 4. Users 

- GET http://localhost:8080/api/users  - Get ALL Users (ADMIN)
- GET http://localhost:8080/api/users/{id}  - Get User by ID (ADMIN, USER(only owner))
- PUT http://localhost:8080/api/users/{id}  - Update User by ID (ADMIN)
- DEL http://localhost:8080/api/users/{id}  - Delete User by ID (ADMIN)
- POST http://localhost:8080/api/users/{id}/upload  - Upload User Profile Photo (USER (only owner))

🟢 5. Reservations

- GET http://localhost:8080/api/reservations  - Get ALL Reservations (ADMIN)
- GET http://localhost:8080/api/reservations/{id}  - Get Reservation by ID (ADMIN)
- POST http://localhost:8080/api/reservations/create  - Add new Reservation (USER)
- POST http://localhost:8080/api/reservations/{id}/confirm  - Confirm Reservation (USER(only owner))
- DEL http://localhost:8080/api/reservations/{id}  - Delete Reservation by ID (ADMIN)
- GET http://localhost:8080/api/reservations/my-reservations  - Get All Reservations for User (USER (only owner))


## ✅Testing
The project includes unit tests to ensure the functionality of core features.

## ✅Running Tests
- Run the tests to validate the code functionality and observe test coverage.
- The project ensures a minimum of 70% coverage across all methods.

[![temp-Image-Aoz9-Or.avif](https://i.postimg.cc/k425sNqC/temp-Image-Aoz9-Or.avif)](https://postimg.cc/9RjVF7Dg)

## ✅Technology Stack

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

## ✅Diagrams of the project:

1. Class diagram

[![temp-Imagepa6-WP7.avif](https://i.postimg.cc/NMcN7D19/temp-Imagepa6-WP7.avif)](https://postimg.cc/bGmHqQVp)

2. Detailed diagrams of every class:

- [Flight](https://postimg.cc/64nv4Rc7)

- [Reservation](https://postimg.cc/gxT2djy1)

- [Airport](https://postimg.cc/SnqvsfdM)

- [User](https://postimg.cc/wtj45xSx)

- [UserRole](https://postimg.cc/phg0tfyy)

- [Authentication](https://postimg.cc/XBdsNryL)


## ✅Data Base Schema

[![temp-Imagei-BUUR6.avif](https://i.postimg.cc/Xv0hQnWY/temp-Imagei-BUUR6.avif)](https://postimg.cc/nX00LbMy)

[![temp-Image-SCedhm.avif](https://i.postimg.cc/9MLnmT7D/temp-Image-SCedhm.avif)](https://postimg.cc/bD2TLZLP)

## ✅Author
Nadiia Alaieva [![izzifona](https://img.icons8.com/ios-glyphs/30/000000/github.png)](https://github.com/tizzifona)


## ✅Disclaimer
This project is developed as part of a bootcamp learning experience and is intended for educational purposes only. The creators and contributors are not responsible for any issues, damages, or losses that may occur from using this code.

This project is not meant for commercial use, and any trademarks or references to third-party services (such as Funko) belong to their respective owners. By using this code, you acknowledge that it is a work in progress, created by learners, and comes without warranties or guarantees of any kind.

Use at your own discretion and risk.

Thank You! ❤️
