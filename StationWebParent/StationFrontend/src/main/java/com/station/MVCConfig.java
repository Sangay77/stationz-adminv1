package com.station;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("user-photos", "/user-photos/**", registry);
        exposeDirectory("category-image", "/category-image/**", registry);
        exposeDirectory("product-images", "/product-images/**", registry);
        exposeDirectory("site-logo", "/site-logo/**", registry);
    }

    private void exposeDirectory(String dirName, String resourcePattern, ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(dirName).toAbsolutePath();
        String absolutePath = uploadPath.toUri().toString();

        registry.addResourceHandler(resourcePattern)
                .addResourceLocations(absolutePath);
    }
}
