
package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserStore {

    private static final Path FILE =
        Paths.get("data", "users.json");

    private static final ObjectMapper mapper =
        new ObjectMapper();

    private static final BCryptPasswordEncoder encoder =
        new BCryptPasswordEncoder();

    private static final SecureRandom random =
        new SecureRandom();

    private static final String ADMIN_CODE =
        System.getenv("ADMIN_REGISTRATION_CODE");

    private static final String TEMP_CHARS =
        "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$";

    // READ JSON FILE

    private static ArrayNode loadUsers() {

        try {
            Files.createDirectories(FILE.getParent());

            if (!Files.exists(FILE)) {
                Files.writeString(FILE, "[]");
            }

            JsonNode data = mapper.readTree(FILE.toFile());

            if (data == null || !data.isArray()) {
                throw new IOException("Invalid users.json");
            }

            return (ArrayNode) data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // REGISTER ACCOUNT

    public static boolean register(
            String username,
            String password) {

        return register(username, "", password, "Employer", "");
    }

    public static boolean register(
            String username,
            String email,
            String password) {

        return register(username, email, password, "Employer", "");
    }

    // REGISTER ACCOUNT WITH ROLE

    public static boolean register(
            String username,
            String email,
            String password,
            String role,
            String adminCode) {

        if (username == null || username.isBlank()
                || password == null || password.length() < 8
                || role == null) {
            return false;
        }

        if (!role.equals("Admin") && !role.equals("Employer")) {
            return false;
        }

        if (role.equals("Admin")) {

            if (ADMIN_CODE == null || ADMIN_CODE.isBlank()
                    || !ADMIN_CODE.equals(adminCode)) {
                return false;
            }
        }

        String cleanUsername = username.trim();
        String cleanEmail = email == null ? "" : email.trim();

        ArrayNode users = loadUsers();

        for (JsonNode user : users) {

            if (user.path("username").asText("")
                    .equalsIgnoreCase(cleanUsername)) {
                return false;
            }

            if (!cleanEmail.isBlank()
                    && user.path("email").asText("")
                    .equalsIgnoreCase(cleanEmail)) {
                return false;
            }
        }

        ObjectNode newUser = mapper.createObjectNode();

        newUser.put("username", cleanUsername);
        newUser.put("email", cleanEmail);
        newUser.put("password", encoder.encode(password));
        newUser.put("role", role);
        newUser.put("mustChangePassword", false);

        users.add(newUser);

        saveUsers(users);

        return true;
    }

    // LOGIN ACCOUNT

    public static boolean authenticate(
            String username,
            String password) {

        if (username == null || password == null) {
            return false;
        }

        ArrayNode users = loadUsers();

        for (JsonNode user : users) {

            String savedUsername =
                user.path("username").asText("");

            String savedPassword =
                user.path("password").asText("");

            if (savedUsername.equalsIgnoreCase(username)
                    && encoder.matches(password, savedPassword)) {
                return true;
            }
        }

        return false;
    }

    // GET ACCOUNT ROLE

    public static String getRole(String username) {

        ArrayNode users = loadUsers();

        for (JsonNode user : users) {

            if (user.path("username").asText("")
                    .equalsIgnoreCase(username)) {

                return user.path("role").asText("Employer");
            }
        }

        return null;
    }

    // CHECK IF USER MUST CHANGE PASSWORD

    public static boolean needsPasswordChange(String username) {

        ArrayNode users = loadUsers();

        for (JsonNode user : users) {

            if (user.path("username").asText("")
                    .equalsIgnoreCase(username)) {

                return user.path("mustChangePassword")
                    .asBoolean(false);
            }
        }

        return false;
    }

    // GET ALL EMPLOYER USERNAMES

    public static List<String> getEmployers() {

        List<String> employers = new ArrayList<>();

        ArrayNode users = loadUsers();

        for (JsonNode user : users) {

            String role = user.path("role").asText("Employer");

            if (role.equalsIgnoreCase("Employer")) {

                employers.add(
                    user.path("username").asText("")
                );
            }
        }

        return employers;
    }

    // GENERATE TEMPORARY PASSWORD

    private static String generateTemporaryPassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {

            int index = random.nextInt(TEMP_CHARS.length());

            password.append(TEMP_CHARS.charAt(index));
        }

        return password.toString();
    }

    // ADMIN RESETS EMPLOYER PASSWORD

    public static String resetEmployerPassword(
            String adminUsername,
            String employerUsername) {

        if (adminUsername == null || employerUsername == null) {
            return null;
        }

        ArrayNode users = loadUsers();

        ObjectNode targetUser = null;
        boolean validAdmin = false;

        for (JsonNode user : users) {

            String username =
                user.path("username").asText("");

            String role =
                user.path("role").asText("Employer");

            if (username.equalsIgnoreCase(adminUsername)
                    && role.equalsIgnoreCase("Admin")) {
                validAdmin = true;
            }

            if (username.equalsIgnoreCase(employerUsername)
                    && role.equalsIgnoreCase("Employer")) {
                targetUser = (ObjectNode) user;
            }
        }

        if (!validAdmin || targetUser == null) {
            return null;
        }

        String temporaryPassword =
            generateTemporaryPassword();

        targetUser.put(
            "password",
            encoder.encode(temporaryPassword)
        );

        targetUser.put("mustChangePassword", true);

        saveUsers(users);

        return temporaryPassword;
    }

    // CHANGE TEMPORARY PASSWORD

    public static boolean changePassword(
            String username,
            String newPassword) {

        if (username == null || newPassword == null
                || newPassword.length() < 8) {
            return false;
        }

        ArrayNode users = loadUsers();

        for (JsonNode user : users) {

            if (user.path("username").asText("")
                    .equalsIgnoreCase(username)) {

                if (!user.path("mustChangePassword")
                        .asBoolean(false)) {
                    return false;
                }

                ObjectNode account = (ObjectNode) user;

                account.put(
                    "password",
                    encoder.encode(newPassword)
                );

                account.put("mustChangePassword", false);

                saveUsers(users);

                return true;
            }
        }

        return false;
    }

    // SAVE JSON FILE

    private static void saveUsers(ArrayNode users) {

        try {

            mapper.writerWithDefaultPrettyPrinter()
                .writeValue(FILE.toFile(), users);

            System.out.println("Account saved successfully!");

            System.out.println(
                "JSON location: " + FILE.toAbsolutePath()
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
