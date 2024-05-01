package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.CartMapper;
import com.nvm.shoestoreapi.dto.mapper.OrderMapper;
import com.nvm.shoestoreapi.dto.mapper.ReceiptMapper;
import com.nvm.shoestoreapi.dto.response.*;
import com.nvm.shoestoreapi.repository.*;
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
import java.util.ArrayList;
import java.util.Calendar;
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
//        try {
//            return exportService.createOutputFile(writeExcelOrder(productRepository.findAll()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return null;
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

    private Workbook writeExcelProduct(List<OrderReport> orderReports) throws IOException {
        // Create Workbook
        Workbook workbook = exportService.getWorkbookTemplate("reports/BaoCaoHangTon.xlsx");
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

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(orderReport.getTotalMoneyOrder());
            cell5.setCellStyle(exportService.getCellStyleDataRight(workbook));


            sheetIndex++;
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

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(orderReport.getTotalMoneyOrder());
            cell5.setCellStyle(exportService.getCellStyleDataRight(workbook));


            sheetIndex++;
        }
        return workbook;
    }

//    private Workbook writeExcelOrder(List<OrderReport> orderReports) throws IOException {
//        // Create Workbook
//        Workbook workbook = exportService.getWorkbookTemplate("reports/BaoCaoBanHang.xlsx");
//
//        // Sử dụng một sheet duy nhất
//        Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
//
//        // Khởi tạo rowIndex để quản lý vị trí bắt đầu của mỗi báo cáo tháng
//        int rowIndex = 2;
//
//        for (OrderReport orderReport : orderReports) {
//            Row titleRow = sheet.createRow(rowIndex); // Tạo tiêu đề cho mỗi tháng
//            Cell titleCell = titleRow.createCell(0);
//            titleCell.setCellValue("Báo cáo tháng " + orderReport.getMonth().toString());
//            rowIndex++;
//
//            int index = 1;
//            for (ProductReport item : orderReport.getProducts()) {
//                Row row = sheet.createRow(rowIndex);
//                writeBookProductReport(index, item, row, workbook);
//                index++;
//                rowIndex++;
//            }
//
//            // Tổng doanh thu cho tháng
//            Row row = sheet.createRow(rowIndex);
//            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 4));
//            Cell cellTotalLabel = row.createCell(0);
//            cellTotalLabel.setCellValue("Tổng doanh thu:");
//            cellTotalLabel.setCellStyle(exportService.getCellStyleDataRight(workbook));
//
//            Cell cellTotalValue = row.createCell(5);
//            cellTotalValue.setCellValue(orderReport.getTotalMoneyOrder());
//            cellTotalValue.setCellStyle(exportService.getCellStyleDataRight(workbook));
//
//            rowIndex += 2; // Thêm một khoảng trống giữa các báo cáo của các tháng
//        }
//        return workbook;
//    }

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

            Cell cell5 = row.createCell(5);
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
        cell.setCellValue(productReport.getTotalQuantity());
        cell.setCellStyle(exportService.getCellStyleDataCenter(workbook));

        cell = row.createCell(5);
        cell.setCellValue(productReport.getTotalMoney());
        cell.setCellStyle(exportService.getCellStyleDataRight(workbook));
    }
}
