package org.egov.wcms.web.controllers;


import org.egov.wcms.web.models.ErrorRes;
import org.egov.wcms.web.models.MeterReadingReq;
import org.egov.wcms.web.models.MeterReadingRes;
import org.egov.wcms.web.models.RequestInfo;
    import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import javax.validation.constraints.*;
    import javax.validation.Valid;
    import javax.servlet.http.HttpServletRequest;
        import java.util.Optional;
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-03T01:09:48.367+05:30")

@Controller
    @RequestMapping("/connection/v2")
    public class MeterreadingApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public MeterreadingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/meterreading/_create", method = RequestMethod.POST)
                public ResponseEntity<MeterReadingRes> meterreadingCreatePost(@ApiParam(value = "required parameters have to be populated" ,required=true )  @Valid @RequestBody MeterReadingReq meterReadings) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<MeterReadingRes>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"actionHistory\" : [ {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  }, {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  } ],  \"meterReading\" : [ {    \"gapCode\" : \"gapCode\",    \"resetFlag\" : true,    \"connectionNumber\" : \"connectionNumber\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"reading\" : 6.027456183070403,    \"consumption\" : 1.46581298050294517310021547018550336360931396484375,    \"id\" : \"id\",    \"consumptionAdjusted\" : 5.962133916683182377482808078639209270477294921875,    \"numberOfDays\" : 5.63737665663332876420099637471139430999755859375,    \"readingDate\" : 0  }, {    \"gapCode\" : \"gapCode\",    \"resetFlag\" : true,    \"connectionNumber\" : \"connectionNumber\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"reading\" : 6.027456183070403,    \"consumption\" : 1.46581298050294517310021547018550336360931396484375,    \"id\" : \"id\",    \"consumptionAdjusted\" : 5.962133916683182377482808078639209270477294921875,    \"numberOfDays\" : 5.63737665663332876420099637471139430999755859375,    \"readingDate\" : 0  } ]}", MeterReadingRes.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<MeterReadingRes>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<MeterReadingRes>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/meterreading/_search", method = RequestMethod.POST)
                public ResponseEntity<MeterReadingRes> meterreadingSearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,@Size(min=0,max=64) @ApiParam(value = "HSC consumer number for the water connection") @Valid @RequestParam(value = "consumerNumber", required = false) String consumerNumber) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<MeterReadingRes>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"actionHistory\" : [ {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  }, {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  } ],  \"meterReading\" : [ {    \"gapCode\" : \"gapCode\",    \"resetFlag\" : true,    \"connectionNumber\" : \"connectionNumber\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"reading\" : 6.027456183070403,    \"consumption\" : 1.46581298050294517310021547018550336360931396484375,    \"id\" : \"id\",    \"consumptionAdjusted\" : 5.962133916683182377482808078639209270477294921875,    \"numberOfDays\" : 5.63737665663332876420099637471139430999755859375,    \"readingDate\" : 0  }, {    \"gapCode\" : \"gapCode\",    \"resetFlag\" : true,    \"connectionNumber\" : \"connectionNumber\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"reading\" : 6.027456183070403,    \"consumption\" : 1.46581298050294517310021547018550336360931396484375,    \"id\" : \"id\",    \"consumptionAdjusted\" : 5.962133916683182377482808078639209270477294921875,    \"numberOfDays\" : 5.63737665663332876420099637471139430999755859375,    \"readingDate\" : 0  } ]}", MeterReadingRes.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<MeterReadingRes>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<MeterReadingRes>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/meterreading/_update", method = RequestMethod.POST)
                public ResponseEntity<MeterReadingRes> meterreadingUpdatePost(@ApiParam(value = "required parameters have to be populated" ,required=true )  @Valid @RequestBody MeterReadingReq meterReadings) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<MeterReadingRes>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"actionHistory\" : [ {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  }, {    \"actions\" : [ {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    }, {      \"isInternal\" : \"isInternal\",      \"by\" : \"by\",      \"tenantId\" : \"tenantId\",      \"businessKey\" : \"businessKey\",      \"action\" : \"action\",      \"comment\" : \"comment\",      \"assignee\" : \"assignee\",      \"media\" : [ \"media\", \"media\" ],      \"when\" : 4,      \"status\" : \"status\"    } ]  } ],  \"meterReading\" : [ {    \"gapCode\" : \"gapCode\",    \"resetFlag\" : true,    \"connectionNumber\" : \"connectionNumber\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"reading\" : 6.027456183070403,    \"consumption\" : 1.46581298050294517310021547018550336360931396484375,    \"id\" : \"id\",    \"consumptionAdjusted\" : 5.962133916683182377482808078639209270477294921875,    \"numberOfDays\" : 5.63737665663332876420099637471139430999755859375,    \"readingDate\" : 0  }, {    \"gapCode\" : \"gapCode\",    \"resetFlag\" : true,    \"connectionNumber\" : \"connectionNumber\",    \"auditDetails\" : {      \"lastModifiedTime\" : 5,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 5    },    \"tenantId\" : \"tenantId\",    \"reading\" : 6.027456183070403,    \"consumption\" : 1.46581298050294517310021547018550336360931396484375,    \"id\" : \"id\",    \"consumptionAdjusted\" : 5.962133916683182377482808078639209270477294921875,    \"numberOfDays\" : 5.63737665663332876420099637471139430999755859375,    \"readingDate\" : 0  } ]}", MeterReadingRes.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<MeterReadingRes>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<MeterReadingRes>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
