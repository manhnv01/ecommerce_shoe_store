package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.CartMapper;
import com.nvm.shoestoreapi.dto.mapper.OrderMapper;
import com.nvm.shoestoreapi.dto.mapper.ReceiptMapper;
import com.nvm.shoestoreapi.dto.response.*;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.ProductService;
import com.nvm.shoestoreapi.service.ReportService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    final OrderRepository orderRepository;
    final ReceiptRepository receiptRepository;
    final ReceiptMapper receiptMapper;
    final OrderMapper orderMapper;
    final CartMapper cartMapper;
    final ExportService exportService;
    final ProductService productService;
    final ProductDetailsRepository productDetailsRepository;
    final CartDetailsRepository cartDetailsRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderReport> getOrderReport(Integer year) {
        Calendar from = Calendar.getInstance();
        from.set(year, Calendar.JANUARY, 1);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        Calendar to = Calendar.getInstance();
        to.set(year, Calendar.DECEMBER, 31);
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        List<OrderResponse> orderResponses = orderRepository
                .findAllByCompletedDateBetweenAndOrderStatus(from.getTime(), to.getTime(), 3)
                .stream().map(orderMapper::convertToResponse).collect(Collectors.toList());
        return getOrderReportByMonthInYear(orderResponses, year);
    }

    @Override
    public List<ReceiptReport> getReceiptReport(Integer year) {
        Calendar from = Calendar.getInstance();
        from.set(year, Calendar.JANUARY, 1);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        Calendar to = Calendar.getInstance();
        to.set(year, Calendar.DECEMBER, 31);
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        List<ReceiptResponse> receiptResponses = receiptRepository
                .findAllByCreatedAtBetween(from.getTime(), to.getTime())
                .stream().map(receiptMapper::convertToResponse).collect(Collectors.toList());
        return getReceiptReportByMonthInYear(receiptResponses, year);
    }

    @Override
    public byte[] exportOrderReport(Integer year) {
        try {
            return exportService.createOutputFile(writeExcelOrder(getOrderReport(year)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] exportReceiptReport(Integer year) {
        try {
            return exportService.createOutputFile(writeExcelReceipt(getReceiptReport(year)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] exportProductReport() {
        try {
            return exportService.createOutputFile(writeExcelProduct(productRepository.findAllInventory()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ReceiptReport> getReceiptReportByMonthInYear(List<ReceiptResponse> receiptResponses, Integer year) {
        List<ReceiptReport> receiptReports = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            List<ProductReport> productReports = new ArrayList<>();
            ReceiptReport receiptReport = new ReceiptReport();
            receiptReport.setMonth(i + 1);
            receiptReport.setYear(year);
            int finalI = i;
            List<ReceiptResponse> receiptResponsesByMonth = receiptResponses
                    .stream()
                    .filter(receiptResponse -> receiptResponse.getCreatedAt().getMonth() == finalI)
                    .collect(Collectors.toList());
            receiptReport.setTotalQuantityReceipt(receiptResponsesByMonth.stream().mapToInt(receiptResponse -> receiptResponse.getReceiptDetails().stream().mapToInt(ReceiptDetailsResponse::getQuantity).sum()).sum());
            receiptReport.setTotalMoneyReceipt(receiptResponsesByMonth.stream().mapToDouble(receiptResponse -> receiptResponse.getReceiptDetails().stream().mapToDouble(receiptDetailsResponse -> receiptDetailsResponse.getQuantity() * receiptDetailsResponse.getPrice()).sum()).sum());
            for (ReceiptResponse receiptResponse : receiptResponsesByMonth) {
                for (ReceiptDetailsResponse receiptDetailsResponse : receiptResponse.getReceiptDetails()) {
                    ProductReport productReport = new ProductReport();
                    productReport.setId(receiptDetailsResponse.getProductId());
                    productReport.setName(receiptDetailsResponse.getProductName());
                    productReport.setColor(receiptDetailsResponse.getProductColor());
                    productReport.setSize(receiptDetailsResponse.getProductSize());
                    productReport.setTotalQuantity(receiptDetailsResponse.getQuantity());
                    productReport.setTotalMoney(receiptDetailsResponse.getQuantity() * receiptDetailsResponse.getPrice());
                    productReports.add(productReport);
                }
            }
            // Sum total quantity of each product
            productReports = new ArrayList<>(productReports.stream().collect(Collectors.toMap(ProductReport::getId, productReport -> productReport, (productReport, productReport2) -> {
                productReport.setTotalQuantity(productReport.getTotalQuantity() + productReport2.getTotalQuantity());
                productReport.setTotalMoney(productReport.getTotalMoney() + productReport2.getTotalMoney());
                return productReport;
            })).values());
            receiptReport.setProducts(productReports);
            receiptReports.add(receiptReport);
        }
        return receiptReports;
    }

    private List<OrderReport> getOrderReportByMonthInYear(List<OrderResponse> orderResponses, Integer year) {
        List<OrderReport> orderReports = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            List<ProductReport> productReports = new ArrayList<>();
            OrderReport orderReport = new OrderReport();
            orderReport.setMonth(i + 1);
            orderReport.setYear(year);
            int finalI = i;
            List<OrderResponse> orderResponsesByMonth = orderResponses
                    .stream()
                    .filter(saleResponse -> saleResponse.getCompletedDate().getMonth() == finalI)
                    .collect(Collectors.toList());
            orderReport.setTotalQuantityOrder(orderResponsesByMonth.stream().mapToInt(saleResponse -> saleResponse.getOrderDetails().stream().mapToInt(OrderDetailsResponse::getQuantity).sum()).sum());

            // kiểm tra vs salePrice
            orderReport.setTotalMoneyOrder(orderResponsesByMonth.stream().mapToDouble(saleResponse -> saleResponse.getOrderDetails().stream().mapToDouble(orderDetailsResponse -> orderDetailsResponse.getQuantity() * orderDetailsResponse.getProductPrice()).sum()).sum());
            for (OrderResponse receiptResponse : orderResponsesByMonth) {
                for (OrderDetailsResponse orderDetailsResponse : receiptResponse.getOrderDetails()) {
                    ProductReport productReport = new ProductReport();
                    productReport.setId(orderDetailsResponse.getProductDetailsId());
                    productReport.setName(orderDetailsResponse.getProductName());
                    productReport.setColor(orderDetailsResponse.getProductColor());
                    productReport.setSize(orderDetailsResponse.getProductSize());
                    productReport.setTotalQuantity(orderDetailsResponse.getQuantity());
                    ///// kiểm tra salePrice
                    productReport.setTotalMoney(orderDetailsResponse.getQuantity() * orderDetailsResponse.getProductPrice());
                    productReports.add(productReport);
                }
            }
            // Sum total quantity of each product
            productReports = new ArrayList<>(productReports.stream().collect(Collectors.toMap(ProductReport::getId, productReport -> productReport, (productReport, productReport2) -> {
                productReport.setTotalQuantity(productReport.getTotalQuantity() + productReport2.getTotalQuantity());
                productReport.setTotalMoney(productReport.getTotalMoney() + productReport2.getTotalMoney());
                return productReport;
            })).values());
            orderReport.setProducts(productReports);
            orderReports.add(orderReport);
        }
        return orderReports;
    }

    private Workbook writeExcelProduct(List<InventoryReport> inventoryReportList) throws IOException {
        // Create Workbook
        LocalDateTime now = LocalDateTime.now();
        // format
        String time = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Workbook workbook = exportService.getWorkbookTemplate("reports/BaoCaoHangTon.xlsx");
        int sheetIndex = 0;
        int rowIndex = 2;
        int index = 1;
        for (InventoryReport inventoryReport : inventoryReportList) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            Row titleRow = sheet.getRow(0);
            Cell cell = titleRow.getCell(0);
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("[time]")) {
                cellValue = cellValue.replace("[time]", time);
            }
            cell.setCellValue(cellValue);

            Row row = sheet.createRow(rowIndex);
            writeBookInventoryReport(index, inventoryReport, row, workbook);

            index++;
            rowIndex++;
        }
        return workbook;
    }

    private Workbook writeExcelOrder(List<OrderReport> orderReports) throws IOException {
        // Create Workbook
        Workbook workbook = exportService.getWorkbookTemplate("reports/BaoCaoBanHang.xlsx");
        int sheetIndex = 0;
        for (OrderReport orderReport : orderReports) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            Row titleRow = sheet.getRow(0);
            Cell cell = titleRow.getCell(0);
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("[month]")) {
                cellValue = cellValue.replace("[month]", orderReport.getMonth().toString());
            }
            cell.setCellValue(cellValue);

            int rowIndex = 2;
            int index = 1;
            for (ProductReport item : orderReport.getProducts()) {
                Row row = sheet.createRow(rowIndex);
                writeBookProductReport(index, item, row, workbook);
                index++;
                rowIndex++;
            }
            Row row = sheet.createRow(rowIndex);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 4));
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("Tổng doanh thu:");
            cell1.setCellStyle(exportService.getCellStyleDataRight(workbook));

            Cell cell5 = row.createCell(6);
            cell5.setCellValue(orderReport.getTotalMoneyOrder());
            cell5.setCellStyle(exportService.getCellStyleDataRight(workbook));


            sheetIndex++;
        }
        return workbook;
    }

    private Workbook writeExcelReceipt(List<ReceiptReport> receiptReports) throws IOException {
        // Create Workbook
        Workbook workbook = exportService.getWorkbookTemplate("reports/BaoCaoNhapHang.xlsx");
        int sheetIndex = 0;
        for (ReceiptReport receiptReport : receiptReports) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            Row titleRow = sheet.getRow(0);
            Cell cell = titleRow.getCell(0);
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("[month]")) {
                cellValue = cellValue.replace("[month]", receiptReport.getMonth().toString());
            }
            cell.setCellValue(cellValue);

            int rowIndex = 2;
            int index = 1;
            for (ProductReport item : receiptReport.getProducts()) {
                Row row = sheet.createRow(rowIndex);
                writeBookProductReport(index, item, row, workbook);
                index++;
                rowIndex++;
            }
            Row row = sheet.createRow(rowIndex);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 4));
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("Tổng tiền nhập:");
            cell1.setCellStyle(exportService.getCellStyleDataRight(workbook));

            Cell cell5 = row.createCell(6);
            cell5.setCellValue(receiptReport.getTotalMoneyReceipt());
            cell5.setCellStyle(exportService.getCellStyleDataRight(workbook));

            sheetIndex++;
        }
        return workbook;
    }

    private void writeBookProductReport(int index, ProductReport productReport, Row row, Workbook workbook) {
        Cell cell = row.createCell(0);
        cell.setCellValue(index);
        cell.setCellStyle(exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(1);
        cell.setCellValue(productReport.getId());
        cell.setCellStyle(exportService.getCellStyleDataLeft(workbook));

        cell = row.createCell(2);
        cell.setCellValue(productReport.getName());
        cell.setCellStyle(exportService.getCellStyleDataLeft(workbook));

        cell = row.createCell(3);
        cell.setCellValue(productReport.getColor());
        cell.setCellStyle(exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(4);
        cell.setCellValue(productReport.getSize());
        cell.setCellStyle(exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(5);
        cell.setCellValue(productReport.getTotalQuantity());
        cell.setCellStyle(exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(6);
        cell.setCellValue(productReport.getTotalMoney());
        cell.setCellStyle(exportService.getCellStyleDataRight(workbook));
    }

    private void writeBookInventoryReport(int index, InventoryReport inventoryReport, Row row, Workbook workbook) {
        boolean isZero = inventoryReport.getQuantity() == 0;

        Cell cell = row.createCell(0);
        cell.setCellValue(index);
        cell.setCellStyle(isZero ? exportService.getCellStyleDataCenterRed(workbook) : exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(1);
        cell.setCellValue(inventoryReport.getId());
        cell.setCellStyle(isZero ? exportService.getCellStyleDataCenterRed(workbook) : exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(2);
        cell.setCellValue(inventoryReport.getName());
        cell.setCellStyle(isZero ? exportService.getCellStyleDataLeftRed(workbook) : exportService.getCellStyleDataLeft(workbook));

        cell = row.createCell(3);
        cell.setCellValue(inventoryReport.getColor());
        cell.setCellStyle(isZero ? exportService.getCellStyleDataCenterRed(workbook) : exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(4);
        cell.setCellValue(inventoryReport.getSize());
        cell.setCellStyle(isZero ? exportService.getCellStyleDataCenterRed(workbook) : exportService.getCellStyleDataCenter(workbook));

        // nếu quantity = 0 thì hiển thị màu đỏ
        cell = row.createCell(5);
        cell.setCellValue(inventoryReport.getQuantity());
        cell.setCellStyle(isZero ? exportService.getCellStyleDataCenterRed(workbook) : exportService.getCellStyleDataCenter(workbook));
    }
}
