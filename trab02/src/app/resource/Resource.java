package app.resource;

import java.util.LinkedList;
import java.util.Queue;

public class Resource {
    public ResourceState state;
    public Queue<ResourceQueueItem> requestQueue;

    public Resource() {
        state = ResourceState.RELEASED;
        requestQueue = new LinkedList<>();
    }
}
