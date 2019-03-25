package org.egov.user.domain.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.EncryptionService;
import org.egov.encryption.audit.AuditService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EncryptionDecryptionUtil
{
    private EncryptionService encryptionService;
    @Autowired
    private AuditService auditService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(("${egov.state.level.tenant.id}"))
    private String stateLevelTenantId;

    @Value(("${decryption.abac.enabled}"))
    private boolean abacEnabled;

    public EncryptionDecryptionUtil(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public <T>T encryptObject(Object objectToEncrypt, String key,Class<T> classType)
    {
        try {
            return encryptionService.encryptJson(objectToEncrypt,key,stateLevelTenantId,classType);
        } catch (IOException | HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while encrypting",e);
            throw new CustomException("ENCRYPTION_ERROR","Error occurred in encryption process");
        }catch (Exception e){
            log.error("Unknown Error occurred while encrypting",e);
            throw new CustomException("UNKNOWN_ERROR","Unknown error occurred in encryption process");
        }
    }

    public <E,P>P decryptObject(Object objectToDecrypt, String key, Class<E> classType, RequestInfo requestInfo)
    {

        try {
            boolean objectToDecryptNotList=false;
            if(requestInfo==null || requestInfo.getUserInfo()==null) {
                return (P) objectToDecrypt;
            }
            if(!(objectToDecrypt instanceof List))
            {
                objectToDecryptNotList=true;
                objectToDecrypt= Collections.singletonList(objectToDecrypt);
            }
            final User encrichedUserInfo=getEncrichedandCopiedUserInfo(requestInfo.getUserInfo());
            key = getKeyToDecrypt(objectToDecrypt, encrichedUserInfo);
            P decryptedObject =  (P)encryptionService.decryptJson(objectToDecrypt,key,encrichedUserInfo,classType);
            auditTheDecryptRequest(objectToDecrypt, key, encrichedUserInfo);
            if(objectToDecryptNotList)
            {
                decryptedObject=(P)((List<E>)decryptedObject).get(0);
            }
            return decryptedObject;
        } catch (IOException | HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while decrypting",e);
            throw new CustomException("DECRYPTION_SERVICE_ERROR","Error occurred in decryption process");
        } catch (Exception e){
            log.error("Unknown Error occurred while decrypting",e);
            throw new CustomException("UNKNOWN_ERROR","Unknown error occurred in decryption process");
        }
    }

    public boolean isUserDecryptingForSelf(Object objectToDecrypt, User userInfo) {
        org.egov.user.domain.model.User userToDecrypt = null;
        if(objectToDecrypt instanceof List) {
            if(((List) objectToDecrypt).isEmpty())
                return false;
            if(((List) objectToDecrypt).size() > 1)
                return false;
            userToDecrypt = (org.egov.user.domain.model.User) ((List) objectToDecrypt).get(0);
        } else {
            throw new CustomException("DECRYPTION_NOTLIST_ERROR", objectToDecrypt + " is not of type List of User");
        }

        if(userToDecrypt.getUuid().equalsIgnoreCase(userInfo.getUuid()))
            return true;
        else
            return false;
    }

    private boolean isDecryptionForIndividualUser(Object objectToDecrypt) {
        if(((List) objectToDecrypt).size() == 1)
            return true;
        else
            return false;
    }

    public String getKeyToDecrypt(Object objectToDecrypt, User userInfo) {
        if(!abacEnabled)
            return "ALL_ACCESS";
        else if(isUserDecryptingForSelf(objectToDecrypt, userInfo))
            return "UserListSelf";
        else if(isDecryptionForIndividualUser(objectToDecrypt))
            return "UserListOtherIndividual";
        else
            return "UserListOtherBulk";
    }

    public void auditTheDecryptRequest(Object objectToDecrypt, String key, User userInfo) {
        String purpose;
        if(!abacEnabled)
            purpose="AbacDisabled";
        else if(isUserDecryptingForSelf(objectToDecrypt, userInfo))
            purpose = "Self";
        else if(isDecryptionForIndividualUser(objectToDecrypt))
            purpose = "SingleSearchResult";
        else
            purpose = "BulkSearchResult";

        ObjectNode abacParams = objectMapper.createObjectNode();
        abacParams.set("key", TextNode.valueOf(key));

        List<String> decryptedUserUuid =  (List<String>) ((List) objectToDecrypt).stream()
                .map(user -> ((org.egov.user.domain.model.User) user).getUuid()).collect(Collectors.toList());

        ObjectNode auditData = objectMapper.createObjectNode();
        auditData.set("entityType", TextNode.valueOf(User.class.getName()));
        auditData.set("decryptedEntityIds", objectMapper.valueToTree(decryptedUserUuid));
        auditService.audit(userInfo.getUuid(), System.currentTimeMillis(), purpose, abacParams, auditData);
    }

    private User getEncrichedandCopiedUserInfo(User userInfo)
    {
        List<Role>newRoleList=new ArrayList<>();
        if(userInfo.getRoles()!=null)
        {
            for(Role role:userInfo.getRoles())
            {
                Role newRole=Role.builder().code(role.getCode()).name(role.getName()).id(role.getId()).build();
                newRoleList.add(newRole);
            }
        }

        if(newRoleList.stream().filter(role -> (role.getCode()!=null)&&(userInfo.getType()!=null) && role.getCode().equalsIgnoreCase(userInfo.getType())).count()==0)
        {
            Role roleFromtype=Role.builder().code(userInfo.getType()).name(userInfo.getType()).build();
            newRoleList.add(roleFromtype);
        }

        User newuserInfo=User.builder().id(userInfo.getId()).userName(userInfo.getUserName()).name(userInfo.getName())
                        .type(userInfo.getType()).mobileNumber(userInfo.getMobileNumber()).emailId(userInfo.getEmailId())
                        .roles(newRoleList).tenantId(userInfo.getTenantId()).uuid(userInfo.getUuid()).build();
        return  newuserInfo;
    }
}
