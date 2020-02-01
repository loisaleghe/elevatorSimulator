package elevatorSimulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Scheduler implements Runnable {

	private FloorData floorData;
	private boolean canSendData;
	private boolean canGetData;
	private boolean moreData;

	public Scheduler() {
		this.floorData = null;
		this.canGetData = false;
		this.canSendData = true;
		this.moreData = true;
	}


	public boolean getMoreData() {
		return this.moreData;
	}

	public void setMoreData(boolean moreData) {
		this.moreData = moreData;
	}

	public synchronized void sendData(FloorData fl) {
		//		Wait if flood data is being processed
		while(!this.canSendData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

//		System.out.println("== Requesting to go from floor " + fl.getFloor() + " to floor " + fl.getCarButton());
		this.floorData = new FloorData(fl);
		this.canGetData = true;
		this.canSendData = false;

		notifyAll();
	}

	public synchronized FloorData getData() {
		//		Wait if there is no floor data available
		while(!this.canGetData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

//		System.out.println( "== Adding floor " + this.floorData.getCarButton() + " to floors to visit ");
		FloorData fl = new FloorData(this.floorData);
		this.floorData = null;
		this.canSendData = true;
		this.canGetData = false;

		notifyAll();

		return fl;
	}

	@Override
	public void run() {

	}

	public static void main(String[] args) {


		Scheduler scheduler = new Scheduler();
		Thread schedulerSubsystem = new Thread(scheduler, "Schedular");
		Thread floorSubsystem = new Thread(new FloorSubsystem(scheduler), "Floor Subsystem");
		Thread elevatorSubsystem = new Thread(new ElevatorSubsystem(scheduler), "Elevator Subsystem");

		schedulerSubsystem.start();
		elevatorSubsystem.start();
		floorSubsystem.start();


	}

}
