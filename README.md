# Crud-kotlin
# Travel Agency Project

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
    - [Clone the Repository](#clone-the-repository)
    - [Database Configuration](#database-configuration)
    - [Build and Run the Application](#build-and-run-the-application)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This project is a simple travel agency application built using Kotlin, Spring Boot, and a combination of HTML, CSS, and JavaScript for the frontend. The application allows users to book flights, hotels, and destinations, providing a comprehensive platform for managing travel bookings.

## Technologies Used

- **Backend:**
    - Kotlin
    - Spring Boot
    - Spring Data JPA
    - H2 Database (for development)
    - MySQL/PostgreSQL (for production)

- **Frontend:**
    - HTML
    - CSS
    - JavaScript

- **Development Environment:**
    - IntelliJ IDEA

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- Java JDK 11 or higher
- IntelliJ IDEA
- Maven
- Git
- A web browser

## Endpoints

### User Endpoints

- `GET /users`: Retrieve all users.
- `GET /users/{id}`: Retrieve a user by ID.
- `POST /users`: Create a new user.
- `PUT /users/{id}`: Update an existing user.
- `DELETE /users/{id}`: Delete a user.

### Booking Endpoints

- `GET /bookings`: Retrieve all bookings.
- `GET /bookings/{id}`: Retrieve a booking by ID.
- `POST /bookings`: Create a new booking.
- `PUT /bookings/{id}`: Update an existing booking.
- `DELETE /bookings/{id}`: Delete a booking.

### Destination Endpoints

- `GET /destinations`: Retrieve all destinations.
- `GET /destinations/{id}`: Retrieve a destination by ID.
- `POST /destinations`: Create a new destination.
- `PUT /destinations/{id}`: Update an existing destination.
- `DELETE /destinations/{id}`: Delete a destination.

### Hotel Endpoints

- `GET /hotels`: Retrieve all hotels.
- `GET /hotels/{id}`: Retrieve a hotel by ID.
- `POST /hotels`: Create a new hotel.
- `PUT /hotels/{id}`: Update an existing hotel.
- `DELETE /hotels/{id}`: Delete a hotel.

### Flight Endpoints

- `GET /flights`: Retrieve all flights.
- `GET /flights/{id}`: Retrieve a flight by ID.
- `POST /flights`: Create a new flight.
- `PUT /flights/{id}`: Update an existing flight.
- `DELETE /flights/{id}`: Delete a flight.
