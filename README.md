# CttPdcpAdapter
CTT Postal Code Adapter from REST to SOAP web service

Compatible with Java 8 platform but not with later versions because JAX-WS was removed

Check java versions:

```
java -version
javac -version
```

To compile the code:

```
javac *.java
```

To start the server in the default URL (you can provide one argument to define a different URL):

```
java CTTAdapter
```

To test the service:

```
curl -i -H "Content-Type: text/xml;charset=UTF-8" --data "@Ping.xml" -X POST http://localhost:8080/ctt
curl -i -H "Content-Type: text/xml;charset=UTF-8" --data "@ValidatePostalCode.xml" -X POST http://localhost:8080/ctt
```


----
