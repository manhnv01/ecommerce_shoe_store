package com.nvm.shoestoreapi.service.impl;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.nvm.shoestoreapi.dto.response.OrderDetailsResponse;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.service.OrderService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;
import static com.nvm.shoestoreapi.util.Constant.formatPrice;

@Service
public class ExportService {

    @Autowired
    private OrderService orderService;

    public byte[] generateInvoicePdf(Long id) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        OrderResponse order = orderService.getById(id);

        // Load font từ resources folder
        FontProgram fontProgram = FontProgramFactory.createFont(new ClassPathResource("fonts/DejaVuSans.ttf").getURL().getPath());
        PdfFont font = PdfFontFactory.createFont(fontProgram, "Identity-H");


        // Tiêu đề cửa hàng
        Paragraph storeInfo = new Paragraph("Shoes Station 97 Đặng Văn Ngữ")
                .setFont(font).setTextAlignment(TextAlignment.CENTER);
        storeInfo.setMarginBottom(8);

        document.add(storeInfo);

        // Thông tin hóa đơn
        Paragraph invoiceInfo = new Paragraph("HÓA ĐƠN BÁN HÀNG")
                .setFont(font).setTextAlignment(TextAlignment.CENTER);

        invoiceInfo.setFontSize(15);
        invoiceInfo.setBold();
        invoiceInfo.setMarginBottom(25);

        document.add(invoiceInfo);

        document.add(new LineSeparator(new DashedLine())); // Dấu gạch đứt

        // table có 2 cột 2 hàng cột 1 căn tri, cô 2 cn phải , table rộng toi da kich thuoc border no
        com.itextpdf.layout.element.Table table1 = new com.itextpdf.layout.element.Table(new float[]{1, 1});
        table1.setMarginTop(15);
        table1.useAllAvailableWidth();
        table1.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Số: #" + order.getId()).setFont(font).setFontSize(11).setTextAlignment(TextAlignment.LEFT)));
        table1.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Ngày: " + formatDate(order.getCreatedDate())).setFont(font).setFontSize(11).setTextAlignment(TextAlignment.RIGHT)));
        table1.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(" ").setFont(font).setFontSize(11).setTextAlignment(TextAlignment.LEFT)));
        table1.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Giờ: " + formatTimeInvoice(order.getCreatedDate())).setFont(font).setFontSize(11).setTextAlignment(TextAlignment.RIGHT)));
        table1.setMarginBottom(15);

        document.add(table1);

        document.add(new LineSeparator(new DashedLine())); // Dấu gạch đứt

        // Thông tin khách hàng
        // begin name khách hàng

        Paragraph customerNameText = new Paragraph()
                .setFont(font).setFontSize(11) // Set font chính
                .setTextAlignment(TextAlignment.LEFT);

        customerNameText.add(new Text("Khách hàng: "));
        Text nameBold = new Text(order.getCustomerName())
                .setBold().setFontSize(11); // Set font bold
        customerNameText.add(nameBold);
        customerNameText.setMarginTop(15);
        document.add(customerNameText);

        // end name khách hàng

        // begin phone khách hàng
        Paragraph customerPhoneText = new Paragraph()
                .setFont(font).setFontSize(11) // Set font chính
                .setTextAlignment(TextAlignment.LEFT);

        customerPhoneText.add(new Text("Điện thoại: "));
        Text phoneBold = new Text(order.getPhone())
                .setBold().setFontSize(11); // Set font bold
        customerPhoneText.add(phoneBold);
        document.add(customerPhoneText);

        // end phone khách hàng

        // begin address khách hàng

        Paragraph customerAddressText = new Paragraph()
                .setFont(font).setFontSize(11) // Set font chính
                .setTextAlignment(TextAlignment.LEFT);

        customerAddressText.add(new Text("Địa chỉ: " + order.getAddress())).setFontSize(11);
        customerAddressText.setMarginBottom(20);
        document.add(customerAddressText);

        // end address khách hàng

        // Bảng chi tiết sản phẩm
        com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(new float[]{1, 1, 1});
        table.useAllAvailableWidth();  // Sử dụng độ rộng tối đa của trang
        // Thêm hàng tiêu đề cho bảng
        addCellHeaderTable(table, "Sản phẩm", font, TextAlignment.LEFT);
        addCellHeaderTable(table, "Đơn giá", font, TextAlignment.CENTER);
        addCellHeaderTable(table, "Thành tiền", font, TextAlignment.RIGHT);

        for (OrderDetailsResponse orderDetails : order.getOrderDetails()) {
            // Thêm hàng dữ liệu
            Long price = Optional.ofNullable(orderDetails.getSalePrice()).orElse(orderDetails.getProductPrice());

            addCellProductNameTable(table, orderDetails.getProductName() + " - " + orderDetails.getProductColor() + " - " + orderDetails.getProductSize(), font);
            addCellToTable(table, orderDetails.getQuantity().toString(), font, TextAlignment.LEFT);
            addCellToTable(table, formatPrice(price), font, TextAlignment.CENTER);
            addCellToTable(table, formatPrice(orderDetails.getTotalPrice()), font, TextAlignment.RIGHT);
        }
        document.add(table);

        // Tổng kết hóa đơn
        com.itextpdf.layout.element.Table table2 = new Table(new float[]{1, 1});
        table2.setMarginTop(15);
        table2.useAllAvailableWidth();
        table2.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Tổng tiền hàng (" + order.getTotalQuantity() +" sản phẩm):").setFont(font).setFontSize(12).setTextAlignment(TextAlignment.LEFT)));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(formatPrice(order.getTotalMoney() - order.getTotalDiscount())).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.RIGHT)));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Phí vận chuyển: ").setFont(font).setFontSize(12).setTextAlignment(TextAlignment.LEFT)));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(formatPrice(order.getTotal_fee())).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.RIGHT)));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Thành tiền: ").setFont(font).setFontSize(12).setBold().setTextAlignment(TextAlignment.LEFT)));
        table2.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(formatPrice(order.getTotalMoney() - order.getTotalDiscount() + order.getTotal_fee())).setFont(font).setFontSize(12).setBold().setTextAlignment(TextAlignment.RIGHT)));
        document.add(table2);

        // Lời cảm ơn
        Paragraph thanks = new Paragraph()
                .setFont(font).setFontSize(11) // Set font chính
                .setTextAlignment(TextAlignment.LEFT);

        thanks.add(new Text("Cảm ơn bạn đã ủng hộ ")).setFontSize(12).setTextAlignment(TextAlignment.CENTER);
        Text storeBold = new Text("Shoes Station")
                .setBold(); // Set font bold
        thanks.add(storeBold);
        thanks.setMarginTop(40);
        document.add(thanks);

        // Đóng tài liệu và trả về mảng byte
        document.close();
        return outputStream.toByteArray();
    }

    public Workbook getWorkbookTemplate(String srcTemplate) throws IOException {
        return new XSSFWorkbook(new ClassPathResource(srcTemplate).getInputStream());
    }

    // Ham tao file excel
    public void createOutputFile(Workbook workbook, String fileName) throws IOException {
        // Get Current Directory using getAbsolutePath()
        File filePath = new File("");
        String excelFilePath = filePath.getAbsolutePath() + "\\" + fileName;
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }

    // Ham tao file excel va tra ve byte[]
    public byte[] createOutputFile(Workbook workbook) throws IOException {
        // Ghi workbook vào ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    public void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }


    // Hàm căn lề trái
    public CellStyle getCellStyleDataLeft(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);
        style.setShrinkToFit(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // Hàm căn lề phải
    public CellStyle getCellStyleDataRight(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setWrapText(false);
        style.setShrinkToFit(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // Hàm căn lề giữa
    public CellStyle getCellStyleDataCenter(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(false);
        style.setShrinkToFit(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    public CellStyle getCellStyleDataCenterRed(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(false);
        style.setShrinkToFit(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    public CellStyle getCellStyleDataLeftRed(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(false);
        style.setShrinkToFit(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // Hàm tạo kiểu dữ liệu datetime
    public CellStyle getCellStyleDataFormatDateTime(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    // hàm in đậm text
    public CellStyle getCellStyleDataCenterAndBoldAndWrapText(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    // Hàm hỗ trợ để thêm cell vào bảng với font đã chỉ định
    private void addCellToTable(
            com.itextpdf.layout.element.Table table,
            String content,
            PdfFont font,
            TextAlignment alignment) {

        Paragraph para = new Paragraph(content)
                .setFont(font)
                .setPaddingBottom(8)
                .setFontSize(11)  // Set font chính
                .setTextAlignment(alignment);  // Thiết lập căn chỉnh cho Paragraph
        com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell()
                .add(para);
        cell.setBorder(Border.NO_BORDER); // Bỏ tất cả các viền
        cell.setBorderBottom(new DashedBorder(1)); // Đặt viền dưới là gạch đứt
        table.addCell(cell);
    }

    private void addCellHeaderTable(
            com.itextpdf.layout.element.Table table,
            String content,
            PdfFont font,
            TextAlignment alignment) {
        Paragraph para = new Paragraph(content)
                .setFont(font)
                .setPaddingBottom(8)
                .setPaddingTop(8)
                .setBold()
                .setFontSize(12)  // Set font chính
                .setTextAlignment(alignment);  // Thiết lập căn chỉnh cho Paragraph
        com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell(1,1).add(para);

        cell.setBorder(Border.NO_BORDER); // Bỏ tất cả các viền
        cell.setBorderTop(new DashedBorder(1)); // Đặt viền trên là gạch đứt
        cell.setBorderBottom(new DashedBorder(1)); // Đặt viền dưới là gạch đứt
        table.addCell(cell);
    }

    private void addCellProductNameTable(
            com.itextpdf.layout.element.Table table,
            String content,
            PdfFont font) {
        Paragraph para = new Paragraph(content)
                .setFont(font)
                .setPaddingTop(8)
                .setFontSize(11);
        com.itextpdf.layout.element.Cell cell = new Cell(1, 3).add(para);
        cell.setBorder(Border.NO_BORDER); // Bỏ tất cả các viền
        table.addCell(cell);
    }
}
