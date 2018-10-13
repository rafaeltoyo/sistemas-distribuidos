package client.controller;

public class InteresseController {
    private static InteresseController ourInstance = new InteresseController();

    public static InteresseController getInstance() {
        return ourInstance;
    }

    private InteresseController() {
    }
}
