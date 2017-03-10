./mvnw clean package -Dmaven.test.skip=true
sudo docker build -t egovio/egf-masters:latest .
sudo docker-compose build
sudo docker-compose up
