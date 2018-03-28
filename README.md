# cashman

Additionally I have added Travis CI Automated build: I used maven as the automated build tool of choice, Maven being a favourite amongst many Java developers, for the case of this assignment, I built the application on the back of Spring-boot, and Spring-boot plugs in well with Maven Automated test suite: Since this app is only a back end solution, I felt that Junit testing was sufficient for test suite coverage. I included various tests for each of the layers:

REST – (Junit, RestAssured for the REST call tests)
Service – (Junit, SpringBootTest)
Data – (Junit, SpringBootTest) All libraries that are used. Including any third party libraries used: Building with “mvn clean install” should include all the libraries, but just in-case, I have included a fat-jar version which can be started with:
“java -jar suncorp-atm-1.0-SNAPSHOT.jar” Instructions on how to build the application: There is a prerequisite to have the build tool Maven installed. https://maven.apache.org/download.cgi

Once Maven is correctly installed, and able to execute from the command line, the following line executed in the application root folder will build the application and run all the unit tests.

“mvn clean install”

This will result in Maven downloading all the required dependencies, building the application into the target folder, and running all unit tests. To avoid running the test, and merely only building the application, run the following:

“mvn clean install –DskipTests”

Instructions on how to execute the application When the application starts, an embedded instance of Tomcat will start up with spring-boot. The default port is 8080, thus please ensure this port is available, if this is not possible, simply add configuration to the application.yml file in the src/main/resources folder

e.g: server: port: 8085

There are 2 ways to execute the application:

To start up the application, using maven and spring-boot plugin use the following command:

“mvn spring-boot:run”

to run the application directly from the compiled jar file

“java –jar target/suncorp-atm-1.0-SNAPSHOT.jar

The minimum code deliverables are:

Source code – Supplied
Automated test suite – Supplied
Automated build (including any tools you prefer to use to aid in the creation and maintenance of your software e.g. static analysis utilities) – Supplied
We will ask you how long it took to produce your solution, or if you were unable to complete it, how long you expect it would take to complete

It took me about a day to complete the solution, the most time consuming was ensuring that all the test have a minimum of 80% code coverage.

Total Code coverage is 100% classes, and 86% lines covered.

Mandatory Feature Set

The device will have a supply of cash (physical bank notes) available. a.	Completed and tested for Australia’s national currency is the Australian dollar which comes in denominations of $5, $10, $20, $50 and $100 notes. Coins come in 5, 10, 20 and 50 cent and one and two dollar denominations.

It must know how many of each type of bank note it has. It should be able to report back how much of each note it has. a.	There is an internal service ATMService. getDenominationQuantities

initialisation. After initialisation, it is only possible to add or remove notes. a.	Initialisation of the note quantities are done in the application.yml file as follows: default: denominations: ten: 10 twenty: 10 fifty: 10 hundred: 10 twohundred: 10

b.	The ATMBase.class is a singleton and immutable in most of the sense, however to facilitate testing, I did not make it final, and added a ATMTestBase.class in the tests which facilitated mutability in aid of testing. These are the Memory Data classes and carry the denomination and quantity data.

It must support $20 and $50 Australian denominations. a.	Supports ALL of Australia’s national currency.

It should be able to dispense legal combinations of notes. For example, a request for $100 can be satisfied by either five $20 notes or 2 $50 notes. It is not required to present a list of options. a.	Currency dispensing is accurate based on the available denominations, and intelligibly identifies the most practical combinations as required.

If a request cannot be satisfied due to failure to find a suitable combination of notes, it should report an error condition in some fashion. For example, in an ATM with only $20 and $50 notes, it is not possible to dispense $30. a.	The service class fails with a customized exception with an appropriate message, which is trapped, and translated into a HttpStatus.PRECONDITION_FAILED (412) when accessing the withdrawal via the REST Api.

Dispensing money should reduce the amount of available cash in the machine. a.	All dispensing and deposits correctly reduce and increase the note quantities.

Failure to dispense money due to an error should not reduce the amount of available cash in the machine. a.	No denomination counts are affected when a failed withdrawal occurs.

For an ATM-style of machine (with $20 and $50 notes), the following dispensed amounts are of particular interest: a.	$20 - - Tested Ok b.	$40 - Tested Ok c.	$50 - Tested Ok d.	$70 - Tested Ok e.	$80 - Tested Ok f.	$100 - Tested Ok g.	$150 - Tested Ok h.	$60 - Tested Ok i.	$110 - Tested Ok j.	$200, when there is only 3x$50 notes and 8x$20 notes available. Tested Ok i.	Result of Junit Test testATMDenominations200Special:

ATMDto(message=null, theMoney={FIFTY=2, TWENTY=5}) Optional Feature Set Support all other legal Australian denominations and coinage. a.	All coinage and notes included, and scalable in the case of additional coinage or notes are added. The controller should dispense combinations of cash that leave options open. For example, if it could serve up either 5 $20 notes or 2 $50 notes to satisfy a request for $100, but it only has 5 $20 notes left, it should serve the 2 $50 notes.

The controller needs to be able to inform other interested objects of activity. Threshold notification in particular is desirable, so that the ATM can be re-supplied before it runs out of cash. Withdrawal events trigger application events which has a listener which subsequently compare the remaining quantity for the specified denomination against a threshold specified in the application.yml For now only a message is logged, however, notifications can be sent from here to further stakeholders

Persistence of the controller is optional at this time. It can be kept in memory. However, it should go through a distinct initialisation period where it is told the current available amounts prior to being used. Persistence in the form of an in memory singleton immutable data class
