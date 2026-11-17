
package com.example;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("dashboard")
public class MainDashboard extends ToolLayout {

    public MainDashboard() {
        super("dashboard", createPage());
    }

    private static VerticalLayout createPage() {

        // LOAD INITIAL TOOL CATALOG
        ToolCatalog.loadTools();

        VerticalLayout page = new VerticalLayout();

        page.setPadding(true);
        page.setSpacing(true);

        page.getStyle()
            .set("box-sizing", "border-box");

        // GET USER INFORMATION

        String username = (String) VaadinSession.getCurrent()
            .getAttribute("username");

        String role = (String) VaadinSession.getCurrent()
            .getAttribute("role");

        if (username == null) {
            username = "Guest";
        }

        if (role == null) {
            role = "Employer";
        }

        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        // TITLE

        H2 title = new H2("Main Dashboard");

        title.getStyle()
            .set("color", "white")
            .set("margin", "0");

        // WELCOME MESSAGE

        Span welcome = new Span(
            "Welcome, " + username + " (" + role + ")"
        );

        welcome.getStyle()
            .set("color", "#91A9C5")
            .set("font-size", "14px");

        // COUNTER CARDS

        HorizontalLayout cards = new HorizontalLayout();

        cards.setWidthFull();

        cards.setSpacing(true);

        cards.add(
            counterCard("Available", "#10B981"),
            counterCard("Borrowed", "#EF4444"),
            counterCard("Under Maintenance", "#F59E0B"),
            counterCard("Lost", "#94A3B8")
        );

        // ADD TOOL BUTTON

        Button add = new Button(
            "Add New Tool",
            VaadinIcon.PLUS.create()
        );

        add.getStyle()
            .set("background", "#087BFA")
            .set("color", "white");

        add.addClickListener(e ->
            UI.getCurrent().navigate("add-tool")
        );

        // SEARCH BUTTON

        Button search = new Button(
            "Search Tools",
            VaadinIcon.SEARCH.create()
        );

        search.addClickListener(e ->
            UI.getCurrent().navigate("search-tool")
        );

        // ACTION BUTTONS

        HorizontalLayout actions = new HorizontalLayout();

        if (isAdmin) {
            actions.add(add, search);
        } else {
            actions.add(search);
        }

        // ADD COMPONENTS

        page.add(
            title,
            welcome,
            cards,
            actions
        );

        return page;
    }

    // COUNTER CARD METHOD

    private static VerticalLayout counterCard(
        String status,
        String color
    ) {

        int count = (int) ToolStore.getTools().stream()
            .filter(tool ->
                status.equalsIgnoreCase(tool.getStatus())
            )
            .count();

        H3 number = new H3(String.valueOf(count));

        number.getStyle()
            .set("color", color)
            .set("margin", "0");

        Span label = new Span(status);

        label.getStyle()
            .set("color", "white");

        VerticalLayout card = new VerticalLayout(
            label,
            number
        );

        card.setWidthFull();

        card.getStyle()
            .set("background", "#102744")
            .set("border", "1px solid #24466D")
            .set("border-radius", "8px")
            .set("padding", "15px")
            .set("box-sizing", "border-box");

        return card;
    }
}
