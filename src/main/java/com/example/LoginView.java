
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
public class LoginView extends VerticalLayout {

    public LoginView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle()
            .set("background", "linear-gradient(135deg, #06152b, #0b2c50)");

        H2 title = new H2("Tool Management System");
        title.getStyle().set("color", "white");

        TextField username = new TextField("Username");
        username.setWidthFull();

        PasswordField password = new PasswordField("Password");
        password.setWidthFull();

        Button login = new Button("Login");
        login.setWidthFull();

        login.getStyle()
            .set("background", "#087BFA")
            .set("color", "white");

        Anchor forgotPassword = new Anchor(
            "forgot-password",
            "Forgot Password?"
        );

        forgotPassword.getStyle()
            .set("color", "#60A5FA");

        Anchor register = new Anchor(
            "register",
            "Create an Account"
        );

        register.getStyle()
            .set("color", "#60A5FA");

        login.addClickListener(event -> {

            String enteredUsername = username.getValue().trim();
            String enteredPassword = password.getValue();

            if (enteredUsername.isEmpty()
                    || enteredPassword.isEmpty()) {

                Notification.show("Enter username and password.");
                return;
            }

            boolean authenticated = UserStore.authenticate(
                enteredUsername,
                enteredPassword
            );

            if (authenticated) {

                String role = UserStore.getRole(enteredUsername);

                VaadinSession.getCurrent().setAttribute(
                    "username", enteredUsername
                );

                VaadinSession.getCurrent().setAttribute(
                    "role", role
                );

                Notification.show(
                    "Welcome, " + enteredUsername
                );

                if (UserStore.needsPasswordChange(enteredUsername)) {

                    getUI().ifPresent(ui ->
                        ui.navigate("change-password")
                    );

                } else {

                    getUI().ifPresent(ui ->
                        ui.navigate("dashboard")
                    );
                }

            } else {

                Notification.show(
                    "Incorrect username or password."
                );
            }
        });

        VerticalLayout loginCard = new VerticalLayout(
            title,
            username,
            password,
            forgotPassword,
            register,
            login
        );

        loginCard.setWidth("350px");
        loginCard.setPadding(true);
        loginCard.setSpacing(true);

        loginCard.getStyle()
            .set("background", "#102744")
            .set("border", "1px solid #315477")
            .set("border-radius", "12px")
            .set("padding", "25px")
            .set("box-sizing", "border-box");

        add(loginCard);
    }
}
