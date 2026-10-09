
# Use the MySQL server image
FROM mysql/mysql-server:latest

# Set working directory
WORKDIR /tmp

# Copy SQL and dump files
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/

# Run employees.sql during database initialization
COPY test_db/employees.sql /docker-entrypoint-initdb.d/

# Set database credentials for the lab
ENV MYSQL_ROOT_PASSWORD=example
ENV MYSQL_ROOT_HOST=%
