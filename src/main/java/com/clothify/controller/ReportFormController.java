package com.clothify.controller;

import com.clothify.db.DbConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportFormController implements Initializable {

    @FXML
    private DatePicker dpFrom;
    @FXML
    private DatePicker dpTo;
    @FXML
    private ComboBox<String> cmbReportType;
    @FXML
    private TextArea txtReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbReportType.setItems(FXCollections.observableArrayList(
                "Sales Summary", "Inventory Summary"
        ));
        cmbReportType.setValue("Sales Summary");
        dpFrom.setValue(LocalDate.now().minusDays(30));
        dpTo.setValue(LocalDate.now());
    }

    @FXML
    void btnGenerateOnAction() {
        String type = cmbReportType.getValue();
        LocalDate from = dpFrom.getValue();
        LocalDate to = dpTo.getValue();

        if (type == null) {
            new Alert(Alert.AlertType.WARNING, "Select a report type").show();
            return;
        }
        if (from == null || to == null || from.isAfter(to)) {
            new Alert(Alert.AlertType.WARNING, "Please select a valid date range").show();
            return;
        }

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            StringBuilder sb = new StringBuilder();
            String template;
            String reportTitle;

            if ("Sales Summary".equals(type)) {
                generateSalesReport(conn, sb);
                template = "sales_report";
                reportTitle = "Sales Summary";
            } else {
                generateInventoryReport(conn, sb);
                template = "inventory_report";
                reportTitle = "Inventory Summary";
            }

            txtReport.setText(sb.toString());
            File pdf = generateJasperPdf(conn, template, reportTitle, from, to);
            new Alert(Alert.AlertType.INFORMATION,
                    "Report generated successfully.\nPDF: " + pdf.getAbsolutePath()).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Report generation failed: " + e.getMessage()).show();
        }
    }

    private File generateJasperPdf(Connection conn, String templateName, String reportTitle,
                                   LocalDate from, LocalDate to) throws Exception {
        InputStream templateStream = getClass().getResourceAsStream("/reports/" + templateName + ".jrxml");
        if (templateStream == null) {
            throw new RuntimeException("Report template not found: /reports/" + templateName + ".jrxml");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("FROM_DATE", java.sql.Date.valueOf(from));
        params.put("TO_DATE", java.sql.Date.valueOf(to));
        params.put("REPORT_TITLE", reportTitle);
        params.put("STORE_NAME", "Clothify Store POS");

        JasperDesign design = JRXmlLoader.load(templateStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(design);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);

        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String datePart = from.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" +
                to.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File out = new File(reportsDir, templateName + "_" + datePart + ".pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint, out.getAbsolutePath());
        return out;
    }

    private void generateSalesReport(Connection conn, StringBuilder sb) throws Exception {
        String from = dpFrom.getValue().toString();
        String to = dpTo.getValue().toString();

        sb.append("==============================================\n");
        sb.append("          CLOTHIFY STORE - SALES REPORT\n");
        sb.append("==============================================\n");
        sb.append("Period: ").append(from).append(" to ").append(to).append("\n\n");

        String summSql = "SELECT COUNT(*) as cnt, COALESCE(SUM(total_amount),0) as total " +
                "FROM orders WHERE DATE(order_date) BETWEEN ? AND ? AND status='COMPLETED'";
        try (PreparedStatement pstm = conn.prepareStatement(summSql)) {
            pstm.setString(1, from);
            pstm.setString(2, to);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    sb.append("Total Orders  : ").append(rs.getInt("cnt")).append("\n");
                    sb.append("Total Revenue : Rs. ").append(String.format("%,.2f", rs.getDouble("total"))).append("\n\n");
                }
            }
        }

        sb.append("----------------------------------------------\n");
        sb.append(String.format("%-14s %-10s %s\n", "Date", "Orders", "Revenue"));
        sb.append("----------------------------------------------\n");

        String dailySql = "SELECT DATE(order_date) as d, COUNT(*) as cnt, SUM(total_amount) as total " +
                "FROM orders WHERE DATE(order_date) BETWEEN ? AND ? AND status='COMPLETED' " +
                "GROUP BY DATE(order_date) ORDER BY d DESC";
        try (PreparedStatement pstm = conn.prepareStatement(dailySql)) {
            pstm.setString(1, from);
            pstm.setString(2, to);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    sb.append(String.format("%-14s %-10d Rs. %,.2f\n",
                            rs.getString("d"), rs.getInt("cnt"), rs.getDouble("total")));
                }
            }
        }

        sb.append("\n----------------------------------------------\n");
        sb.append("Top Selling Products\n");
        sb.append("----------------------------------------------\n");

        String topSql = "SELECT od.product_name, SUM(od.quantity) as qty, SUM(od.line_total) as revenue " +
                "FROM order_details od JOIN orders o ON od.order_id = o.order_id " +
                "WHERE DATE(o.order_date) BETWEEN ? AND ? AND o.status='COMPLETED' " +
                "GROUP BY od.product_name ORDER BY qty DESC LIMIT 10";
        try (PreparedStatement pstm = conn.prepareStatement(topSql)) {
            pstm.setString(1, from);
            pstm.setString(2, to);
            try (ResultSet rs = pstm.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    sb.append(String.format("%d. %-30s Qty: %-6d Rs. %,.2f\n",
                            rank++, rs.getString("product_name"),
                            rs.getInt("qty"), rs.getDouble("revenue")));
                }
            }
        }

        sb.append("\n==============================================\n");
    }

    private void generateInventoryReport(Connection conn, StringBuilder sb) throws Exception {
        sb.append("==============================================\n");
        sb.append("       CLOTHIFY STORE - INVENTORY REPORT\n");
        sb.append("==============================================\n");
        sb.append("Generated: ").append(LocalDate.now()).append("\n\n");

        String summSql = "SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN quantity < 10 THEN 1 ELSE 0 END) as low, " +
                "SUM(CASE WHEN quantity = 0 THEN 1 ELSE 0 END) as out_of " +
                "FROM products WHERE is_active = TRUE";
        try (PreparedStatement pstm = conn.prepareStatement(summSql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                sb.append("Total Products  : ").append(rs.getInt("total")).append("\n");
                sb.append("Low Stock (<10) : ").append(rs.getInt("low")).append("\n");
                sb.append("Out of Stock    : ").append(rs.getInt("out_of")).append("\n\n");
            }
        }

        sb.append("----------------------------------------------\n");
        sb.append(String.format("%-8s %-30s %-12s %-8s %s\n", "Code", "Name", "Price", "Qty", "Status"));
        sb.append("----------------------------------------------\n");

        String sql = "SELECT * FROM products WHERE is_active = TRUE ORDER BY quantity ASC";
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                int qty = rs.getInt("quantity");
                String status = qty == 0 ? "OUT" : qty < 10 ? "LOW" : "OK";
                sb.append(String.format("%-8s %-30s Rs. %-8.2f %-8d %s\n",
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getDouble("unit_price"),
                        qty, status));
            }
        }

        sb.append("\n==============================================\n");
    }
}
