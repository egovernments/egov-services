./mvnw clean package
docker build -t egovio/financials:latest .
docker-compose build
docker-compose up
