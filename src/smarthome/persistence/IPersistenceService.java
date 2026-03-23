package smarthome.persistence;

import smarthome.model.SmartHomeSystem;

public interface IPersistenceService {
    SmartHomeSystem loadSystem();
    void saveSystem(SmartHomeSystem system);
}
