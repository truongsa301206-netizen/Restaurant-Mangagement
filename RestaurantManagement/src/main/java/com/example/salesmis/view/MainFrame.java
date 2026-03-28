package com.example.salesmis.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(KhachHangManagementPanel khachHangPanel,
                     MonAnManagementPanel     monAnPanel,
                     OrderManagementPanel     orderPanel,
                     RevenueReportPanel       reportPanel) {

        setTitle("Cơm Chiên Manager - Restaurant Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 780);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblBanner = new JLabel("🍳 CƠM CHIÊN MANAGER", SwingConstants.CENTER);
        lblBanner.setFont(new Font("Arial", Font.BOLD, 18));
        lblBanner.setOpaque(true);
        lblBanner.setBackground(new Color(0xC00000));
        lblBanner.setForeground(Color.WHITE);
        lblBanner.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(lblBanner, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("👥 Khách Hàng", khachHangPanel);
        tabs.addTab("🍜 Món Ăn",     monAnPanel);
        tabs.addTab("📋 Đơn Hàng",   orderPanel);
        tabs.addTab("📊 Báo Cáo",    reportPanel); // [cite: 108]

        add(tabs, BorderLayout.CENTER);

        JLabel lblStatus = new JLabel(" Java Swing MVC + Hibernate/JPA + MySQL | com.example.salesmis", SwingConstants.LEFT);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(Color.GRAY);
        add(lblStatus, BorderLayout.SOUTH);
    }
}