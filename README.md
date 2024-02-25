# Government Document Management System

This project is part of the curriculum for the "Upravljanje digitalnim dokumentima" (Digital Document Management)

## Technologies Used

* Backend:
  * Java
  * Spring Boot
* Frontend:
  * React
* Database:
  * PostgreSQL
* Search:
  * Elasticsearch 8, Kibana, Logstash
* DevOps:
  * Docker Compose


## Features

- **Document Management**: Upload, Download, and manage laws and contracts efficiently.
- **Search**: Utilize Elasticsearch 8 for fast and accurate document retrieval.
- **Serbian Text Analysis**: Used Serbian Analyzer for enhanced search capabilities in Serbian language documents.
- **Logging and Monitoring**: Utilize ELK (Elasticsearch, Logstash, Kibana) stack for monitoring application logs.

## Installation

1. **Clone Repository**:
   ```
   git clone https://github.com/kukuckarastislav/udd-elk.git
   ```

2. **Setup Environment Variables**:
    - Create a `.env` file in the root directory and configure necessary environment variables such as database connection details, Elasticsearch host, etc.

3. **Build and Run Docker Containers**:
   ```
   docker-compose up --build
   ```

4. **Access Application**:
    - Once the containers are up and running, access the application at `http://localhost:8080`.

## Screenshot

### Home
![ss1](/AppScreenshot/ss1.png)

### Advanced search - Boolean query
![ss2](/AppScreenshot/ss3.png)

### Geolocation search
![ss3](/AppScreenshot/ss4.png)

### Creating new Government and uplouding contract, parsing text from contract
![ss4](/AppScreenshot/ss5.png)














