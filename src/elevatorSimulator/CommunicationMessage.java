/**
 * 
 */
package elevatorSimulator;

/**
 * @author djani
 * This enum represents the messages exchanged between all the subsystems involved in the elevator simulator
 */
public enum CommunicationMessage {
	NO_MORE_FLOOR_REQUESTS,
	MORE_FLOOR_REQUESTS,
	ELEVATOR_CONTINUE,
	ELEVATOR_STOP,
	ELEVATOR_MOVE
}
