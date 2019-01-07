This is my first real spring project, you will find here loads of mistakes and bad practices. 

How to run?

BACKEND: 
	./mvnw spring-boot:run
	or ./mvnw clean package -> cd target -> java -jar cinemabackend-0.0.1-SNAPSHOT.jar 
FRONTEND:
	./mvnw spring-boot:run

GUI works fine only in exploded mode ie. when run with command: mvn spring-boot:run . When you run it from generated jar resources don't load at all. Don't know why yet, but probably it's because of springboot and javafx wrong integration and configuration.


Short description:

Backend project:
Server for moderncinema application. Simple CRUD operations on db.
I know, it's not a REST API.

Frontend project:
JavaFX and Springboot Hybrid. Huge mess in code. Refactor needed (styles one time from file one time in fxml .... , Autowiring ChooseSeatsController to SeancesController is something strange too, but when I realised it was to much to do to refactor it).

