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

## ✅Testing
The project includes unit tests to ensure the functionality of core features.

## ✅Running Tests
- Run the tests to validate the code functionality and observe test coverage.
- The project ensures a minimum of 70% coverage across all methods.

## ✅Technology Stack

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

## ✅Author
Nadiia Alaieva [![izzifona](https://img.icons8.com/ios-glyphs/30/000000/github.png)](https://github.com/tizzifona)
