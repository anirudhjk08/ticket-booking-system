# Ticket Booking System

A REST API for booking seats, built with Spring Boot. It prevents double booking when many users try to book the same seat at the same moment.

## Tech Stack
Java, Spring Boot, Spring Data JPA (Hibernate), Spring Security + JWT, PostgreSQL, JUnit 5, Mockito, Docker

## Features
- Register and log in (passwords hashed with BCrypt, JWT authentication)
- Add seats, list all seats, list available seats
- Book a seat (one booking per seat, linked to the logged-in user)
- View my bookings
- Consistent JSON error responses (400, 401, 404, 409)

## How double booking is prevented
Two layers protect each seat:
1. **Optimistic locking:** `Seat` has a `@Version` column. An update only succeeds if the version is unchanged, so only one of several concurrent updates wins.
2. **Unique constraint:** `bookings.seat_id` is unique, so the database refuses a second booking for the same seat.

I proved this with a concurrency test: 10 threads (using `CountDownLatch` so they start together) book the same seat. Result: **1 success, 9 rejected**. Without `@Version` and the constraint, all 10 succeeded.

## Architecture
Controller -> Service -> Repository -> PostgreSQL. DTOs are used for requests and responses, and a `@RestControllerAdvice` maps exceptions to HTTP status codes.

## API
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | /api/auth/register | No | Create a user |
| POST | /api/auth/login | No | Get a JWT |
| POST | /api/seats | Yes | Add a seat |
| GET | /api/seats | Yes | List all seats |
| GET | /api/seats/available | Yes | List unbooked seats |
| POST | /api/seats/{id}/book | Yes | Book a seat |
| GET | /api/bookings/my | Yes | My bookings |

Send the token as `Authorization: Bearer <token>`.

## Run with Docker
```
cp .env.example .env     # then edit the values
docker compose up --build
```
The API is at http://localhost:8080.

## Run locally
1. Create a PostgreSQL database.
2. Copy `application-example.properties` to `application.properties` and fill in your values.
3. Run `DemoApplication`.

## Tests
- `BookingServiceTest`: unit tests with Mockito
- `SeatConcurrencyTest`: integration test proving only one thread can book a seat

## Problems I hit and fixed
- **Leftover columns:** `ddl-auto=update` never drops columns, so an old `NOT NULL` column broke inserts. Fixed by dropping the stale table.
- **Duplicate seat numbers:** added a service check plus a unique DB constraint (the check alone is not atomic).
- **Startup failure from a derived query:** `findByUsername` did not exist on `Booking`; the correct name follows the relationship, `findByUserUsername`.
- **Concurrency test counting wrongly:** the unique constraint fired before the version check, so the test also had to expect `DataIntegrityViolationException`.
- **Old booking route still active:** a leftover controller method skipped creating booking rows; removed it so one service owns booking logic.
- **Docker networking:** the app container must reach Postgres at host `db`, not `localhost`.