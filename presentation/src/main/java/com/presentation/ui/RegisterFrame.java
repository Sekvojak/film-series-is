package com.presentation.ui;

import com.domain.User;
import com.service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final UserService userService = new UserService();

    private JTextField fnameField;
    private JTextField lnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterFrame() {
        setTitle("Registrácia");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fnameField = new JTextField();
        lnameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        panel.add(new JLabel("Meno:"));
        panel.add(fnameField);

        panel.add(new JLabel("Priezvisko:"));
        panel.add(lnameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Heslo:"));
        panel.add(passwordField);

        panel.add(new JLabel("Potvrď heslo:"));
        panel.add(confirmPasswordField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton registerBtn = new JButton("Zaregistrovať");
        JButton backBtn = new JButton("Späť");

        registerBtn.addActionListener(e -> register());
        backBtn.addActionListener(e -> goBack());

        panel.add(registerBtn);
        panel.add(backBtn);

        return panel;
    }

    private void register() {
        String fname = fnameField.getText().trim();
        String lname = lnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();

        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vyplň všetky polia.");
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Heslá sa nezhodujú.");
            return;
        }

        // pokus o registráciu
        boolean success = userService.register(fname, lname, email, password);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Email už existuje!");
            return;
        }

        JOptionPane.showMessageDialog(this, "Registrácia úspešná!");

        new LoginFrame().setVisible(true);
        dispose();
    }

    private void goBack() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
