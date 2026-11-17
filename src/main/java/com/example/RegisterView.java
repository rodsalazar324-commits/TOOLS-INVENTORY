
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends VerticalLayout {

    public RegisterView() {

        // MAIN BACKGROUND

        getStyle()
            .set("background", "linear-gradient(135deg, #06152b, #0b2c50)")
            .set("min-height", "100vh")
            .set("width", "100%")
            .set("box-sizing", "border-box");

        setPadding(false);
        setSpacing(false);

        setAlignItems(FlexComponent.Alignment.CENTER);

        setJustifyContentMode(
            FlexComponent.JustifyContentMode.CENTER
        );

        // REGISTER CARD

        VerticalLayout registerCard = new VerticalLayout();

        registerCard.setWidth("380px");
        registerCard.setPadding(true);
        registerCard.setSpacing(true);

        registerCard.setAlignItems(
            FlexComponent.Alignment.STRETCH
        );

        registerCard.getStyle()
            .set("background", "#081D35")
            .set("border", "1px solid #183653")
            .set("border-radius", "8px")
            .set("padding", "30px")
            .set("box-sizing", "border-box")
            .set("box-shadow", "0 8px 25px rgba(0,0,0,0.3)");

        // TITLE

        H1 title = new H1("Create Account");

        title.getStyle()
            .set("color", "white")
            .set("font-size", "25px")
            .set("margin", "0");

        Span description = new Span(
            "Register to access the Tool Management System"
        );

        description.getStyle()
            .set("color", "#91A9C5")
            .set("font-size", "12px");

        // USERNAME

        TextField username = new TextField("Username");

        username.setPlaceholder("Enter username");
        username.setPrefixComponent(VaadinIcon.USER.create());
        username.setWidthFull();

        // EMAIL

        TextField email = new TextField("Email");

        email.setPlaceholder("Enter email address");
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        email.setWidthFull();

        // ACCOUNT TYPE

        ComboBox<String> role = new ComboBox<>();

        role.setLabel("Account Type");
        role.setItems("Employer", "Admin");
        role.setValue("Employer");
        role.setWidthFull();
        role.setAllowCustomValue(false);

        // ADMIN AUTHORIZATION CODE

        PasswordField adminCode =
            new PasswordField("Admin Authorization Code");

        adminCode.setPlaceholder("Enter admin code");
        adminCode.setPrefixComponent(VaadinIcon.LOCK.create());
        adminCode.setWidthFull();
        adminCode.setVisible(false);

        role.addValueChangeListener(event -> {

            adminCode.setVisible(
                "Admin".equals(event.getValue())
            );

        });

        // PASSWORD

        PasswordField password = new PasswordField("Password");

        password.setPlaceholder("Enter password");
        password.setPrefixComponent(VaadinIcon.LOCK.create());
        password.setWidthFull();

        // CONFIRM PASSWORD

        PasswordField confirmPassword =
            new PasswordField("Confirm Password");

        confirmPassword.setPlaceholder("Confirm your password");
        confirmPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        confirmPassword.setWidthFull();

        // REGISTER BUTTON

        Button register = new Button("Register");

        register.setWidthFull();

        register.getStyle()
            .set("background", "#087BFA")
            .set("color", "white")
            .set("border-radius", "5px");

        // REGISTER FUNCTION

        register.addClickListener(event -> {

            String user = username.getValue().trim();

            String userEmail = email.getValue().trim();

            String pass = password.getValue();

            String confirm = confirmPassword.getValue();

            String selectedRole = role.getValue();

            String enteredAdminCode = adminCode.getValue();

            if (user.isEmpty() || userEmail.isEmpty()
                    || pass.isEmpty() || confirm.isEmpty()) {

                Notification.show(
                    "Please complete all fields"
                );

                return;
            }

            if (!userEmail.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                Notification.show(
                    "Please enter a valid email address"
                );

                return;
            }

            if (pass.length() < 8) {

                Notification.show(
                    "Password must be at least 8 characters"
                );

                return;
            }

            if (!pass.equals(confirm)) {

                Notification.show(
                    "Passwords do not match"
                );

                return;
            }

            if ("Admin".equals(selectedRole)
                    && enteredAdminCode.isBlank()) {

                Notification.show(
                    "Enter the admin authorization code"
                );

                return;
            }

            try {

                boolean success = UserStore.register(
                    user,
                    userEmail,
                    pass,
                    selectedRole,
                    enteredAdminCode
                );

                if (success) {

                    Notification.show(
                        selectedRole +
                        " account registered successfully!"
                    );

                    getUI().ifPresent(ui ->
                        ui.navigate("")
                    );

                } else {

                    Notification.show(
                        "Registration failed. Check your details, "
                        + "authorization code, or existing account."
                    );

                }

            } catch (RuntimeException e) {

                Notification.show(
                    "Unable to register. Please try again."
                );

                e.printStackTrace();
            }

        });

        // BACK TO LOGIN BUTTON

        Button back = new Button("Back to Login");

        back.setWidthFull();

        back.getStyle()
            .set("background", "transparent")
            .set("color", "white")
            .set("border", "1px solid #315477")
            .set("border-radius", "5px");

        back.addClickListener(event ->
            getUI().ifPresent(ui -> ui.navigate(""))
        );

        // ADD COMPONENTS

        registerCard.add(
            title,
            description,
            username,
            email,
            role,
            adminCode,
            password,
            confirmPassword,
            register,
            back
        );

        add(registerCard);
    }
}
