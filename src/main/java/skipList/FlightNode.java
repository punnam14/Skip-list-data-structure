package skipList;

/**
 * The class that represents a node in a flight skip list. 
 * Contains the key of type FlightKey and the data of type FlightData. 
 * Also stores the following pointers to FlightNode(s): next, down, prev and up.
 */
public class FlightNode {
	private FlightKey key;
	private FlightData data;
	// FILL IN CODE: add other variables: next, prev, down, up
	private FlightNode next;
	private FlightNode prev;
	private FlightNode top;
	private FlightNode down;

	/**
     * FlightNode Constructor
	 * @param key flight key
	 * @param data fight data
	 */
	public FlightNode(FlightKey key, FlightData data) {
		this.key = key;
		this.data = data;
	}

	/**
     * A getter for the key
	 * @return key
	 */
	public FlightKey getKey() {
		return key;
	}

	/**
	 * A getter for the data
	 * @return data
	 */
	public FlightData getData() {
		return data;
	}

	/**
	 * A getter for the whole node
	 * @return the node with key and data
	 */
	public String getNode(FlightNode node) {
		return node.getKey().toString() + "-" + node.getData().toString();
	}

	/** Getter for next
	 *
	 * @return a reference to the next node
	 */
	public FlightNode getNext() {
		return next;
	}

	/** Getter for prev
	 *
	 * @return a reference to the previous node
	 */
	public FlightNode getPrev() {
		return prev;
	}

	/** Getter for the top node
	 *
	 * @return a reference to the top node
	 */
	public FlightNode getTop() {
		return top;
	}

	/** Getter for the down node
	 *
	 * @return a reference to the below node
	 */
	public FlightNode getDown() {
		return down;
	}

	/**
	 * Setter for the next
	 * @param other set the next node to other key
	 */
	public void setNext(FlightNode other) {
		next = other;
	}

	/**
	 * Setter for the prev
	 * @param other set the prev node to other key
	 */
	public void setPrev(FlightNode other) {
		prev = other;
	}

	/**
	 * Setter for the top
	 * @param other set the top node to other key
	 */
	public void setTop(FlightNode other) {
		top = other;
	}

	/**
	 * Setter for the down
	 * @param other set the below node to other key
	 */
	public void setDown(FlightNode other) {
		down = other;
	}
}
