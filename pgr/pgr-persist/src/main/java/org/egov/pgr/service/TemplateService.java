package org.egov.pgr.service;

import org.springframework.stereotype.Service;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;
import org.trimou.util.ImmutableMap;

import java.util.Map;

@Service
public class TemplateService {

    private static final String TEMPLATE_TYPE = "txt";
    private static final String TEMPLATE_FOLDER = "templates";
    private static final int PRIORITY = 1;

    private MustacheEngine templatingEngine;

    public TemplateService() {
        ClassPathTemplateLocator classPathTemplateLocator = new ClassPathTemplateLocator(PRIORITY, TEMPLATE_FOLDER, TEMPLATE_TYPE);
        this.templatingEngine = MustacheEngineBuilder.newBuilder().addTemplateLocator(classPathTemplateLocator).build();
    }

    public String loadByName(String templateName, ImmutableMap.ImmutableMapBuilder<Object, Object> builder) {
        return templatingEngine.getMustache(templateName).render(builder.build());
    }

    public String loadByName(String templateName, Map<Object, Object> builder) {
        return templatingEngine.getMustache(templateName).render(builder);
    }

}
