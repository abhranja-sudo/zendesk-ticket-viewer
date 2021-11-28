# Zendesk Ticket Viewer


## Requirements 🔧
* Java version **11** or higher.

## Running The Application 🔌

To test the application run the following commands.

* Download the repository files (project) from the download section or clone this project by typing in the bash the following command:

       git clone https://github.com/abhranja-sudo/zendesk-ticket-viewer.git
       
       
* Import it in Intellij IDEA or any other IDE:

* Open src/main/resources/config.properties  

* Replace the **xxxx** in *src/main/resources/config.properties* with **Token** in format *bearer yourtoken* 

* To run the application

        cd zendesk-ticket-viewer
        ./mvnw clean install exec:java
        
     

Architectural Design
---

**Component Overview**

1.	Controller: Communicates data between viewer and service layer. It stores the reference to one of the concrete service depending on state and delegates 	it to do all the state specific work.
2.	MenuService: responsible to execute the business logic when the user is in Menu(Home) screen.
3.	GetAllTicketService: when user chooses to view all the tickets, then state transitions to getAllTicketService and it will now be responsible to execute the 		business logic as long user is present in this context.
4.	GetTicketService: Similarly as above, when user decides to view single tickets, then state transitions to this service and will process user inputs.
5.	TicketRetriever: responsible for executing GET request to Zendesk API’s  and throw exception if the response other than 2xx.
6.	Viewer: Responsible for displaying prompt and taking input.

The frame which get displayed on screen is modular and can be composed with Header, Footer and the body which in-turn can be composed with varying options.

**More Design Choices:**

1.    Used cursor pagination to page through the ticket instead offset pagination as according to link, Cursor pagination provides greatly improved performance when retrieving extremely large record sets. This effectively means if we chose to import large data at once, we will not have to change the existing way of doing things.
2.    GetAllTicketService Only fetching 25 tickets at once  as user demands it as it helps keeping our service simpler. On the further iteration, the user experience can be improved further by pre fetching at least 50 ticket ahead of the latest displayed ticket.


