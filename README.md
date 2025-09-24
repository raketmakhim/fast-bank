# fast-bank
Banking project for learning 

## Requirements
Java 17 or higher
Docker (for running the database)

## Setup
1. Clone the repository
2. Navigate to the project directory
3. Run the database using Docker:
   ```
   docker run --name fast-bank-db -e POSTGRES_USER=yourusername -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=fast_bank -p 5432:5432 -d postgres
   ```
4. Update the database connection settings in `src/main/resources/application.properties`
5. Build the project using Maven:
   ```
   mvn clean install
   ```
6. Run the application:
   ```
    mvn spring-boot:run
    ```
7. Access the application at `http://localhost:8080`.
