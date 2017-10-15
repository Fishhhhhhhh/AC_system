# AC_system
This is a simple aircraft software REST subsystem base on Java. In this project, Eclipse (Oxygen), Java 1.8, Tomcat 8.5 and JAX-RS 2.0, java-json is used. The project for now can only be tested locally through the Postman and Apache Tomcat local server.

Assumption<br />
The system has to be started first before dealing with other request.

0. Setup Java Development Kit (JDK) and Eclipse IDE
1. Setup Apache Tomcat<br />
You can download the latest version of Tomcat from https://tomcat.apache.org/. Once you downloaded the installation, unpack the binary distribution into a convenient location. For example in C:\apache-tomcat on windows, or /usr/local/apache-tomcat on Linux/Unix and set CATALINA_HOME environment variable pointing to the installation locations. After a successful startup, the default web applications included with Tomcat will be available by visiting http://localhost:8080/. 

2. Import the project into Eclipse and build the path

3. Add the server Apache-tomcat<br />
Windows -> Show View -> Servers. Then in the servers view, right click and add new. It will show a pop up containing many server vendors. Under Apache select Tomcat 8.5 (Depending upon your downloaded server version). And in the run time configuration point it to the Tomcat folder you have downloaded.

4. Run test with Postman<br />
Make a request to aqmRequestProcess to start, enqueue or dequeue to the aircraft queue. <br />
4.1 Request start
Put http://localhost:8080/AC_System/ServiceAC?request=Start in POSTMAN with GET request and see the following result {"Start":"success"}.
4.2 Request Enqueue
Put http://localhost:8080/AC_System/ServiceAC in POSTMAN with POST request and post the request following this format:

4.1 Request Dequeue
Put http://localhost:8080/AC_System/ServiceAC?request=Start in POSTMAN with GET request and see the following result {"Start":"success"}.
