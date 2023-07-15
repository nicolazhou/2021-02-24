package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {

	public enum EventType {
		
		AZIONE,
		GOAL,
		INFORTUNIO,
		ESPULSIONE
		
	}
	
	private EventType type;
	private Integer time;
	public Event(EventType type, int time) {
		super();
		this.type = type;
		this.time = time;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Event [type=" + type + ", time=" + time + "]";
	}
	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return this.time.compareTo(o.getTime());
	}
	
	
}
