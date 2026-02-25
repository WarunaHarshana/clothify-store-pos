package com.clothify.controller;

import com.clothify.model.Supplier;
import com.clothify.service.custom.SupplierService;
import com.clothify.service.custom.impl.SupplierServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtContact;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAddress;
    @FXML private TextField txtSearch;

    @FXML private TableView<Supplier> tblSuppliers;
    @FXML private TableColumn<Supplier, Integer> colId;
    @FXML private TableColumn<Supplier, String> colName;
    @FXML private TableColumn<Supplier, String> colContact;
    @FXML private TableColumn<Supplier, String> colPhone;
    @FXML private TableColumn<Supplier, String> colEmail;
    @FXML private TableColumn<Supplier, String> colAddress;

    private final SupplierService supplierService = new SupplierServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadTable();

        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, supplier) -> {
            if (supplier != null) {
                txtName.setText(supplier.getName());
                txtContact.setText(supplier.getContactPerson());
                txtPhone.setText(supplier.getPhone());
                txtEmail.setText(supplier.getEmail());
                txtAddress.setText(supplier.getAddress());
            }
        });
    }

    @FXML
    void btnAddOnAction() {
        try {
            Supplier supplier = new Supplier();
            supplier.setName(txtName.getText().trim());
            supplier.setContactPerson(txtContact.getText().trim());
            supplier.setPhone(txtPhone.getText().trim());
            supplier.setEmail(txtEmail.getText().trim());
            supplier.setAddress(txtAddress.getText().trim());
            supplier.setActive(true);

            if (supplier.getName().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Supplier name is required").show();
                return;
            }

            if (supplierService.addSupplier(supplier)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier added").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add supplier").show();
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            Supplier selected = tblSuppliers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a supplier first").show();
                return;
            }

            Supplier supplier = new Supplier(
                    selected.getSupplierId(),
                    txtName.getText().trim(),
                    txtContact.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim(),
                    true
            );

            if (supplierService.updateSupplier(supplier)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update supplier").show();
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            Supplier selected = tblSuppliers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a supplier first").show();
                return;
            }

            if (supplierService.deleteSupplier(selected.getSupplierId())) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier deleted").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete supplier").show();
        }
    }

    @FXML
    void btnSearchOnAction() {
        try {
            tblSuppliers.setItems(FXCollections.observableArrayList(
                    supplierService.searchSuppliers(txtSearch.getText())
            ));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Search failed").show();
        }
    }

    @FXML
    void btnReloadOnAction() {
        loadTable();
    }

    private void loadTable() {
        try {
            tblSuppliers.setItems(FXCollections.observableArrayList(supplierService.getAllSuppliers()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load suppliers").show();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtContact.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtSearch.clear();
        tblSuppliers.getSelectionModel().clearSelection();
    }
}

