# Behavior Model
![CS behavior modelv2](https://user-images.githubusercontent.com/23732725/106094399-47498300-6175-11eb-91eb-b0a92257c59c.png)

The behavior model consists of three components: 1) a set of states, 2) a set of transitions, and 3) configuration. The model is constructed in the graphical user interface environment in the ADOxx platform, and semi-automatically converted into java codes. Each behavior model is converted into a \textit{class} in java and corresponds to a CS type. Once the structures of behavior models are automatically constructed in java code, we additionally implement the unspecified parts.

- Each state *q* consists of a name, initial state, and time. A name is a *string* value that specifies the name of the state, an initial state is a *boolean* value to express whether the state is an initial state or not, and a time is an *integer* value that specifies a sojourn time in the state. The name is expressed above the state, and time is at the bottom side in the state. If a small circle appears in the state, then it means that it is the initial state. Given that we use a discrete-time simulation, the time should be expressed as an integer value. The time value could be 0, which means transitions in this state are immediately executed with no time delay. We only allow the transition that can occur at a predefined time.

- Each transition *tr* consists of a guard, a probability, an action list, and a synchronization channel. A guard is a *boolean* value that specifies a precondition that must be satisfied for the transition. The probability is a *double* value that specifies the probability of the transition being triggered. The action is a function executed during the transition and corresponds to the function in the java code. We additionally implement the contents of actions in java. The synchronization channel describes the interactions between two models. We use two notations, ! and ?, to denote the sending synchronization action and receiving synchronization action respectively, which refer to the communication channel in a UPPAAL modeling tool [[1]](#1).

- The configuration has a parameter that specifies parameter values for the behavior model.

For every simulation tick, whole behavior models that belong to the SoS model are executed. In the execution of each behavior model, if a sojourn time has passed, then the behavior model will collect a transition whose guard condition is true for all leaving transitions in the current state. Then, for all collected transitions, one transition will be triggered according to the probabilities in transitions.

## Example

![CS behavior model examplev4](https://user-images.githubusercontent.com/23732725/106096866-aa3d1900-6179-11eb-92a2-3b0e4e9b4e20.PNG)

A scenario described in the example model is that a human pushes a light toggle button of a bulb for each tick, but the turn on/off operations do not work correctly because the bulb is old. The behavior of pushing the toggle button is expressed by a send action (*PushButton!*) of the communication channel with a probability of 0.7. The behavior of being toggled is expressed in the bulb model (*PushButton?*). Whenever states are changed by a sending synchronization action of the human model, actions in transition are executed (*bulbOn* or *bulbOff* in the bulb model). The small black state in the *human* model denotes a dummy state to express self-transition because the ADOxx does not allow a relation whose source and destination are the same.

# MCIRSoS Behavior Model
The MCIR-SoS model contains share (or global) variables that are commonly accessed by behavior models. The share variables are as follows: 2D array for specifying ground and sea map (*GroundMap[MapSize][MapSize]*, and *SeaMap[MapSize][MapSize]*), and environmental condition values (*WeatherCondition* and *RoadCondition*).
The CS models have a *cost* parameter to express the operational cost of each CS, meaning each CS consumes *cost* for each simulation tick. We set *MapSize* to 50 and the *maxSeaPatientNum* and *maxGroundPatientNum* as 500. The *cost* of the firefighter and ambulance is 0.1, while that of the helicopter is 0.2 because the helicopter not only can rescue, but also transfer patients. In summary, the sea and ground are expressed as 50*50 size arrays, and 500 patients emerge at both maps during one adaptation cycle.
## Firefighter CS Model
![FirefighterCSv2](https://user-images.githubusercontent.com/23732725/106094561-8ed00f00-6175-11eb-92f7-5aa6ddd271a9.png)

Once the firefighter is dispatched, the position of the firefighter is randomly assigned on the ground map (*initPosition* action). The firefighter then starts searching patients around him/her at the ground (*searchGroundPatient* action) with a certain probability. The probability of the search action triggered depends on the *WeatherCondition*. In the searching process, the firefighter rescues patients with high severity first. If the firefighter finds the patient on the ground, he/she will rescue the patient (*rescueGroundPatient* action) and transfer the patient to the ambulance; if not, the firefighter will randomly move (*move* action). After both actions, the firefighter will return to the *Search* state and start the search again.
## Helicopter CS Model
![HelicopterCSv2](https://user-images.githubusercontent.com/23732725/106094563-90013c00-6175-11eb-80e6-9fef852bf530.png)

Once the helicopter is dispatched, the position of the helicopter is randomly assigned on the sea map (*initPosition* action). The helicopter then starts searching patients around it at the sea (*searchSeaPatient* action) with a certain probability. The probability of the search action triggered depends on the *WeatherCondition*. Similar to the firefighter, the helicopter rescues patients with high severity first. If the helicopter finds the patient in the sea, then it will directly transfer the rescued patient to the hospital (*transferToHospital* action) or just transfer to the ambulance according to the *weatherCondition*. If the weather is fine, the probability of transferring is high; if not, the helicopter randomly moves (*move* action). After these actions, the helicopter will return to the *Search* state and start the search again.
## Ambulance CS Model
![AmbulanceCSv2](https://user-images.githubusercontent.com/23732725/106094577-955e8680-6175-11eb-808a-c59a224bd5e7.png)

At the *Waiting* state, the ambulance checks whether or not there are rescued patients (*checkRescuedPatient* action). If there exist rescued patients, then the ambulance will transfer them to the hospital (*transferToHospital* action). The transfer time depends on the roadCondition (*RC* in *TransferS* and the *TransferG* states). After the transfer, the ambulance will return to the *Waiting* state. If no rescued patients exist, then the ambulance will also return to the *Waiting* state.
## Patient Internal Environment Model
![PatientCSv4](https://user-images.githubusercontent.com/23732725/106094590-998aa400-6175-11eb-8b5c-8151f853c923.png)

The patient's models have an *x,y* parameter to express the position of each patient. The behaviors described in both models correspond to a each cell of the *GroundMap[][]* and *SeaMap[][]}* For each cell of both maps, the patients emerge if the number of current patients does not exceed the limit. Upon emerging, the patients are assigned a severity level (1 or 2) with the same probability. Next, the patients are transited to the *Rescued* state if the firefighter or helicopter executes synchronization channels with a send command (*GroundPatient[x,y]! or *SeaPatient[x,y]!*).

## References
<a id="1">[1]</a> 
Larsen, Kim G., Paul Pettersson, and Wang Yi. "UPPAAL in a nutshell." International journal on software tools for technology transfer 1.1-2 (1997): 134-152.
