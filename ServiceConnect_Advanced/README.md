# ServiceConnect — full-stack Java + MySQL MVP

ServiceConnect is a working full-stack service marketplace starter project. It has a Spring Boot REST backend, a browser frontend, MySQL persistence, role-aware customer/provider/admin workflows, appointment capacity rules, provider verification, private file uploads, distance-based search, and Docker deployment files.

> This is a functional college-project MVP, not a production-certified marketplace. Before accepting real medical records, identity documents, or payments, add a security/privacy review, encryption and retention policies, audited access, malware scanning, backups, rate limiting, and legally appropriate consent. The app does not medically verify uploaded reports. SMS/email delivery and real-time GPS tracking are not connected in this version.

## Features implemented

### Customer
- Register/login with a customer account.
- Search active services by keyword, category, and city.
- Use browser location and a 1–50 km search radius; results with coordinates are distance-filtered and sorted nearest-first.
- See provider profile, experience, service price, phone, address, verification state, and online state.
- Call via `tel:` links and open Google Maps directions links.
- Request a future appointment at provider location or customer's address.
- Enter problem details and optionally upload images/PDF reports after submitting.
- Receive a unique `SC-xxxxxx` appointment code and track request status/deadline.
- Cancel pending or approved bookings; capacity is released.
- View invoices created after provider approval.

### Provider
- Register a provider account and submit a business/professional profile.
- Enter services, category, description, qualifications, experience, address, coordinates, home-service radius, and delivery modes.
- Upload verification documents (PDF/image, up to 10 MB each).
- Add/deactivate services and set price/duration.
- Configure working days, hours, break, slot duration, and daily appointment capacity.
- Update online status and location with browser geolocation.
- View requests and uploaded customer attachments; approve/reject requests.
- View the F / NC / A / CL daily calendar and invoices; mark invoices paid.

### Admin
- Admin account is bootstrapped from environment variables.
- View provider applications and submitted documents.
- Mark providers verified, under review, rejected, or request resubmission.
- View basic platform counts.

### Booking rules
- A booking request reserves the selected time and counts against daily capacity while pending.
- Provider must approve within 24 hours. A scheduled server-side task expires unanswered pending requests and releases their capacity.
- Rejected, cancelled, and expired requests no longer occupy capacity.
- The backend checks working day, business hours, break time, slot alignment, overlap, daily capacity, service delivery mode, and (when customer coordinates are supplied) provider home-service radius.
- Provider calendar status: `F` = full; `NC` = partially booked/not closed; `A` = no appointments; `CL` = closed.

## Requirements
- Java 17 or newer
- Maven 3.8+
- MySQL 8+
- VS Code with Extension Pack for Java (recommended)

## Option A — Run locally from VS Code

1. Install Java 17+ and Maven. Install MySQL 8 and start its service.
2. Open MySQL Workbench or a MySQL terminal and run:

   ```sql
   CREATE DATABASE serviceconnect;
   ```

3. Open this `ServiceConnect_Advanced` folder in VS Code.
4. Open `src/main/resources/application.properties`. Set `spring.datasource.username` and `spring.datasource.password` for your MySQL account. The default username is `root` and password is blank; change it to match your local MySQL setup.
5. On Windows, install Maven and add its `bin` folder to PATH. Open the VS Code terminal in the project folder (Terminal → New Terminal) and run:

   ```bash
   mvn spring-boot:run
   ```

6. Open <http://localhost:8080>.

The first Maven run downloads dependencies and needs internet access. JPA creates/updates the tables automatically (`ddl-auto=update`).

## Option B — Run the complete stack with Docker

1. Install Docker Desktop and start it.
2. In the project folder, copy `.env.example` to `.env` and change all passwords.
3. Run:

   ```bash
   docker compose up --build
   ```

4. Open <http://localhost:8080>.
5. Stop with `Ctrl+C`, then run `docker compose down`. Database and uploads persist in Docker volumes. Do not use `docker compose down -v` unless you intend to delete the database and uploads.

## First-time test flow

1. Create a **Provider** account from the registration form.
2. Log in with that provider account.
3. Complete the provider profile, add coordinates (or use the location fields), upload relevant verification documents, add one or more services, and save availability.
4. Log out. Log in as admin using `ADMIN_EMAIL` and `ADMIN_PASSWORD` from your environment. Local defaults are `admin@serviceconnect.local` / `ChangeMe123!` if you did not change them.
5. In Admin → Provider verification, open the documents and mark the provider `VERIFIED`.
6. Log out and register/login as a **Customer**.
7. Allow browser location or enter the provider city, search the service, choose a radius, select an available date/time, enter the problem and request the appointment.
8. Log back in as provider and approve/reject it. A new invoice is generated on approval.
9. Test cancellation and confirm the slot/calendar capacity is released.

## Public deployment outline

The included Dockerfile builds the application and `docker-compose.yml` runs the app + MySQL locally. For a public URL, use a host that supports Docker containers and a managed MySQL database (for example, a VPS or a container platform plus managed MySQL):

1. Push this project to a private Git repository.
2. Provision MySQL 8 and note its JDBC URL, database name, username, and password.
3. Deploy the Dockerfile as a web service; set `PORT`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `ADMIN_EMAIL`, `ADMIN_PASSWORD`, and `UPLOAD_DIR` environment variables.
4. Attach persistent storage to `UPLOAD_DIR` (or replace local uploads with private object storage). Attach a persistent database volume or use managed MySQL.
5. Enable HTTPS. Restrict admin access, rotate secrets, configure backups, and test file access from two separate accounts before sharing the URL.
6. Set `spring.jpa.hibernate.ddl-auto=validate` after you introduce reviewed production migrations; do not leave schema auto-update enabled indefinitely in a production system.

A public website needs persistent storage for uploaded documents. Ephemeral container disks can lose uploaded files after redeploy. Do not publish the default admin password or commit `.env`.

## Important limitations / next engineering steps

- Search distance uses straight-line (Haversine) distance between coordinates, not road travel distance or traffic-aware ETA.
- Customer selects a specific provider from nearest-first search results. Automatic Uber-style sequential dispatch to providers is not implemented yet.
- Location updates are sent when the provider presses the location/online button; continuous background tracking and live map movement are not implemented.
- Appointment code is displayed in the app; no SMS OTP gateway is connected.
- No integrated online payment gateway, email/SMS notifications, chat, reviews/ratings, or medical diagnosis is included.
- Local upload storage is suitable for local testing only. Secure production file storage and data protection work is required before real sensitive records are used.
- This MVP uses cookie-based server sessions and disables CSRF for the JSON API to simplify local testing. Before public production use, add a proper CSRF strategy, stronger security headers, login throttling, account recovery, audit logs, and session controls.
- Appointment approval is automatically expired by a scheduler that runs once per minute. A pending request's 24-hour deadline is stored in MySQL and survives application restarts.

## Main source layout

- `model/` — JPA entities (`UserAccount`, `ProviderProfile`, `ServiceOffering`, `AvailabilitySettings`, `Appointment`, `AppointmentAttachment`, `ProviderDocument`, `Invoice`).
- `repo/` — Spring Data repository interfaces.
- `service/BookingService.java` — booking validation, slot generation, capacity checks, and expiry.
- `web/ApiController.java` — REST endpoints for accounts, search, provider settings, bookings, files, invoices, and admin verification.
- `resources/static/` — browser interface.
