package org.egov.eis.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.eis.domain.model.RequestContext;
import org.egov.eis.domain.service.DepartmentService;
import org.egov.eis.persistence.entity.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Test
    public void test_should_fetch_department_for_given_code() throws Exception {
        final String departmentCode = "departmentCode";
        final Department department = new Department();
        department.setId(1L);
        department.setCode("departmentCode");
        department.setName("departmentName");
        when(departmentService.find(departmentCode))
                .thenReturn(Collections.singletonList(department));

        mockMvc.perform(get("/departments")
                .header("X-CORRELATION-ID", "someId")
                .param("code", departmentCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("departmentsResponse.json")));

        assertEquals("someId", RequestContext.getId());
    }

    @Test
    public void test_should_fetch_all_departments() throws Exception {
        final Department department = new Department();
        department.setId(1L);
        department.setCode("departmentCode");
        department.setName("departmentName");
        when(departmentService.find(null))
                .thenReturn(Collections.singletonList(department));

        mockMvc.perform(get("/departments")
                .header("X-CORRELATION-ID", "someId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("departmentsResponse.json")));

        assertEquals("someId", RequestContext.getId());
    }


    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}