package org.egov.encryption.masking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class MaskingServiceTest {

    @Before
    public void init() {

    }

    @Test
    public void test() throws IllegalAccessException, InstantiationException {

        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<? extends Masking>> maskingTechniques =  reflections.getSubTypesOf(Masking.class);

        Map<String, Masking> maskingTechniqueMap = new HashMap<>();

        for(Class<? extends Masking> maskingTechnique : maskingTechniques) {
            Masking masking = maskingTechnique.newInstance();
            maskingTechniqueMap.put(masking.getMaskingTechnique(), masking);
            log.info(masking.getMaskingTechnique());
        }
    }


}