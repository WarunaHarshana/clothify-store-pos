package com.clothify.controller;

import com.clothify.model.Employee;
import com.clothify.service.EmployeeService;
import com.clothify.service.impl.EmployeeServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cmbRole;
    @FXML private DatePicker dpHireDate;
    @FXML private TextField txtSearch;

    @FXML private TableView<Employee> tblEmployees;
    @FXML private TableColumn<Employee, Integer> colId;
    @FXML private TableColumn<Employee, String> colName;
    @FXML private TableColumn<Employee, String> colPhone;
    @FXML private TableColumn<Employee, String> colEmail;
    @FXML private TableColumn<Employee, String> colRole;
    @FXML private TableColumn<Employee, String> colHireDate;

    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "STAFF"));
        cmbRole.setValue("STAFF");
        dpHireDate.setValue(LocalDate.now());

        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        loadTable();

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, emp) -> {
            if (emp != null) {
                txtName.setText(emp.getFullName());
                txtPhone.setText(emp.getPhone());
                txtEmail.setText(emp.getEmail());
                cmbRole.setValue(emp.getRole());
                try {
                    dpHireDate.setValue(LocalDate.parse(emp.getHireDate()));
                } catch (Exception e) {
                    dpHireDate.setValue(LocalDate.now());
                }
            }
        });
    }

    @FXML
    void btnAddOnAction() {
        try {
            if (txtName.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Employee name is required").show();
                return;
            }

            Employee emp = new Employee();
            emp.setFullName(txtName.getText().trim());
            emp.setPhone(txtPhone.getText().trim());
            emp.setEmail(txtEmail.getText().trim());
            emp.setRole(cmbRole.getValue());
            emp.setHireDate(dpHireDate.getValue().toString());
            emp.setActive(true);

            if (employeeService.addEmployee(emp)) {
                new Alert(Alert.AlertType.INFORMATION, "Employee added").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add employee").show();
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            Employee selected = tblEmployees.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select an employee first").show();
                return;
            }

            Employee emp = new Employee(
                    selected.getEmployeeId(),
                    txtName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    cmbRole.getValue(),
                    dpHireDate.getValue().toString(),
                    true
            );

            if (employeeService.updateEmployee(emp)) {
                new Alert(Alert.AlertType.INFORMATION, "Employee updated").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update employee").show();
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            Employee selected = tblEmployees.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select an employee first").show();
                return;
            }

            if (employeeService.deleteEmployee(selected.getEmployeeId())) {
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete employee").show();
        }
    }

    @FXML
    void btnSearchOnAction() {
        try {
            tblEmployees.setItems(FXCollections.observableArrayList(
                    employeeService.searchEmployees(txtSearch.getText())
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
            tblEmployees.setItems(FXCollections.observableArrayList(employeeService.getAllEmployees()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load employees").show();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtPhone.clear();
        txtEmail.clear();
        cmbRole.setValue("STAFF");
        dpHireDate.setValue(LocalDate.now());
        txtSearch.clear();
        tblEmployees.getSelectionModel().clearSelection();
    }
}

