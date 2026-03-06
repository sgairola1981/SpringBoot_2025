package com.gairola.llm.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class DocxReaderService {

    public String readDocx(InputStream inputStream) {

        StringBuilder text = new StringBuilder();

        try (XWPFDocument document = new XWPFDocument(inputStream)) {

            document.getParagraphs().forEach(p ->
                    text.append(p.getText()).append("\n")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return text.toString();
    }
}