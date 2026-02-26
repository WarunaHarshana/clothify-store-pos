package com.clothify.controller;

import com.clothify.model.Category;
import com.clothify.service.CategoryService;
import com.clothify.service.impl.CategoryServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryFormController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtSearch;

    @FXML private TableView<Category> tblCategories;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private TableColumn<Category, String> colDescription;

    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadTable();

        tblCategories.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, category) -> {
            if (category != null) {
                txtName.setText(category.getCategoryName());
                txtDescription.setText(category.getDescription());
            }
        });
    }

    @FXML
    void btnAddOnAction() {
        try {
            String name = txtName.getText() == null ? "" : txtName.getText().trim();
            if (name.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Category name is required").show();
                return;
            }

            Category category = new Category(0, name, txtDescription.getText(), true);
            if (categoryService.addCategory(category)) {
                new Alert(Alert.AlertType.INFORMATION, "Category added").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add category").show();
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            Category selected = tblCategories.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a category first").show();
                return;
            }

            String name = txtName.getText() == null ? "" : txtName.getText().trim();
            if (name.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Category name is required").show();
                return;
            }

            Category category = new Category(selected.getCategoryId(), name, txtDescription.getText(), true);
            if (categoryService.updateCategory(category)) {
                new Alert(Alert.AlertType.INFORMATION, "Category updated").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update category").show();
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            Category selected = tblCategories.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a category first").show();
                return;
            }

            if (categoryService.deleteCategory(selected.getCategoryId())) {
                new Alert(Alert.AlertType.INFORMATION, "Category deleted").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete category").show();
        }
    }

    @FXML
    void btnSearchOnAction() {
        try {
            tblCategories.setItems(FXCollections.observableArrayList(
                    categoryService.searchCategories(txtSearch.getText())
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
            tblCategories.setItems(FXCollections.observableArrayList(categoryService.getAllCategories()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load categories").show();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        txtSearch.clear();
        tblCategories.getSelectionModel().clearSelection();
    }
}
