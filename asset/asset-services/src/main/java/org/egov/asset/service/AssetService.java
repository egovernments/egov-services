/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssetService {

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private AssetProducer assetProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public List<Asset> getAssets(AssetCriteria searchAsset) {
		System.out.println("AssetService");
		return assetRepository.findForCriteria(searchAsset);
	}
	
	public AssetResponse create(AssetRequest assetRequest) {
		Asset asset=assetRepository.create(assetRequest);
		List<Asset> assets=new ArrayList<Asset>();
		assets.add(asset);
		AssetResponse assetResponse=getAssetResponse(assets);
		return assetResponse;
	}
	
	public AssetResponse createAsync(AssetRequest assetRequest) {
		
		assetRequest.getAsset().setCode(assetRepository.getAssetCode());
		
		// TODO validate assetcategory for an asset
		ObjectMapper objectMapper=new ObjectMapper();
		System.out.println("assetRequest createAsync::"+assetRequest);
		String value=null;
		
		try {
			value = objectMapper.writeValueAsString(assetRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		assetProducer.sendMessage(applicationProperties.getCreateAssetTopicName(),"save-asset", value);
		
		List<Asset> assets = new ArrayList<Asset>();
		assets.add(assetRequest.getAsset());
		AssetResponse assetResponse = getAssetResponse(assets);
		
	   return assetResponse;	
	}
	
	public AssetResponse update(AssetRequest assetRequest){
		
		Asset asset=assetRepository.update(assetRequest);
		List<Asset> assets=new ArrayList<Asset>();
		assets.add(asset);
		AssetResponse assetResponse=getAssetResponse(assets);
		return assetResponse;
	}
	
	public AssetResponse updateAsync(String topic,String key,AssetRequest assetRequest){
		
		ObjectMapper objectMapper=new ObjectMapper();
		System.out.println("assetRequest createAsync::"+assetRequest);
		String value=null;
		
		try {
			value = objectMapper.writeValueAsString(assetRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		assetProducer.sendMessage(topic, key, value);
		
		List<Asset> assets = new ArrayList<Asset>();
		assets.add(assetRequest.getAsset());
		AssetResponse assetResponse = getAssetResponse(assets);
		
	   return assetResponse;
	}
	
	
	
	private AssetResponse getAssetResponse(List<Asset> assets){
		AssetResponse  assetResponse=new AssetResponse();
		assetResponse.setAssets(assets);
	  return assetResponse;
	}
}