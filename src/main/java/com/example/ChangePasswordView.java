
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;

@Route("change-password")
public class ChangePasswordView extends VerticalLayout
        implements BeforeEnterObserver {

    public ChangePasswordView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle()
            .set("background", "linear-gradient(135deg, #06152b, #0b2c50)")
            .set("padding", "20px")
            .set("box-sizing", "border-box");

        PasswordField newPassword =
            new PasswordField("New Password");

        newPassword.setPlaceholder("Enter new password");
        newPassword.setWidthFull();

        PasswordField confirmPassword =
            new PasswordField("Confirm Password");

        confirmPassword.setPlaceholder("Confirm new password");
        confirmPassword.setWidthFull();

        H2 title = new H2("Change Your Password");

        title.getStyle()
            .set("color", "white");

        Button changeButton = new Button("Change Password");

        changeButton.setWidthFull();

        changeButton.getStyle()
            .set("background", "#087BFA")
            .set("color", "white")
            .set("border-radius", "5px");

        changeButton.addClickListener(event -> {

            String username =
                (String) VaadinSession.getCurrent()
                    .getAttribute("username");

            String password = newPassword.getValue();
            String confirm = confirmPassword.getValue();

            if (password.isBlank() || confirm.isBlank()) {

                Notification.show("Complete both password fields.");

                return;
            }

            if (password.length() < 8) {

                Notification.show(
                    "Password must contain at least 8 characters."
                );

                return;
            }

            if (!password.equals(confirm)) {

                Notification.show("Passwords do not match.");

                return;
            }

            boolean changed = UserStore.changePassword(
                username,
                password
            );

            if (changed) {

                Notification.show(
                    "Password changed successfully!"
                );

                getUI().ifPresent(ui ->
                    ui.navigate("dashboard")
                );

            } else {

                Notification.show(
                    "Unable to change password. Please try again."
                );
            }
        });

        VerticalLayout card = new VerticalLayout();

        card.setWidth("350px");
        card.setPadding(true);
        card.setSpacing(true);

        card.getStyle()
            .set("background", "#102744")
            .set("border", "1px solid #315477")
            .set("border-radius", "12px")
            .set("padding", "25px")
            .set("box-sizing", "border-box");

        card.add(
            title,
            newPassword,
            confirmPassword,
            changeButton
        );

        add(card);
    }

    // PAGE ACCESS CONTROL

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        String username =
            (String) VaadinSession.getCurrent()
                .getAttribute("username");

        if (username == null) {

            event.rerouteTo("");
            return;
        }

        if (!UserStore.needsPasswordChange(username)) {

            event.rerouteTo("dashboard");
        }
    }
}
