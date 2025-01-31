package com.luan.common.controller;

import com.luan.common.service.PdfReportService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.ByteArrayOutputStream;

@Path("/report")
public class ReportController {

    @Inject
    PdfReportService pdfReportService;

    @GET
    @Path("/generate")
    public Response generate() throws Exception {
        ByteArrayOutputStream stream = pdfReportService.convertHtmlToPdf("templates/products.html", null);
        return Response.ok(stream.toByteArray()).header("Content-Disposition", "attachment; filename=products.pdf").build();
    }

}
