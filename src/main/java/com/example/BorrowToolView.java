
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("borrow-tool")
public class BorrowToolView extends ToolLayout {

    public BorrowToolView() {
        super("borrow-tool", createContent());
    }

    private static VerticalLayout createContent() {

        VerticalLayout layout = new VerticalLayout();

        layout.setPadding(true);
        layout.setSpacing(true);

        H2 title = new H2("Borrow Tool");

        title.getStyle().set("color", "white");

        TextField toolId = new TextField("Tool ID");

        toolId.setPlaceholder("Enter tool ID");
        toolId.setWidth("350px");

        TextField borrower = new TextField("Borrower's Name");

        borrower.setPlaceholder("Enter borrower's full name");
        borrower.setWidth("350px");

        Button borrowButton = new Button("Borrow Tool");

        borrowButton.getStyle()
            .set("background", "#087BFA")
            .set("color", "white");

        borrowButton.addClickListener(event -> {

            String id = toolId.getValue().trim();
            String borrowerName = borrower.getValue().trim();

            if (id.isEmpty() || borrowerName.isEmpty()) {

                Notification.show(
                    "Please enter the Tool ID and borrower's name."
                );

                return;
            }

            Tool tool = ToolStore.findTool(id);

            if (tool == null) {

                Notification.show("Tool not found.");

                return;
            }

            if (!tool.borrow(borrowerName)) {

                Notification.show(
                    "This tool is not available."
                );

                return;
            }

            Notification.show(
                tool.getName() + " borrowed by " + borrowerName
            );

            toolId.clear();
            borrower.clear();
        });

        layout.add(
            title,
            toolId,
            borrower,
            borrowButton
        );

        return layout;
    }
}
