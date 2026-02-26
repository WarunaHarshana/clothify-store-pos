package com.clothify.util;

import com.clothify.db.DbConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public final class InvoiceUtil {

    private InvoiceUtil() {
    }

    public static File generateInvoicePdf(int orderId) throws Exception {
        Connection conn = DbConnection.getInstance().getConnection();

        InputStream templateStream = InvoiceUtil.class.getResourceAsStream("/reports/invoice_report.jrxml");
        if (templateStream == null) {
            throw new RuntimeException("Invoice template not found: /reports/invoice_report.jrxml");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", orderId);
        params.put("STORE_NAME", "Clothify Store POS");

        JasperDesign design = JRXmlLoader.load(templateStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(design);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);

        File reportsDir = new File("reports");
        if (!reportsDir.exists()) reportsDir.mkdirs();

        File out = new File(reportsDir, "invoice_" + orderId + ".pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint, out.getAbsolutePath());
        return out;
    }
}
