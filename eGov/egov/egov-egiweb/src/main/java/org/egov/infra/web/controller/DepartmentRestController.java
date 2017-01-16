//package org.egov.infra.web.controller;
//
//import static org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter.FACTORY;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.egov.infra.admin.master.entity.Department;
//import org.egov.infra.admin.master.service.DepartmentService;
//import org.egov.infra.exception.ApplicationRuntimeException;
//import org.egov.infra.web.adaptor.DepartmentAdaptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.gson.GsonBuilder;
//
//@RestController
//public class DepartmentRestController {
//
//	@Autowired
//	private DepartmentService departmentService;
//
//	@RequestMapping(value = "/departments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getDepartments(@ModelAttribute("department") final Department department, Model model)
//			throws IOException {
//		final String jsonResponse = toJSON(departmentService.getAllDepartments(), Department.class,
//				DepartmentAdaptor.class);
//		return jsonResponse;
//	}
//
//	public static <T> String toJSON(List<Department> list, Class<Department> class1,
//			Class<DepartmentAdaptor> class2) {
//		try {
//			return new GsonBuilder().registerTypeAdapterFactory(FACTORY)
//					.registerTypeAdapter(class1, class2.newInstance()).create().toJson(list);
//		} catch (Exception e) {
//			throw new ApplicationRuntimeException("Could not convert object list to json string", e);
//		}
//	}
//
//}
