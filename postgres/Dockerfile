FROM postgres:15
ENV POSTGRES_USER=my_user \
    POSTGRES_PASSWORD=mypass \
    POSTGRES_DB=mydb

COPY init.sql /docker-entrypoint-initdb.d/

# To build the Docker image, run:
# docker build -t my_postgres_image .

# To run the Docker container, use:
# docker run -d -p 5432:5432 --name my_postgres_container my_postgres_image