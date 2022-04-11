# SYSC 3303 PROJECT

Date: Feb 19th 2022.  
**Version 2.0**

## - RequestMsg
This class is used to express the format of massage communicated between each class. In this case, we use three numbers to represent where input from, elevator movements and destination.

## - Floor.java 
This classs represents the Floor Subsystem which emulates a floor in a building. The floor Subsystem exchanges messages with the scheduler. The read_event method can receive the input from user, then floor_send method is used to send information to scheduler for the next step. After all system finish successfully, Floor will claim that elevator has reached there.

### For the elevator systems, there are 4 different states. The default state is called idle state and whenever the elevator gets requests from scheduler, it will turn to Closing state meaning doors closed and prepare to move which is called MoveEle state. When the elevator reach the destination floor, it will become to Opening state and then change to idle to wait for the next request.

## - Elevator.java 
This class represents the Elevator Subsystem which emulates an elevator car. It sends calls out to the scheduler when there is a request from the elevator and receives instructions from the scheduler when there is a message/event from the floor. The setCurrentState method is used to declare the current state of elevator. In this iteration, we contribute that each state will maintain 1 second and then switch to the next state, the timerStart method is built for this. In addition, receiveMsg is the method is used to get requests from scheduler and report after arrival using report method.

## - idle.java
This class make the elevator keep opening and it is the default state of the elevator.

## - Closing.java
This class handles the closing operation and closing state of the elevator of the elevator doors.

## - MoveEle.java
This class move the elevator and MoveEle state of the elevator.

## - Opening.java 
This class handles the opening operation and opening state of the elevator of the elevator doors.

## - Scheduler.java
This class receives instructions form the floor and elevator class. It is used to schedule the elevator cars and the order they respond to requests. In Scheduler, there are two lists which are used to put requests that to go up or down from floor and elevators. handleRequest method will implement this step and then addElevator should adjust the elevator systems. After arrival, method called arrival will send massage to floor so that one program routine finished.

## - ArrivalMessage.java
This class is responsible for outputing the arrival of an elevator. It returns a boolean variable to represent if elevator has arrived or not.

## - Main.java
This class contains the main method used to run the code.

### Breakdown of responibilites:
- Han Jiatong:- Elevator.java, UML diagram, test case
- Zhang Boshen:- Floor.java, Scheduler.java, UML sequence diagram, README
- Iyamu Ese:- UML Class diagram, Junit test
- Ziheng Zhu: State diagram

Team Members
- Han Jiatong
- Iyamu Ese
- Zhang Boshen
- Zhu Ziheng
