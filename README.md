# Zendesk Ticket Viewer

## Personal Information
* Name: Abhishek Ranjan
* Email: aranjan5694@sdsu.edu

## Requirements 🔧
* Java version **11** or higher.

## Running The Application 🔌

To test the application run the following commands.

* Download the repository files (project) from the download section or clone this project by typing in the bash the following command:

       git clone https://github.com/abhranja-sudo/zendesk-ticket-viewer.git
       
* By default program will be able to run from my credential

* To add the custom credential, Open src/main/resources/config.properties and replace bearer token and base url

* To run the application, go inside project directory

        cd zendesk-ticket-viewer
        
* On Mac/Linux

        ./mvnw clean install exec:java
        
* Or on windows

        mvnw.cmd clean install exec:java

* Please note the above command will run all the unit tests as part of build.

Architectural Design
---


The response to the user depends on the context user is currently in. In other words, the same user input should behave differently depending on the context user is in. Like pressing **n** (taking input for displaying next set of tickets) should be acceptable only if user is in the GetAllTicket screen. For rest of the context, It should be treated as invalid input.
Thus, ***State Design Pattern*** seemed fit to achieve varying state. It enabled me alter the behavior when its state changes.



**Component Overview**

1.	***Controller*** Communicates data between viewer and service layer. It stores the reference to one of the concrete service depending on state and delegates 	it to do all the state specific work.
2.	***MenuService*** responsible to execute the business logic when the user is in Menu(Home) screen.
3.	***GetAllTicketService*** when user chooses to view all the tickets, then state transitions to getAllTicketService and it will now be responsible to execute the 		business logic as long user is present in this context.
4.	***GetTicketService*** Similarly as above, when user decides to view single tickets, then state transitions to this service and will process user inputs.
5.	***TicketRetriever*** responsible for executing GET request to Zendesk API’s  and throw exception if the response other than 2xx.
6.	***Viewer*** Responsible for displaying prompt and taking input.

The frame which get displayed on screen is ***modular*** and can be composed with Header, Footer and the body which in-turn can be composed with varying options.
This helped achieved building custom frame to show on screen for each scenario.

**More Design Choices:**

1.    Used ***cursor pagination*** to page through the ticket instead offset pagination as according to [Zendesk documentation](https://developer.zendesk.com/documentation/developer-tools/pagination/comparing-cursor-pagination-and-offset-pagination/), Cursor pagination provides greatly improved performance when retrieving extremely large record sets. This effectively means if we chose to import large data at once, we will not have to change the existing way of doing things.
2.    GetAllTicketService Only fetching 25 tickets at once  as user demands it as it helps keeping our service simpler. On the ***further iteration***, the user experience can be improved further by pre fetching at least 50 ticket ahead of the latest displayed ticket.
