package pl.piotrek.cinema.model.table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.piotrek.cinema.model.User;

public class UserTableModel {
    private User user;

    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty email;
    private BooleanProperty active;

    public UserTableModel(User user) {
        this(user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnabled());
        this.user = user;
    }

    public UserTableModel(String firstName, String lastName, String email, boolean enabled) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.active = new SimpleBooleanProperty(enabled);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public User getUser() {
        return user;
    }
}
