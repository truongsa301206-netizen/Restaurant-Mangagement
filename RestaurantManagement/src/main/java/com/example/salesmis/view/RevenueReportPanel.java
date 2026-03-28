package com.example.salesmis.view;

import com.example.salesmis.config.JpaUtil;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RevenueReportPanel extends JPanel {

    private JTable tblRevenue;
    private DefaultTableModel revenueTableModel;
    private JTable tblTopMon;
    private DefaultTableModel topMonTableModel;

    public RevenueReportPanel() {
        initComponents();
        loadReports();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblHeader = new JLabel("BÁO CÁO DOANH THU VÀ TOP MÓN BÁN CHẠY");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblHeader, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildMonthlyRevenuePanel(),
                buildTopMonAnPanel()
        );
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Làm mới báo cáo");
        btnRefresh.addActionListener(e -> loadReports());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel buildMonthlyRevenuePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(new JLabel("Doanh thu theo tháng"), BorderLayout.NORTH);

        String[] cols = {"Năm", "Tháng", "Số đơn", "Tổng doanh thu"};
        revenueTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRevenue = new JTable(revenueTableModel);
        panel.add(new JScrollPane(tblRevenue), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTopMonAnPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(new JLabel("Top 10 món ăn bán chạy nhất"), BorderLayout.NORTH);

        String[] cols = {"Tên món", "Tổng SL bán", "Tổng tiền"};
        topMonTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTopMon = new JTable(topMonTableModel);
        panel.add(new JScrollPane(tblTopMon), BorderLayout.CENTER);
        return panel;
    }

    private void loadReports() {
        loadMonthlyRevenue();
        loadTopMonAn();
    }

    @SuppressWarnings("unchecked")
    private void loadMonthlyRevenue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                    "SELECT YEAR(so.ngay), MONTH(so.ngay), COUNT(so), SUM(so.tongTien) " +
                            "FROM SalesOrder so WHERE so.trangThai = 'HOANTHANH' " +
                            "GROUP BY YEAR(so.ngay), MONTH(so.ngay) " +
                            "ORDER BY YEAR(so.ngay) DESC, MONTH(so.ngay) DESC", Object[].class).getResultList();

            revenueTableModel.setRowCount(0);
            for (Object[] row : rows) {
                revenueTableModel.addRow(row);
            }
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTopMonAn() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                            "SELECT d.monAn.tenMon, SUM(d.soLuong), SUM(d.thanhTien) " +
                                    "FROM OrderDetail d JOIN d.salesOrder so WHERE so.trangThai = 'HOANTHANH' " +
                                    "GROUP BY d.monAn.id, d.monAn.tenMon " +
                                    "ORDER BY SUM(d.soLuong) DESC", Object[].class)
                    .setMaxResults(10)
                    .getResultList();

            topMonTableModel.setRowCount(0);
            for (Object[] row : rows) {
                topMonTableModel.addRow(row);
            }
        } finally {
            em.close();
        }
    }
}