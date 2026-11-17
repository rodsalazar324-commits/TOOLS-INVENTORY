
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("return-tool")
public class ReturnToolView extends ToolLayout {

    public ReturnToolView() {
        super("return-tool", createContent());
    }

    private static VerticalLayout createContent() {

        VerticalLayout layout = new VerticalLayout();

        layout.setPadding(true);
        layout.setSpacing(true);

        H2 title = new H2("Return Tool");

        title.getStyle().set("color", "white");

        TextField toolId = new TextField("Tool ID");

        toolId.setPlaceholder("Enter borrowed tool ID");
        toolId.setWidth("350px");

        TextField returnedBy = new TextField("Returned By");

        returnedBy.setPlaceholder("Enter person's full name");
        returnedBy.setWidth("350px");

        Button returnButton = new Button("Return Tool");

        returnButton.getStyle()
            .set("background", "#087BFA")
            .set("color", "white");

        returnButton.addClickListener(event -> {

            String id = toolId.getValue().trim();
            String returner = returnedBy.getValue().trim();

            if (id.isEmpty() || returner.isEmpty()) {

                Notification.show(
                    "Please enter the Tool ID and returner's name."
                );

                return;
            }

            Tool tool = ToolStore.findTool(id);

            if (tool == null) {

                Notification.show("Tool not found.");

                return;
            }

            if (!tool.returnTool(returner)) {

                Notification.show(
                    "This tool is not currently borrowed."
                );

                return;
            }

            Notification.show(
                tool.getName() + " returned successfully!"
            );

            toolId.clear();
            returnedBy.clear();
        });

        layout.add(
            title,
            toolId,
            returnedBy,
            returnButton
        );

        return layout;
    }
}
