
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.Route;

@Route("manage-users")
public class AdminResetPasswordView extends ToolLayout {

    public AdminResetPasswordView() {
        super("manage-users", createContent());
    }

    private static VerticalLayout createContent() {

        VerticalLayout content = new VerticalLayout();

        content.setWidthFull();
        content.setPadding(true);
        content.setSpacing(true);

        H2 title = new H2("Manage Users");

        title.getStyle()
            .set("color", "white");

        Paragraph description = new Paragraph(
            "Select an Employer account to generate a temporary password."
        );

        description.getStyle()
            .set("color", "#9CB8D8");

        ComboBox<String> employer = new ComboBox<>("Select Employer");

        employer.setItems(UserStore.getEmployers());
        employer.setPlaceholder("Choose an Employer");
        employer.setWidth("350px");

        TextField temporaryPassword =
            new TextField("Temporary Password");

        temporaryPassword.setWidth("350px");
        temporaryPassword.setReadOnly(true);
        temporaryPassword.setVisible(false);

        Paragraph instruction = new Paragraph(
            "The Employer must use this password to log in "
            + "and create a new password."
        );

        instruction.getStyle()
            .set("color", "#9CB8D8")
            .set("font-size", "13px");

        Button resetButton =
            new Button("Generate Temporary Password");

        resetButton.getStyle()
            .set("background", "#087BFA")
            .set("color", "white")
            .set("border-radius", "5px");

        resetButton.addClickListener(event -> {

            String selectedEmployer = employer.getValue();

            if (selectedEmployer == null
                    || selectedEmployer.isBlank()) {

                Notification.show("Select an Employer account.");

                return;
            }

            String adminUsername =
                (String) VaadinSession.getCurrent()
                    .getAttribute("username");

            String generatedPassword =
                UserStore.resetEmployerPassword(
                    adminUsername,
                    selectedEmployer
                );

            if (generatedPassword != null) {

                temporaryPassword.setValue(generatedPassword);
                temporaryPassword.setVisible(true);

                Notification.show(
                    "Temporary password generated successfully."
                );

                employer.setItems(UserStore.getEmployers());

            } else {

                Notification.show(
                    "Unable to reset this account. Check Admin permissions."
                );
            }
        });

        content.getStyle()
            .set("padding", "30px")
            .set("box-sizing", "border-box");

        content.add(
            title,
            description,
            employer,
            resetButton,
            temporaryPassword,
            instruction
        );

        return content;
    }
}
