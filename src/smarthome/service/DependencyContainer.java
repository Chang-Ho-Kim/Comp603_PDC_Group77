package smarthome.service;

/**
 * Dependency Container (Service Locator).
 * Manages instantiation and access to all services.
 * While not a full IoC container, this centralizes dependency management.
 */
public class DependencyContainer {
    
    private static DependencyContainer instance;
    
    private final IBillingService billingService;
    private final IAutomationService automationService;
    private final ILoggingService loggingService;
    private final IFormatterService formatterService;
    private final IThresholdManager thresholdManager;
    
    private DependencyContainer() {
        this.billingService = new SimpleBillingService();
        this.automationService = new SimpleAutomationService();
        this.loggingService = new SimpleLoggingService();
        this.formatterService = new SimpleFormatterService();
        this.thresholdManager = new SimpleThresholdManager();
    }
    
    public static synchronized DependencyContainer getInstance() {
        if (instance == null) {
            instance = new DependencyContainer();
        }
        return instance;
    }
    
    public IBillingService getBillingService() {
        return billingService;
    }
    
    public IAutomationService getAutomationService() {
        return automationService;
    }
    
    public ILoggingService getLoggingService() {
        return loggingService;
    }
    
    public IFormatterService getFormatterService() {
        return formatterService;
    }
    
    public IThresholdManager getThresholdManager() {
        return thresholdManager;
    }
}
