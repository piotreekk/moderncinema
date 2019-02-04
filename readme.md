Trochę po polsku:

Projekt pisany na zaliczenie i dla zabawy, wiele rzeczy jest zrobione tak jak nie powinno się tego robić, ale właśnie dzięki temu że je zrobiłem dowiedziałem się, że nie powinienem ich robic :))

Backend prosty, czasem mieszane podejscie legacy z wczesniejszych wersji javy z java 8 (stream API). Próbowałem tam różnych rozwiązań.

Nie mieszałbym drugi raz frontendu klienta javyFx ze springiem, bo to raczej nie ma sensu, javaFx świetnie poradziłaby sobie sama, wszelkie wstrzykiwania zależności i singletony jakie tu mam nie wnoszą nic dobrego do projektu.
Na frontendzie też nie ma jakiegoś ujednolicenia kodu, trzymania się danej konwencji. Raz do modelu binduję pola ( całkiem fajnie to działa w javaFX) a raz tworzę nowe obiekty normalnie, zczytuję wartosci formularzy i wpisuję do obiektu. Oba podejscia tutaj działają dobrze.

UWAGA::::: Klient restowy działa na tym samym wątku co GUI, co czasem powoduje przycinanie się. Na tym etapie porzucam projekt, nie będę robić wątków pobocznych.


____________________________________________________________________________________________________________



How to run?

BACKEND: 
	./mvnw spring-boot:run
	or ./mvnw clean package -> cd target -> java -jar cinemabackend-0.0.1-SNAPSHOT.jar 

FRONTEND:
	./mvnw spring-boot:run

GUI works fine only in exploded mode ie. when run with command: mvn spring-boot:run . When you run it from generated jar resources don't load at all. Don't know why yet, but probably it's because of springboot and javafx wrong integration and configuration.

