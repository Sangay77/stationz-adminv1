package com.station.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class KeyLoaderHelper {

    @Value("${keys.private-path}")
    private Resource privateKeyResource;

    public InputStream loadPrivateKeyStream() throws Exception {
        return privateKeyResource.getInputStream();
    }

    // If you need a File, extract to temp file:
    public File loadPrivateKeyAsFile() throws Exception {
        File temp = File.createTempFile("private", ".key");
        try (InputStream in = privateKeyResource.getInputStream();
             OutputStream out = new FileOutputStream(temp)) {
            in.transferTo(out);
        }
        return temp;
    }
}
