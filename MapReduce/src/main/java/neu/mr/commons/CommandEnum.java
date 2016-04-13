package neu.mr.commons;

/**
 * Enumeration class for different commands
 * @author chintanpathak
 *
 */
public enum CommandEnum {
	
	DISCOVER 		("discover"),
    DISCOVER_ACK 	("discover_ack");
	
    private final String text;

    /**
     * @param text
     */
    private CommandEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
