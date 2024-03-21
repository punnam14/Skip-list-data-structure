package skipList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** The class that stores flights in a skip list. */
public class FlightList {

	// FILL IN CODE: needs to store the head and the height of the skip
	// list. Decide if you also need the tail.
	private FlightNode head, tail;
	private int height;
	private int towerHeight = 1;
	Random random = new Random();

	/**
	 * Constructor.
	 * Reads flights from the file and inserts each flight into the skip list.
	 *
	 * @param filename the name of the file
	 */
	public FlightList(String filename) {
		// FILL IN CODE
		try {
			FileReader f = new FileReader(filename);
			BufferedReader br = new BufferedReader(f);
			String line;
			while ((line = br.readLine()) != null){
				String[] arr = line.split(" ");
				if (arr.length != 6)
					throw new IndexOutOfBoundsException();
				FlightKey key = new FlightKey(arr[0], arr[1], arr[2], arr[3]);
				double price = Double.parseDouble(arr[5]);
				FlightData data = new FlightData(arr[4], price);
				insert(key, data);
			}
		} catch (IOException e){
			System.out.println("No file found");
		}
	}

	/**
	 * Returns true if the node with the given key exists in the skip list,
	 * false otherwise. This method needs to be efficient.
	 *
	 * @param key flight key
	 * @return true if the key is in the skip list, false otherwise
	 */
	public boolean find(FlightKey key) {
		FlightNode current = this.head;
		while (current != null){
			if (current.getKey().compareTo(key) == 0) {
				return true;
			}
			if (current.getNext().getKey().compareTo(key) > 0){
				current = current.getDown();
			}else if (current.getNext().getKey().compareTo(key) <= 0){
				current = current.getNext();
			}
		}
		return false;
	}

	/**
	 * Insert a (key, value) pair to the skip list. Returns true if it was able
	 * to insert it successfully.
	 *
	 * @param key  flight key
	 * @param data associated flight data
	 * @return true if insertion was successful
	 */
	public boolean insert(FlightKey key, FlightData data) {
		// FILL IN CODE
		//PART 0 : Return false if element is already in skipList
		if (find(key))
			return false;
		//Add initial layer
		if (head == null){
			initialLayer();
		}
		//PART 1 : Make the newNode tower
		FlightNode newNode = makeTower(key, data);
		//PART 2 : Set new empty layers
		if (this.towerHeight > this.height){
			newLayer();
		}
		//PART 3 : Insert new key
		FlightNode current = this.head;
		int currentHeight = this.height;
		while (current != null){
			if (current.getNext().getKey().compareTo(key) > 0){
				if (currentHeight == this.towerHeight){
					newNode.setNext(current.getNext());
					current.getNext().setPrev(newNode);
					current.setNext(newNode);
					newNode.setPrev(current);
					if (newNode.getDown() != null){
						current = current.getDown();
						newNode = newNode.getDown();
						currentHeight--;
						towerHeight--;
					}else{
						break;
					}
				}else{
					current = current.getDown();
					currentHeight--;
				}
			}else if (current.getNext().getKey().compareTo(key) <= 0){
				current = current.getNext();
			}
		}
		return currentHeight == towerHeight;
	}

	/**
	 * Helper method for insert
	 * Make an initial layer and set them as head and tail if the skipList is empty
	 */
	public void initialLayer(){
		this.head = new FlightNode(new FlightKey("AAA", "AAA", "01/01/2000", "00:00"), null);
		this.tail = new FlightNode(new FlightKey("ZZZ", "ZZZ", "01/01/3000", "25:25"), null);
		head.setNext(tail);
		tail.setPrev(head);
		this.height = 1;
	}

	/**
	 * Helper method for insert
	 * Given a new (key, value) pair generate a tower of the same node randomly
	 *
	 * @param key  flight key
	 * @param data associated flight data
	 * @return FlightNode that is the top most node of the tower
	 */
	public FlightNode makeTower(FlightKey key, FlightData data){
		FlightNode newNode = new FlightNode(key, data);
		while (random.nextInt(2) != 1){
			newNode.setTop(new FlightNode(key, data));
			newNode.getTop().setDown(newNode);
			newNode = newNode.getTop();
			this.towerHeight++;
		}
		return newNode;
	}

	/**
	 * Helper method for insert
	 * Adds empty layers if generated tower height is greater than already existing height in the skipList
	 */
	public void newLayer(){
		int level = this.height;
		while (level < this.towerHeight){
			FlightNode newHead = new FlightNode(new FlightKey("AAA", "AAA", "01/01/2000", "00:00"), null);
			FlightNode newTail = new FlightNode(new FlightKey("ZZZ", "ZZZ", "01/01/3000", "25:25"), null);
			newHead.setNext(newTail);
			newTail.setPrev(newHead);
			newHead.setDown(head);
			head.setTop(newHead);
			newTail.setDown(tail);
			tail.setTop(newTail);
			head = newHead;
			tail = newTail;
			level++;
		}
		this.height = this.towerHeight;
	}


	/**
	 * Returns the list of nodes that are successors of a given key. Refer to
	 * the project pdf for a description of the method.
	 *
	 * @param key flight key
	 * @return successors of the given key
	 */
	public List<FlightNode> successors(FlightKey key) {
		List<FlightNode> arr = new ArrayList<>();
		// FILL IN CODE
		FlightNode current = head;
		while (current.getNext() != null){
			if (current.getNext().getKey().compareTo(key) > 0){
				if (current.getDown() != null){
					current = current.getDown();
				}else{
					if (compareKey(current, key)){
						arr.add(current);
					}
					current = current.getNext();
					if (!compareKey(current, key)){
						break;
					}
				}
			}else if (current.getNext().getKey().compareTo(key) <= 0){
				current = current.getNext();
			}
		}
		return arr;
	}

	/**
	 * Helper method for successors
	 * Checks if origin, dest and date are same and current node time (first) is greater than given key time (second)
	 *
	 * @param first current node
	 * @param second key passed into successors
	 */
	public boolean compareKey(FlightNode first, FlightKey second){
		String origin = first.getKey().getOrigin();
		String dest = first.getKey().getDest();
		String date = first.getKey().getDate();
		String time = first.getKey().getTime();

		String origin2 = second.getOrigin();
		String dest2 = second.getDest();
		String date2 = second.getDate();
		String time2 = second.getTime();

		return origin.compareTo(origin2) == 0 && dest.compareTo(dest2) == 0 && date.compareTo(date2) == 0 && time.compareTo(time2) > 0;
	}

	/**
	 * Returns the list of nodes that are predecessors of a given key
	 * (that corresponds to flights that are earlier than the given flight).
	 * Refer to the project pdf for a detailed description of the method.
	 *
	 * @param key flight key
	 * @return predecessors of the given key
	 */
	public List<FlightNode> predecessors(FlightKey key, int timeFrame) {
		List<FlightNode> arr = new ArrayList<>();
		// FILL IN CODE
		FlightNode current = head;
		int diff = timeFrame;

		while (current != null){
			if (current.getNext().getKey().compareTo(key) > 0){
				if (current.getDown() != null){
					current = current.getDown();
				}else{
					FlightNode node = current;
					while (node.getPrev() != null){
						int timeDiff = getTime(key) - getTime(node.getKey());
						if (timeDiff <= diff && compare(node,key) && node.getKey().compareTo(key) != 0){
							arr.add(0, node);
						}
						node = node.getPrev();
						if (!compare(node,key)){
							break;
						}
					}
					break;
				}
			}else if (current.getNext().getKey().compareTo(key) <= 0){
				current = current.getNext();
			}
		}
		return arr;
	}

	/**
	 * Helper method for predecessors and findFlights
	 * Takes in a key and retrieves time, splits it to get the hour
	 *
	 * @param key for which we need the hour
	 * @return hour the hour of the key
	 */
	public int getTime(FlightKey key){
		String[] time = key.getTime().split(":");
		int hour = Integer.parseInt(time[0]);

		return hour;
	}

	/**
	 * Helper method for predecessors
	 * Checks if origin, dest and date are same and current node time (first) is smaller than given key time (second)
	 *
	 * @param first current node
	 * @param second key passed into predecessors
	 */
	public boolean compare(FlightNode first, FlightKey second){
		String origin = first.getKey().getOrigin();
		String dest = first.getKey().getDest();
		String date = first.getKey().getDate();
		String time = first.getKey().getTime();

		String origin2 = second.getOrigin();
		String dest2 = second.getDest();
		String date2 = second.getDate();
		String time2 = second.getTime();

		return origin.compareTo(origin2) == 0 && dest.compareTo(dest2) == 0 && date.compareTo(date2) == 0 && time.compareTo(time2) < 0;
	}

	/**
	 * Returns the string representing the SkipList (contains the elements on all levels starting at the
	 * top. Each level should be on a separate line, for instance:
	 * (SFO, PVD, 03/14, 09:15)
	 * (SFO, JFK, 03/15, 06:30), (SFO, PVD, 03/14, 09:15)
	 * (SFO, JFK, 03/15, 06:30), (SFO, JFK, 03/15, 7:15), (SFO, JFK, 03/20, 5:00), (SFO, PVD, 03/14, 09:15)
	 */
	public String toString() {
		// FILL IN CODE
		StringBuilder sb = new StringBuilder();
		FlightNode current = head;
		while (current != null){
			FlightNode start = current;
			while (!start.getKey().getOrigin().equals("ZZZ")){
				if (start.getKey().getOrigin().equals("AAA")){
					start = start.getNext();
				}
				sb.append(start.getKey());
				if (start.getNext().getNext() != null){
					sb.append(", ");
				}
				start = start.getNext();
			}
			current = current.getDown();
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Outputs the SkipList to a file
	 * (prints the elements on all levels starting at the top.
	 * Each level should be printed on a separate line.
	 * @param filename the name of the file
	 */
	public void print(String filename) {
		// FILL IN CODE
		try (PrintWriter printWriter = new PrintWriter (filename)){
			FlightNode current = head;
			while (current != null){
				FlightNode start = current;
				while (start != null && !start.getKey().getOrigin().equals("ZZZ")){
					if (start.getKey().getOrigin().equals("AAA")){
						start = start.getNext();
					}
					printWriter.print(start.getNode(start));
					printWriter.print("  ");
					start = start.getNext();
				}
				printWriter.println();
				current = current.getDown();
			}
		}catch (FileNotFoundException e){
			System.out.println("Could not find the file.");
		}
	}

	/**
	 * Returns a list of nodes that have the same origin and destination cities
	 * and the same date as the key, with departure times within the given time
	 * frame of the departure time of the key.
	 *
	 * @param key       flight key
	 * @param timeFrame interval of time
	 * @return list of flight nodes that have the same origin, destination and date
	 * as the key, and whose departure time is within a given timeframe
	 */
	public List<FlightNode> findFlights(FlightKey key, int timeFrame) {
		List<FlightNode> resFlights = new ArrayList<>();
		// FILL IN CODE
		List<FlightNode> arrSuccessors;
		List<FlightNode> arrPredecessors;
		int keyHour = getTime(key);
		int currentHour;

		//Check Predecessors
		arrPredecessors = predecessors(key, timeFrame);
		if (arrPredecessors.size() != 0){
			resFlights.addAll(arrPredecessors);
		}

		//Check if given key needs to be added
		FlightNode current = findNode(key);
		if (current != null){
			resFlights.add(current);
		}

		//Check Successors
		arrSuccessors = successors(key);
		if (arrSuccessors.size() != 0){
			for (FlightNode node : arrSuccessors){
				currentHour = getTime(node.getKey());
				if (currentHour - keyHour <= timeFrame){
					resFlights.add(node);
				}
			}
		}
		return resFlights;
	}

	/**
	 * Helper method for findFlights
	 * Return the entire node for the given key if it exists, null otherwise
	 *
	 * @param key node to be found
	 * @return first occurrence of the key
	 */
	public FlightNode findNode(FlightKey key) {
		FlightNode current = head;
		while (current != null){
			if (current.getKey().compareTo(key) == 0){
				return current;
			}
			if (current.getNext().getKey().compareTo(key) > 0){
				if (current.getDown() != null){
					current = current.getDown();
				}else{
					break;
				}
			}else if (current.getNext().getKey().compareTo(key) <= 0){
				current = current.getNext();
			}
		}
		return null;
	}
}
