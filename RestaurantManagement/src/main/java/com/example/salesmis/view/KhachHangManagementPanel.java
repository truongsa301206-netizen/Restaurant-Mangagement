package com.example.salesmis.view;




import com.example.salesmis.controller.KhachHangController;
import com.example.salesmis.model.entity.KhachHang;

import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
import java.awt.*;
        import java.util.List;

public class KhachHangManagementPanel extends JPanel {

    private final KhachHangController controller;

    private JTextField txtId, txtTen, txtSdt, txtTimKiem;
    private JComboBox<String> cboLoai;
    private JTable tblKhachHang;
    private DefaultTableModel tableModel;

    public KhachHangManagementPanel(KhachHangController controller) {
        this.controller = controller;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Form nhập liệu
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 8, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtTen = new JTextField();
        txtSdt = new JTextField();
        cboLoai = new JComboBox<>(new String[]{"NORMAL", "VIP"});

        pnlForm.add(new JLabel("ID:")); pnlForm.add(txtId);
        pnlForm.add(new JLabel("Tên khách hàng:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("SĐT:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Loại:")); pnlForm.add(cboLoai);

        // Nút bấm
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        pnlButtons.add(btnAdd); pnlButtons.add(btnUpdate); pnlButtons.add(btnDelete); pnlButtons.add(btnClear);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);

        // Search panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTimKiem = new JTextField(15);
        JButton btnSearch = new JButton("Tìm");
        pnlSearch.add(new JLabel("Tìm kiếm:")); pnlSearch.add(txtTimKiem); pnlSearch.add(btnSearch);

        // Bảng dữ liệu
        String[] columns = {"ID", "Tên", "SĐT", "Số lần mua", "Tổng tiền", "Loại"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKhachHang = new JTable(tableModel);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);
        pnlCenter.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> saveKhachHang(false));
        btnUpdate.addActionListener(e -> saveKhachHang(true));
        btnDelete.addActionListener(e -> deleteKhachHang());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> loadDataByKeyword());

        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblKhachHang.getSelectedRow() != -1) {
                fillForm();
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<KhachHang> list = controller.getAllKhachHang();
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{kh.getId(), kh.getTen(), kh.getSdt(), kh.getTongLan(), kh.getTongTien(), kh.getLoai()});
        }
    }

    private void loadDataByKeyword() {
        tableModel.setRowCount(0);
        List<KhachHang> list = controller.searchKhachHang(txtTimKiem.getText());
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{kh.getId(), kh.getTen(), kh.getSdt(), kh.getTongLan(), kh.getTongTien(), kh.getLoai()});
        }
    }

    private void saveKhachHang(boolean isUpdate) {
        try {
            if (isUpdate) {
                Long id = Long.valueOf(txtId.getText());
                controller.updateKhachHang(id, txtTen.getText(), txtSdt.getText(), cboLoai.getSelectedItem().toString());
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                controller.createKhachHang(txtTen.getText(), txtSdt.getText(), cboLoai.getSelectedItem().toString());
                JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
            }
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteKhachHang() {
        if (txtId.getText().isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteKhachHang(Long.valueOf(txtId.getText()));
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillForm() {
        int row = tblKhachHang.getSelectedRow();
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtTen.setText(tableModel.getValueAt(row, 1).toString());
        txtSdt.setText(tableModel.getValueAt(row, 2).toString());
        cboLoai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        txtId.setText(""); txtTen.setText(""); txtSdt.setText(""); tblKhachHang.clearSelection();
    }
}