package org.egov.pgrrest.read.domain.service;

import delight.nashornsandbox.NashornSandbox;
import org.egov.pgrrest.read.factory.JSScriptEngineFactory;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSScriptEngineFactoryTest {

    private NashornSandbox nashornSandbox;

    @Before
    public void before() {
        nashornSandbox =
            new JSScriptEngineFactory("2000", "1").create();
    }

    @Test
    public void test_should_execute_simple_java_script_code() {
        nashornSandbox.inject("a", "10");
        final Object result = nashornSandbox.eval("a == 10");
        assertEquals(result, true);
    }

    @Test
    public void test_should_execute_date_diff_in_days_function() {
        nashornSandbox.inject("date1", new LocalDate(2017, 7, 25));
        nashornSandbox.inject("date2", new LocalDate(2017, 7 , 27));
        final Object result = nashornSandbox.eval("dateDifferenceInDays(date1, date2)");
        assertEquals(result, 2);
    }

}