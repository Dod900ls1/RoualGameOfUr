package server.message;


/**
 * This class would send a message based on the message type
 */
public record Message(MessageType type, Object data) {}
