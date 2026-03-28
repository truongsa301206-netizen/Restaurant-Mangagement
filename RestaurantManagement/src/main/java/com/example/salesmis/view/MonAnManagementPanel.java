package com.example.salesmis.view;

import com.example.salesmis.controller.MonAnController;
import com.example.salesmis.model.entity.MonAn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MonAnManagementPanel extends JPanel {

    private final MonAnController controller;

    private JTextField txtId, txtTen, txtGia, txtTimKiem;
    private JCheckBox chkActive;
    private JTable tblMonAn;
    private DefaultTableModel tableModel;

    public MonAnManagementPanel(MonAnController controller) {
        this.controller = controller;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 8, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin món ăn"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtTen = new JTextField();
        txtGia = new JTextField();
        chkActive = new JCheckBox("Còn phục vụ", true);

        pnlForm.add(new JLabel("ID:")); pnlForm.add(txtId);
        pnlForm.add(new JLabel("Tên món:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Đơn giá:")); pnlForm.add(txtGia);
        pnlForm.add(new JLabel("Trạng thái:")); pnlForm.add(chkActive);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        pnlButtons.add(btnAdd); pnlButtons.add(btnUpdate); pnlButtons.add(btnDelete); pnlButtons.add(btnClear);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTimKiem = new JTextField(15);
        JButton btnSearch = new JButton("Tìm");
        pnlSearch.add(new JLabel("Tìm món:")); pnlSearch.add(txtTimKiem); pnlSearch.add(btnSearch);

        String[] columns = {"ID", "Tên Món", "Giá", "Còn Phục Vụ"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMonAn = new JTable(tableModel);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);
        pnlCenter.add(new JScrollPane(tblMonAn), BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> saveMonAn(false));
        btnUpdate.addActionListener(e -> saveMonAn(true));
        btnDelete.addActionListener(e -> deleteMonAn());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> loadDataByKeyword());

        tblMonAn.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblMonAn.getSelectedRow() != -1) fillForm();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<MonAn> list = controller.getAllMonAn();
        for (MonAn m : list) {
            tableModel.addRow(new Object[]{m.getId(), m.getTenMon(), m.getGia(), m.getTrangThai() ? "Bật" : "Tắt"});
        }
    }

    private void loadDataByKeyword() {
        tableModel.setRowCount(0);
        List<MonAn> list = controller.searchMonAn(txtTimKiem.getText());
        for (MonAn m : list) {
            tableModel.addRow(new Object[]{m.getId(), m.getTenMon(), m.getGia(), m.getTrangThai() ? "Bật" : "Tắt"});
        }
    }

    private void saveMonAn(boolean isUpdate) {
        try {
            if (isUpdate) {
                Long id = Long.valueOf(txtId.getText());
                controller.updateMonAn(id, txtTen.getText(), txtGia.getText(), chkActive.isSelected());
                JOptionPane.showMessageDialog(this, "Cập nhật món thành công!");
            } else {
                controller.createMonAn(txtTen.getText(), txtGia.getText(), chkActive.isSelected());
                JOptionPane.showMessageDialog(this, "Thêm món thành công!");
            }
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMonAn() {
        if (txtId.getText().isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteMonAn(Long.valueOf(txtId.getText()));
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillForm() {
        int row = tblMonAn.getSelectedRow();
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtTen.setText(tableModel.getValueAt(row, 1).toString());
        txtGia.setText(tableModel.getValueAt(row, 2).toString());
        chkActive.setSelected(tableModel.getValueAt(row, 3).toString().equals("Bật"));
    }

    private void clearForm() {
        txtId.setText(""); txtTen.setText(""); txtGia.setText(""); chkActive.setSelected(true); tblMonAn.clearSelection();
    }
}