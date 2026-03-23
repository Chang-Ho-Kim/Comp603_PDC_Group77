package smarthome.view;

/**
 * Data Transfer Object for passing view data.
 * Decouples the View from the Controller - View receives only the data it needs to display.
 * No behavior or business logic, just plain data.
 */
public class ViewData {
    private String message;
    private String menuContents;
    private String optionsContents;
    private String formattedDateTime;
    
    public ViewData(String message, String menuContents, String optionsContents, String formattedDateTime) {
        this.message = message;
        this.menuContents = menuContents;
        this.optionsContents = optionsContents;
        this.formattedDateTime = formattedDateTime;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getMenuContents() {
        return menuContents;
    }
    
    public String getOptionsContents() {
        return optionsContents;
    }
    
    public String getFormattedDateTime() {
        return formattedDateTime;
    }
}
