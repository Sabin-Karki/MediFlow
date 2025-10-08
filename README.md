# MediFlow Data Pipeline

MediFlow is a robust Spring Boot data pipeline designed to ingest, process, and store health records from CSV files. It provides a flexible and asynchronous system for handling various data types like patients, doctors, and appointments, even when the incoming CSV files have inconsistent column names.

## Key Features

- **RESTful API:** Secure endpoints for uploading different types of data via CSV files.
- **Asynchronous Processing:** File uploads are processed in the background, allowing the API to respond quickly while the data is ingested without blocking the client.
- **Dynamic Header Mapping:** The system does not rely on hard-coded CSV column orders. It uses a database-driven alias system (`FieldAliasMapping`) to map incoming column names (e.g., "fName", "first_name") to the correct internal model fields (e.g., "firstName").
- **Extensible Strategy Pattern:** Easily add support for new data types (e.g., Prescriptions, Lab Results) by implementing a single `FileProcessor` interface, without changing the core pipeline logic.
- **Transactional Integrity:** Data processing is transactional, ensuring that related records (like an Appointment and its associated Clinical Encounter) are created and saved together, maintaining data consistency.
- **Relationship Management:** Automatically handles creating or linking related entities, such as finding an existing `ClinicalEncounter` for a new appointment or creating one if it doesn't exist.

## Architecture Overview

The data flow is orchestrated through a simple yet powerful pipeline:

1.  **Upload:** A client sends a `POST` request with a CSV file to the `FileUploadController` (e.g., `/file/upload/Patient`).
2.  **Queue:** The controller saves the file to a temporary location and creates a `RawDataEvent` record in the database with a `PENDING` status.
3.  **Process:** The `PipeLineService` picks up the pending event and, using the Strategy design pattern, selects the correct `FileProcessor` implementation (e.g., `PatientProcessor`, `AppointmentProcessor`) based on the file type.
4.  **Transform & Load:** The selected processor reads the CSV file, uses the `FieldAliasMapping` table to understand the columns, validates the data, and saves the new entities to the database.

## Technologies Used

- **Backend:** Java 21, Spring Boot
- **Data Persistence:** Spring Data JPA, PostgreSQL
- **API:** Spring Web (REST)
- **Testing:** JUnit 5, Mockito
- **Build:** Apache Maven
- **Utilities:** Lombok

## Getting Started

### Prerequisites

- JDK 21 or later
- Git

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd MediFlow
    ```

2.  **Configure the database:**
    Ensure you have a PostgreSQL database running. You will need to create a file named `application.properties` in `src/main/resources/` and add your database connection details:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

### Building the Project

The project uses the Maven wrapper, so you don't need a local Maven installation.

```bash
./mvnw clean install
```

### Running the Application

```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`.

### Running Tests

```bash
./mvnw test
```

## API Usage

To upload a file, send a `multipart/form-data` POST request to the upload endpoint.

- **Endpoint:** `POST /file/ingest/upload`
- **`{fileType}`:** The type of data being uploaded. This must match the `entityType` in the `FieldAliasMapping` table (e.g., `Patient`, `Appointment`).

### Example: Uploading a Patient CSV

Given a file named `patients.csv` with the following content:

```csv
fName,lName,dob
John,Doe,1990-01-15
Jane,Smith,1985-05-20
```

You can upload it using `curl`:

```bash
curl -X POST -F "file=@/path/to/your/patients.csv" -F "fileType=Patient" http://localhost:8080/file/upload/Patient
```

The server will respond with a `202 Accepted` status, and the file will be processed in the background.

