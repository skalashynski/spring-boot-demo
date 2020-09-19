<h3>This project uses:</h3>
<h5>Spring MVC</h5>
<h5>Spring JPA</h5>
<h5>Spring Security</h5>
<h5>Docker</h5>
<p>There two options to start application using docker:</p>
<ul>
<li>Using Dockerfile and start 2 docker files (Mysql, App)
<ol>
    <li>Create network: <b>docker network create network</b></li>
    <li>Create Mysql image:<b>docker build -t mysql-spring-app-image:latest --file Dockerfile-mysql .</b></li>
    <li>Launch Mysql container: <b>docker run -d -p 3307:3306 --network=network --name=mysql-spring-demo-container mysql-spring-app-image</b></li>
    <li>Open application.properties: change <b>localhost</b> to <b>mysql-spring-demo-container</b></li>
    <li>Create image:<b>docker build --file Dockerfile-app -t app-demo-spring-image:latest .</b></li>
    <li>Launch App container: <b>docker run -d -p 8081:8081 --name=app-demo-spring-container --network=network app-demo-spring-image</b></li>
</ol>
</li>
<li>Using Docker-compose and start 2 containers at the same time</li>
</ul>
<h5>Swagger</h5>
<ul>
    <li>to open swagger use URL: http://localhost:8081/swagger-ui.html</li>
    <li>to open swagger api use URL: http://localhost:8081/v2/api-docs</li>
</ul>