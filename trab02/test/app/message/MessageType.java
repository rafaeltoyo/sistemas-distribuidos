package app.message;

public enum MessageType {
    HAND_SHAKE_REQUEST("handshake-rqt"),
    HAND_SHAKE_RESPONSE("handshake-rsp"),
    JOIN_REQUEST("join-rqt"),
    JOIN_RESPONSE("join-rsp"),
    LEAVE("leave"),
    RESOURCE_ACCESS_REQUEST("resource-rqt"),
    RESOURCE_ACCESS_RESPONSE("resource-rsp");

    private String name;

    MessageType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
