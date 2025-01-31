package com.luan.common.service;

import java.io.ByteArrayOutputStream;

public interface PdfReportService {

    <T> ByteArrayOutputStream convertHtmlToPdf(String htmlPath, T data) throws Exception;

}
