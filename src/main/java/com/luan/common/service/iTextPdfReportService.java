package com.luan.common.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.ByteArrayOutputStream;

/**
 * Implementação do serviço de geração de relatórios em PDF utilizando a biblioteca iText.
 */
@ApplicationScoped
public class iTextPdfReportService implements PdfReportService {

    @Inject
    Template products;

    /**
     * Converte um arquivo HTML para um PDF e retorna o resultado como um ByteArrayOutputStream.
     *
     * @param htmlPath Caminho do arquivo HTML.
     * @return ByteArrayOutputStream contendo o PDF gerado.
     */
    public ByteArrayOutputStream convertHtmlToPdf(String htmlPath, Object data) {
        TemplateInstance templateInstance = products.instance();
        String htmlInput = templateInstance.render();
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();

        HtmlConverter.convertToPdf(htmlInput, pdfOutputStream, converterProperties);

        return pdfOutputStream;
    }

}
