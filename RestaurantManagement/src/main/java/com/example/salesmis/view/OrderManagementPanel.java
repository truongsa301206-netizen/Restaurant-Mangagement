package com.example.salesmis.view;

import com.example.salesmis.controller.OrderController;
import com.example.salesmis.model.entity.KhachHang;
import com.example.salesmis.model.entity.MonAn;
import com.example.salesmis.model.entity.OrderDetail;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.entity.SalesOrder.TrangThaiDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagementPanel extends JPanel {

    private final OrderController orderController;

    private JTextField         txtOrderId;
    private JComboBox<String>  cboKhachHang;
    private JComboBox<String>  cboTrangThai;
    private JLabel             lblTongTien;

    private JTable             tblOrders;
    private DefaultTableModel  orderTableModel;
    private Long               selectedOrderId;

    private JTable             tblDetails;
    private DefaultTableModel  detailTableModel;

    private JComboBox<String>  cboMonAn;
    private JTextField         txtSoLuong;

    private Map<String, Long> khachHangMap = new HashMap<>();
    private Map<String, Long> monAnMap     = new HashMap<>();

    public OrderManagementPanel(OrderController orderController) {
        this.orderController = orderController;
        initComponents();
        loadDropdowns();
        loadOrderTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblHeader = new JLabel("QUẢN LÝ ĐƠN HÀNG");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblHeader, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                buildTopPanel(),
                buildDetailPanel()
        );
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(buildOrderFormPanel(), BorderLayout.NORTH);
        panel.add(buildOrderTablePanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildOrderFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));

        txtOrderId = new JTextField(); txtOrderId.setEditable(false);
        cboKhachHang = new JComboBox<>();
        cboKhachHang.addItem("-- Khách vãng lai --");
        cboTrangThai = new JComboBox<>(new String[]{"TẤT CẢ", "MOI", "HOANTHANH", "HUY"});

        lblTongTien = new JLabel("0 VND");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTien.setForeground(new Color(0xC00000));

        form.add(new JLabel("Order ID:")); form.add(txtOrderId);
        form.add(new JLabel("Khách hàng:")); form.add(cboKhachHang);
        form.add(new JLabel("Lọc trạng thái:")); form.add(cboTrangThai);
        form.add(new JLabel("Tổng tiền:")); form.add(lblTongTien);

        panel.add(form, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNewOrder = new JButton("Tạo đơn mới");
        JButton btnComplete = new JButton("Hoàn thành");
        JButton btnCancel = new JButton("Hủy đơn");
        JButton btnDelete = new JButton("Xóa đơn");
        JButton btnFilter = new JButton("Lọc");

        buttonPanel.add(btnNewOrder); buttonPanel.add(btnComplete);
        buttonPanel.add(btnCancel); buttonPanel.add(btnDelete);
        buttonPanel.add(btnFilter);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnNewOrder.addActionListener(e -> createNewOrder());
        btnComplete.addActionListener(e -> updateTrangThai(TrangThaiDon.HOANTHANH));
        btnCancel.addActionListener(e -> updateTrangThai(TrangThaiDon.HUY));
        btnDelete.addActionListener(e -> deleteOrder());
        btnFilter.addActionListener(e -> loadOrderTable());

        return panel;
    }

    private JScrollPane buildOrderTablePanel() {
        String[] columns = {"ID", "Ngày tạo", "Khách hàng", "Tổng tiền", "Trạng thái"};
        orderTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblOrders = new JTable(orderTableModel);
        tblOrders.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblOrders.getSelectedRow() != -1) {
                fillOrderFormFromSelectedRow();
                loadDetailTable();
            }
        });
        return new JScrollPane(tblOrders);
    }

    private JPanel buildDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JLabel lblDetail = new JLabel("CHI TIẾT ĐƠN HÀNG");
        lblDetail.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblDetail, BorderLayout.NORTH);

        String[] detailColumns = {"Detail ID", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"};
        detailTableModel = new DefaultTableModel(detailColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetails = new JTable(detailTableModel);
        panel.add(new JScrollPane(tblDetails), BorderLayout.CENTER);

        JPanel addMonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cboMonAn = new JComboBox<>();
        txtSoLuong = new JTextField("1", 5);
        JButton btnAddMon = new JButton("Thêm món");
        JButton btnRemoveMon = new JButton("Xóa dòng");

        addMonPanel.add(new JLabel("Chọn món:")); addMonPanel.add(cboMonAn);
        addMonPanel.add(new JLabel("Số lượng:")); addMonPanel.add(txtSoLuong);
        addMonPanel.add(btnAddMon); addMonPanel.add(btnRemoveMon);

        panel.add(addMonPanel, BorderLayout.SOUTH);

        btnAddMon.addActionListener(e -> addMonAnToCurrentOrder());
        btnRemoveMon.addActionListener(e -> removeDetailFromCurrentOrder());

        return panel;
    }

    private void loadDropdowns() {
        khachHangMap.clear(); cboKhachHang.removeAllItems();
        cboKhachHang.addItem("-- Khách vãng lai --");
        for (KhachHang kh : orderController.getAllKhachHang()) {
            String display = kh.getTen() + " (" + kh.getSdt() + ")";
            khachHangMap.put(display, kh.getId());
            cboKhachHang.addItem(display);
        }

        monAnMap.clear(); cboMonAn.removeAllItems();
        for (MonAn ma : orderController.getAllActiveMonAn()) {
            String display = ma.getTenMon() + " - " + ma.getGia() + " VND";
            monAnMap.put(display, ma.getId());
            cboMonAn.addItem(display);
        }
    }

    private void loadOrderTable() {
        String filter = cboTrangThai.getSelectedItem().toString();
        List<SalesOrder> orders = orderController.getOrdersByTrangThai(filter);
        orderTableModel.setRowCount(0);
        for (SalesOrder so : orders) {
            String khName = so.getKhachHang() != null ? so.getKhachHang().getTen() : "Khách vãng lai";
            orderTableModel.addRow(new Object[]{so.getId(), so.getNgay(), khName, so.getTongTien(), so.getTrangThai()});
        }
    }

    private void loadDetailTable() {
        if (selectedOrderId == null) return;
        SalesOrder so = orderController.getOrderById(selectedOrderId);
        detailTableModel.setRowCount(0);
        for (OrderDetail d : so.getDetails()) {
            detailTableModel.addRow(new Object[]{d.getId(), d.getMonAn().getTenMon(), d.getSoLuong(), d.getDonGia(), d.getThanhTien()});
        }
        lblTongTien.setText(so.getTongTien() + " VND");
    }

    private void createNewOrder() {
        try {
            String selectedKh = cboKhachHang.getSelectedItem().toString();
            String khIdStr = selectedKh.startsWith("--") ? null : khachHangMap.get(selectedKh).toString();

            Map<Long, Integer> emptyItems = new HashMap<>();
            if (!monAnMap.isEmpty()) {
                emptyItems.put(monAnMap.values().iterator().next(), 1);
            } else {
                JOptionPane.showMessageDialog(this, "Không có món ăn nào!"); return;
            }

            orderController.createOrder(khIdStr, emptyItems);
            loadOrderTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTrangThai(TrangThaiDon tt) {
        if (selectedOrderId == null) return;
        try {
            orderController.updateTrangThai(selectedOrderId, tt.name());
            loadOrderTable();
            loadDetailTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteOrder() {
        if (selectedOrderId == null) return;
        try {
            orderController.deleteOrder(selectedOrderId);
            selectedOrderId = null;
            txtOrderId.setText("");
            lblTongTien.setText("0 VND");
            detailTableModel.setRowCount(0);
            loadOrderTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMonAnToCurrentOrder() {
        if (selectedOrderId == null) return;
        try {
            Long monAnId = monAnMap.get(cboMonAn.getSelectedItem().toString());
            orderController.addMonAnToOrder(selectedOrderId, monAnId, txtSoLuong.getText());
            loadDetailTable();
            loadOrderTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeDetailFromCurrentOrder() {
        int row = tblDetails.getSelectedRow();
        if (row == -1) return;
        try {
            Long detailId = Long.valueOf(detailTableModel.getValueAt(row, 0).toString());
            orderController.removeDetailFromOrder(selectedOrderId, detailId);
            loadDetailTable();
            loadOrderTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillOrderFormFromSelectedRow() {
        int row = tblOrders.getSelectedRow();
        selectedOrderId = Long.valueOf(orderTableModel.getValueAt(row, 0).toString());
        txtOrderId.setText(selectedOrderId.toString());
        lblTongTien.setText(orderTableModel.getValueAt(row, 3).toString() + " VND");
    }
}