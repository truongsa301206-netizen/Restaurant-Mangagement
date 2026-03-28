package com.example.salesmis.view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Đăng nhập - Cơm Chiên Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("CƠM CHIÊN MANAGER", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Tài khoản:"), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Mật khẩu:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        btnLogin = new JButton("Đăng Nhập");
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        add(panel);

        // Giả lập Login thành công để demo
        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            if (user.equals("admin") && pass.equals("123")) {
                this.dispose(); // Tự tắt đi và cho Launcher kích hoạt MainFrame
            } else {
                JOptionPane.showMessageDialog(this, "Thông tin tài khoản 'admin'/'123'", "Gợi ý", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }
}