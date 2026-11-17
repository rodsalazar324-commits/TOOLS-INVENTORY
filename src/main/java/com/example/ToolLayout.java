
package com.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class ToolLayout extends VerticalLayout
        implements BeforeEnterObserver {

    private final String borderColor = "#24466D";
    private final String activePage;

    public ToolLayout(String activePage, Component pageContent) {

        this.activePage = activePage;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        getStyle()
            .set("background", "linear-gradient(135deg, #06152b, #0b2c50)")
            .set("color", "#DCEBFF")
            .set("overflow", "auto");

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

        // HEADER

        HorizontalLayout header = new HorizontalLayout();

        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(
            FlexComponent.JustifyContentMode.BETWEEN
        );

        header.getStyle()
            .set("padding", "10px 20px")
            .set("background", "#07172B")
            .set("border", "1px solid " + borderColor)
            .set("box-sizing", "border-box");

        Span logo = new Span("🔧");
        logo.getStyle().set("font-size", "25px");

        H3 title = new H3("Tool Management System");

        title.getStyle()
            .set("color", "white")
            .set("margin", "0");

        HorizontalLayout branding =
            new HorizontalLayout(logo, title);

        branding.setAlignItems(FlexComponent.Alignment.CENTER);

        Span adminName = new Span(username + " (" + role + ")");
        adminName.getStyle().set("color", "white");

        Button logout = new Button(
            "Logout",
            VaadinIcon.SIGN_OUT.create()
        );

        logout.getStyle()
            .set("background", "#DC2626")
            .set("color", "white")
            .set("border-radius", "5px");

        logout.addClickListener(e -> {

            VaadinSession.getCurrent().close();

            getUI().ifPresent(ui ->
                ui.navigate("")
            );
        });

        HorizontalLayout userActions =
            new HorizontalLayout(adminName, logout);

        userActions.setAlignItems(FlexComponent.Alignment.CENTER);

        header.add(branding, userActions);

        // SIDEBAR

        VerticalLayout sidebar = new VerticalLayout();

        sidebar.setWidth("150px");
        sidebar.setPadding(true);
        sidebar.setSpacing(false);

        sidebar.getStyle()
            .set("background", "rgba(5, 23, 43, 0.8)")
            .set("border-right", "1px solid " + borderColor)
            .set("box-sizing", "border-box")
            .set("gap", "8px");

        sidebar.add(
            menuButton(
                VaadinIcon.HOME,
                "Dashboard",
                "dashboard",
                activePage
            )
        );

        // ADMIN ONLY MENU

        if (isAdmin) {

            sidebar.add(
                menuButton(
                    VaadinIcon.PLUS_CIRCLE,
                    "Add Tool",
                    "add-tool",
                    activePage
                ),
                menuButton(
                    VaadinIcon.EYE,
                    "Display Tools",
                    "display-tools",
                    activePage
                ),
                menuButton(
                    VaadinIcon.EDIT,
                    "Update Tool",
                    "update-tool",
                    activePage
                ),
                menuButton(
                    VaadinIcon.TRASH,
                    "Delete Tool",
                    "delete-tool",
                    activePage
                ),
                menuButton(
                    VaadinIcon.USERS,
                    "Manage Users",
                    "manage-users",
                    activePage
                )
            );
        }

        // BOTH ADMIN AND EMPLOYER

        sidebar.add(
            menuButton(
                VaadinIcon.SEARCH,
                "Search Tool",
                "search-tool",
                activePage
            ),
            menuButton(
                VaadinIcon.HANDSHAKE,
                "Borrow Tool",
                "borrow-tool",
                activePage
            ),
            menuButton(
                VaadinIcon.ARROW_CIRCLE_LEFT,
                "Return Tool",
                "return-tool",
                activePage
            )
        );

        // PAGE AREA

        HorizontalLayout body = new HorizontalLayout();

        body.setWidthFull();
        body.setPadding(false);
        body.setSpacing(false);

        body.setAlignItems(FlexComponent.Alignment.STRETCH);

        pageContent.getElement()
            .getStyle()
            .set("width", "100%")
            .set("min-width", "0")
            .set("box-sizing", "border-box");

        body.add(sidebar, pageContent);
        body.setFlexGrow(1, pageContent);

        add(header, body);

        setFlexGrow(1, body);
    }

    // PAGE ACCESS CONTROL

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        String username = (String) VaadinSession.getCurrent()
            .getAttribute("username");

        String role = (String) VaadinSession.getCurrent()
            .getAttribute("role");

        if (username == null || role == null) {

            event.rerouteTo("");
            return;
        }

        // FORCE PASSWORD CHANGE

        if (UserStore.needsPasswordChange(username)) {

            event.rerouteTo("change-password");
            return;
        }

        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        boolean adminOnlyPage =
            activePage.equals("add-tool")
            || activePage.equals("display-tools")
            || activePage.equals("update-tool")
            || activePage.equals("delete-tool")
            || activePage.equals("manage-users");

        if (adminOnlyPage && !isAdmin) {
            event.rerouteTo("dashboard");
        }
    }

    // MENU BUTTON

    private Button menuButton(
        VaadinIcon icon,
        String text,
        String route,
        String activePage
    ) {

        Button button = new Button(text, icon.create());

        button.setWidthFull();

        button.getStyle()
            .set(
                "background",
                activePage.equals(route)
                    ? "#0756B8"
                    : "transparent"
            )
            .set("color", "white")
            .set("text-align", "left")
            .set("font-size", "12px")
            .set("font-weight", "600")
            .set("border", "1px solid #183653")
            .set("border-radius", "5px")
            .set("justify-content", "flex-start")
            .set("gap", "8px")
            .set("padding", "8px 10px")
            .set("min-height", "35px")
            .set("box-sizing", "border-box");

        button.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate(route))
        );

        return button;
    }
}
