package smarthome.view;

/**
 * View interface - defines how the application renders data.
 * Receives only ViewData (DTO), not the Controller.
 * This decouples View from business logic and Controller.
 */
public interface View {
    void renderView(ViewData data);
    void showInvalidOption();
}
