After cloning the project, run in terminal:
```
mvn clean install
mvn spring-boot:run
```

Then, make a **POST** request to:
```
http://localhost:8080/api/create_report
```

After which **report.txt** will appear in the root folder of the project.
