package com.example.salesmis;

import com.example.salesmis.controller.KhachHangController;
import com.example.salesmis.controller.MonAnController;
import com.example.salesmis.controller.OrderController;
import com.example.salesmis.dao.KhachHangDAO;
import com.example.salesmis.dao.MonAnDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.dao.impl.KhachHangDAOImpl;
import com.example.salesmis.dao.impl.MonAnDAOImpl;
import com.example.salesmis.dao.impl.SalesOrderDAOImpl;
import com.example.salesmis.service.KhachHangService;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.MonAnService;
import com.example.salesmis.service.OrderService;
import com.example.salesmis.service.impl.KhachHangServiceImpl;
import com.example.salesmis.service.impl.LookupServiceImpl;
import com.example.salesmis.service.impl.MonAnServiceImpl;
import com.example.salesmis.service.impl.OrderServiceImpl;
import com.example.salesmis.view.LoginFrame;
import com.example.salesmis.view.*;

import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args) {

        // 1. Khởi tạo DAO
        KhachHangDAO  khachHangDAO  = new KhachHangDAOImpl();
        MonAnDAO      monAnDAO      = new MonAnDAOImpl(); // (Giả sử file đã có)
        SalesOrderDAO salesOrderDAO = new SalesOrderDAOImpl(); // (Giả sử file đã có)

        // 2. Khởi tạo Service
        KhachHangService khachHangService = new KhachHangServiceImpl(khachHangDAO);
        MonAnService     monAnService     = new MonAnServiceImpl(monAnDAO); // (Giả sử file đã có)
        LookupService    lookupService    = new LookupServiceImpl(khachHangDAO, monAnDAO); // (Giả sử file đã có)
        OrderService     orderService     = new OrderServiceImpl(salesOrderDAO, khachHangDAO, monAnDAO); // (Giả sử file đã có)

        // 3. Khởi tạo Controller
        KhachHangController khachHangController = new KhachHangController(khachHangService);
        MonAnController     monAnController     = new MonAnController(monAnService);
        OrderController     orderController     = new OrderController(orderService, lookupService);

        // 4. Khởi tạo View qua Event Dispatch Thread (Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // Khởi động giao diện chính
            MainFrame frame = new MainFrame(
                    new KhachHangManagementPanel(khachHangController),
                    new MonAnManagementPanel(monAnController),
                    new OrderManagementPanel(orderController),
                    new RevenueReportPanel()
            );

            // Login trước khi vào MainFrame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setOnLoginSuccess(() -> frame.setVisible(true));
            loginFrame.setVisible(true);

            

        });
    }
}