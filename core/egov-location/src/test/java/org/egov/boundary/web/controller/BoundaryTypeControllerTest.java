package org.egov.boundary.web.controller;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.boundary.domain.service.BoundaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public class BoundaryTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoundaryTypeService boundaryTypeService;


   /* @Test
    public void testShouldFetchAllBoundarieTypesForHierarchyTypeidAndtenantId() throws Exception {
        final BoundaryType expectedBoundaryType = BoundaryType.builder().id("1L").name("City").tenantId("tenantId").build();
        when(boundaryTypeService.getAllBoundarTypesByHierarchyTypeIdAndTenantName(any(String.class), any(String.class)))
                .thenReturn(Collections.singletonList(expectedBoundaryType));
        mockMvc.perform(post("/boundarytypes/getByHierarchyType").param("hierarchyTypeName", "ADMINISTRATION").param("tenantId", "tenantId")
                .header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("boundaryTypeResponse.json")));
    }*/

   /* @Test
    public void testShouldReturnBadRequestWhenHierarchyTypeidAndTenmantIdIsEmpty() throws Exception {
        when(boundaryTypeService.getAllBoundarTypesByHierarchyTypeIdAndTenantId(any(Long.class), any(String.class)))
                .thenReturn(null);
        mockMvc.perform(post("/boundarytypes/getByHierarchyType").param("hierarchyTypeId", "")
                .param("tenantId", "").header("X-CORRELATION-ID", "someId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
    }*/

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
