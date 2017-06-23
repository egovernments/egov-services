package org.egov.pgr.notification.domain.service;

import org.junit.Test;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TemplateServiceTest {

    private static final String TEMPLATE_TYPE = "txt";
    private static final String TEMPLATE_FOLDER = "templates";
    private static final int PRIORITY = 1;
    private static final String TEMPLATE_NAME = "sample_template";

    @Test
    public void test_should_get_generated_string_from_named_template_with_placeholders_replaced() {
        final TemplateService templateService = new TemplateService(getMustacheEngine());
        final Map<Object, Object> map = ImmutableMap.of("name", "foo");

        final String generatedString = templateService.loadByName(TEMPLATE_NAME, map);

        assertEquals("This is a sample message with value foo.", generatedString);
    }

    private MustacheEngine getMustacheEngine() {
        ClassPathTemplateLocator classPathTemplateLocator =
            new ClassPathTemplateLocator(PRIORITY, TEMPLATE_FOLDER, TEMPLATE_TYPE);
        return MustacheEngineBuilder.newBuilder()
            .addTemplateLocator(classPathTemplateLocator)
            .build();
    }

}