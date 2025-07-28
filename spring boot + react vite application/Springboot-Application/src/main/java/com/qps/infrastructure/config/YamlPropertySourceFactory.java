package com.qps.infrastructure.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Objects;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet(); // đảm bảo factory khởi tạo xong

        Properties properties = Objects.requireNonNull(factory.getObject(), "YAML properties must not be null");
        String sourceName = name != null ? name : Objects.requireNonNull(resource.getResource().getFilename(), "Filename must not be null");

        return new PropertiesPropertySource(sourceName, properties);
    }
}

