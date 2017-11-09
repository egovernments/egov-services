package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.OpeningBalanceRequest;
import org.egov.inv.model.OpeningBalanceResponse;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class OpeningbalanceApiController implements OpeningbalanceApi {



    public ResponseEntity<OpeningBalanceResponse> openingbalanceCreatePost(@ApiParam(value = "Details for the new Oening Balance request." ,required=true )  @Valid @RequestBody OpeningBalanceRequest openingBalance) {
        // do some magic!
        return new ResponseEntity<OpeningBalanceResponse>(HttpStatus.OK);
    }

    public ResponseEntity<OpeningBalanceResponse> openingbalanceUpdatePost(@ApiParam(value = "Details for the new opening balance." ,required=true )  @Valid @RequestBody OpeningBalanceRequest openingBalanace) {
        // do some magic!
        return new ResponseEntity<OpeningBalanceResponse>(HttpStatus.OK);
    }

}
