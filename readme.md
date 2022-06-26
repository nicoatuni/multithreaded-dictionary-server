# Multithreaded Dictionary

This repo contains my solutions for Assignment 1 of COMP90015 2022S1. The goal of the project is to build a dictionary service consisting of a multithreaded server and client GUI in Java using sockets. Supported operations include adding a new word and definition, querying a word's definition, updating a definition, and removing a word from the dictionary.

More details on the assignment specifications are available in [`specs.pdf`](./specs.pdf), and my writeup of the project is in [`assignment_1_report.pdf`](./assignment_1_report.pdf).


## Source Code
Source files for the server can be found in `src/server`, and those for the client in `src/client`.


## How to Run
To start the server (in project root dir):
`java -jar DictionaryServer.jar <port_number> words.json`

To start the client (in project root dir):
`java -jar DictionaryClient.jar localhost <port_number>`
