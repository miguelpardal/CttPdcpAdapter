# CttPdcpAdapter
CTT Postal Code verification service Adapter from REST to SOAP web service


## Getting Started

Check Java versions:

```
java -version
javac -version
```

The code runs directly on the Java platform version 8 but not on later versions because the JAX-WS libraries were removed.


To compile the code:

```
javac *.java
```

To start the server in the default URL (you can provide an argument URL to override the default):

```
java CTTAdapter
```

To see the generated Web Service description, click the following links: [WSDL](http://localhost:8180/ctt?wsdl) and [XSD](http://localhost:8180/ctt?xsd=1).


To test the service:

```
curl -i -H "Content-Type: text/xml;charset=UTF-8" --data "@Ping.xml" -X POST http://localhost:8180/ctt
curl -i -H "Content-Type: text/xml;charset=UTF-8" --data "@ValidatePostalCode.xml" -X POST http://localhost:8180/ctt
```

The XML files contain valid SOAP requests and can be edited.



## Authors

* [Miguel Pardal](https://github.com/miguelpardal)
* Sérgio Guerreiro


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
