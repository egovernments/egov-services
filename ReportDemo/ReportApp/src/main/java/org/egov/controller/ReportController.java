package org.egov.controller;
import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.egov.domain.model.MetaDataRequest;
import org.egov.domain.model.ReportMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

	private static final String UTF_8 = "UTF-8";
	
	
	public ReportMetaData reportMetaData;
	@Autowired
	public ReportController(ReportMetaData reportMetaData) {
		// TODO Auto-generated constructor stub
		this.reportMetaData = reportMetaData;
	}

	
	


	/*@GetMapping("/report/metadata/_get")
	public ResponseEntity<?> create(@RequestBody @Valid final MetaDataRequest metaDataRequest,
			final BindingResult errors) {
		System.out.println(reportMetaData.toString());
        
		return null;
	}*/
	@GetMapping("/report/metadata/_get")
	public String create() {
		System.out.println(this.reportMetaData.getQuery());
        
		return null;
	}
	
	
	public String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), UTF_8).replace("\n", "");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}