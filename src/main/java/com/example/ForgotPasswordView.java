
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("forgot-password")
public class ForgotPasswordView extends VerticalLayout {

    public ForgotPasswordView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle()
            .set("background", "linear-gradient(135deg, #06152b, #0b2c50)")
            .set("box-sizing", "border-box")
            .set("padding", "20px");

        setPadding(false);
        setSpacing(false);

        H2 title = new H2("Forgot Password");

        title.getStyle()
            .set("color", "white")
            .set("margin", "0");

        Paragraph message = new Paragraph(
            "Please contact your system administrator "
            + "to request a password reset."
        );

        message.getStyle()
            .set("color", "#DCEBFF")
            .set("text-align", "center")
            .set("line-height", "1.6");

        Paragraph instruction = new Paragraph(
            "The administrator will generate a temporary "
            + "password for your account. You will be required "
            + "to change it after logging in."
        );

        instruction.getStyle()
            .set("color", "#9CB8D8")
            .set("text-align", "center")
            .set("font-size", "13px")
            .set("line-height", "1.6");

        Anchor back = new Anchor("", "Back to Login");

        back.getStyle()
            .set("color", "#60A5FA");

        VerticalLayout card = new VerticalLayout();

        card.setWidth("350px");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.STRETCH);

        card.getStyle()
            .set("background", "#102744")
            .set("border", "1px solid #315477")
            .set("border-radius", "12px")
            .set("padding", "25px")
            .set("box-sizing", "border-box");

        card.add(
            title,
            message,
            instruction,
            back
        );

        add(card);
    }
}
