# OzoNav
Java app for guiding Ozobots driving on screen

## Premise
We want to experiment with MAPF (Multi Agent Path Finding) scenarios using actual, physical agents. This app shall help avoid any necessity to concern ourselves with robotics or other "unrelated" complications. The Ozobots come with pre-programmed abilities (movement, scanning of the environment...) that one can combine in custom programs. This app takes any-angle MAPF plans as input and issues commands to Ozobots via the screen that they drive on.

## How to use

### Known issues to be aware of
- For the agent IDs, consecutive whole numbers starting at 0 must be used.
- `add` commands issued with the same `time` value will be executed in undefined order which might, as a consequence of the previous issue, cause two agent IDs to be switched.
- Inaccuracies in robot speed and various dimensions when using a small display with high pixel density have been encountered. Some config fiddling was necessary.  
- The Ozobot radius, line width and duration of individual signals are mere constants in the source code. The aforementioned fiddling showed that it might be a good idea to move them to an external file or add to the other properties that the user is prompted for.

### The Ozobot program
`https://ozoblockly.com/editor?lang=en&robot=evo&mode=D&idkfa#eq4trw`  
Calibrate your Ozobots against your screen, import this program to each of them and activate it before placing them in the scenario.  

*I could add the actual .ozocode file to this repository, but I think it would be pointless, since I upload the program to the robots through the web GUI anyway.*  

### Input - MAPF plan
The input is read from `System.in`. Each line of input represents one command.  
Each command starts with its time of execution since the start of the scenario and the ID of the agent that is the target of the command.  
Distances and coordinates are given in millimeters.  
Time is given in milliseconds.  
Angle values are as follows: 0 goes straight with increasing x, "to the right"; 90 goes straight with increasing y, "down".  

Currently, the following commands are supported:  
 - `<time> <id> add <x> <y> <angle>`  
 Introduction of a new agent at coordinates [x,y], facing the given absolute angle.  
 - `<time> <id> rRel <angle>`  
 Rotation of an agent by relative angle.
 - `<time> <id> rAbs <angle>`  
 Rotation of an agent by absolute angle.
 - `<time> <id> mvDist <distance>`  
 Movement of an agent, in the direction it is currently facing, over the given distance.
 - `<time> <id> mvTo <x> <y>`  
 Rotation of an agent in the direction of the given point and subsequent movement to it.  
 - `end`
 Input will no longer be scanned after this command.
 
 ### Starting the scenario
 Upon application start, the user will be prompted for their screen parameters as well as Ozobot speeds and the time allowed for the human operator to place a new agent on the screen.  
 Then, an "intermediate" window is shown. This window can be dragged onto the screen the user wishes to use as the playground.  
 When the button in this intermediate window is clicked, the playground window maximizes on the screen, input starts being read and the plan timing begins as well.  
