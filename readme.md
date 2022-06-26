# Multithreaded Dictionary

This repo contains my solutions for Assignment 1 of COMP90015 2022S1. The goal of the project is to build a dictionary service consisting of a multithreaded server and client program in Java. More details on the assignment specifications are available in `specs.pdf`, and my writeup of the project is in `assignment_1_report.pdf`.

### Source Code
Source files for the server can be found in `src/server`, and those for the client in `src/client`.

### How to Run
To start the server:
`java -jar DictionaryServer.jar <port_number> words.json`

To start the client:
`java -jar DictionaryClient.jar localhost <port_number>`
