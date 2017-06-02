/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var fileinputid = [];
var filefilled = {};//image fullfilled array
var fileid;
var fileSize = 5e+6;
$(document).ready(function(){
	
	var fileformats = ['jpg', 'jpeg', 'gif', 'png',  '3g2', '3gp', '3gp2', '3gpp', 'divx', 'flv', 'mov', 'mp4', 'mpeg4', 'mpg4', 'mpeg', 'mpg', 'm4v', 'wmv' ];
	var myCenter;
	
	fileinputid = ['file1','file2','file3'];//assigning file id
	var removedarray = [];
	
	
	$('#triggerFile:enabled').click(function(){
		//console.log(removedarray.length);
		if(removedarray.length == 0 || removedarray.length == 3)
		{
			var keys=Object.keys(filefilled);
			fileid = fileinputid[keys.length];
			//console.log("File ID normal:"+fileid);
			}else{
			fileid = removedarray[0];
			//console.log("File ID removal:"+fileid);
		}
		
		$('#'+fileid).trigger("click");
	});
	
	$(document).on('click','.remove-img',function(){
		//console.log("Removal");
		var toRemove = $(this).attr('data-file-id');
		delete filefilled[toRemove];
		if ($.inArray(toRemove, removedarray) !== -1)//check removed file id already exists, if exists leave as such or push it
		{
			
			}else{
			removedarray.push(toRemove);
			removedarray.sort();
			//console.log("sorted removed array"+removedarray);
		}
		
		//console.log("File filled array:"+JSON.stringify(filefilled));
		$('#'+toRemove+'block').remove();
		$('#'+$(this).attr('data-file-id')).val('');
		$('#triggerFile').removeAttr('disabled');
		
	});
	
	$('#file1, #file2, #file3').on('change.bs.fileinput',function(e)
	{
		/*validation for file upload*/
		myfile= $( this ).val();
		var ext = myfile.split('.').pop().toLowerCase();
		if($.inArray(ext, fileformats) > -1){
				//do something  
				if(this.files[0].size > fileSize){
					bootbox.alert(translate('core.error.file.exceed'));
					$( this ).val('');	
					return;
				}
			}else{
				bootbox.alert(ext+' '+translate('core.error.fileformat.notallowed'));
				$( this ).val('');	
				return;
		}
		
		if(e.target.files.length>0)
		{
			filefilled[$(this).attr('id')]=this.files[0].name;
			//console.log("File filled array:"+JSON.stringify(filefilled));
			readURL(this, this.files[0].name);
			var index = removedarray.indexOf(fileid);
			if (index > -1) {
				removedarray.splice(index, 1);
			}
			if(fileid == 'file1')
			{
				try{
					EXIF.getData(e.target.files[0], function() {
						var imagelat = EXIF.getTag(this, "GPSLatitude"),
						imagelongt = EXIF.getTag(this, "GPSLongitude");
						if(imagelat || imagelongt){
							//console.log(imagelat+'<--->'+imagelongt)
							var formatted_lat = format_lat_long(imagelat.toString());
							var formatted_long = format_lat_long(imagelongt.toString());
							$.ajax({
								type: "POST",
								url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+formatted_lat+','+formatted_long+'&sensor=true',
								dataType: 'json',
								success : function(data){
									$('#location').typeahead('val', data.results[0].formatted_address);
									myCenter=new google.maps.LatLng(formatted_lat, formatted_long);
									$('#lat').val(formatted_lat);
									$('#lng').val(formatted_long);
									lat = formatted_lat;
									lng = formatted_long;
									//setmarkerfromimage();
								    	map.setCenter(myCenter);
								}
							});
							//console.log(EXIF.pretty(this));	
						}
					});
				}catch(e){
					
				}
			}
			
			if(Object.keys(filefilled).length == 3)
			{
				$('#triggerFile').attr('disabled','disabled');
			}
		}
	});
	
	function readURL(input, filename) {
		//console.log("Key:"+fileid);
		//console.log("Key value is:"+filefilled[fileid]);
		filename = ((filename.length > 15) ? filename.substring(0,13)+".." : filename);
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				setImage(e,input, filename);
			}
			
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	function format_lat_long(latorlong)
	{
		var loc_arry = latorlong.split(",");
		var degree= parseFloat(loc_arry[0]);
		var minutes= parseFloat(loc_arry[1]);
		var seconds= parseFloat(loc_arry[2]);
		
		//formula is degree+((minutes*60)+seconds/3600)
		var formatted = degree+((minutes*60)+seconds)/3600;
		
		return formatted;
	}
	
	
	var map, geocoder, geolocate, marker, address, citylat, citylng, lat, lng;    
	
	function initialize() {
		
		//city lat and lng from ajax - static kurnool
		var latLng,mapOptions = {
			zoom: 10,
			timeout: 500, 
			mapTypeControl: false,
			navigationControl: false,
		};

		var setCity = function(){

			citylat = 15.82223689, citylng = 78.02439864;
			lat = 15.82223689, lng = 78.02439864;
			latLng = {
		        lat: citylat, // fallback lat 
		        lng: citylng  // fallback lng
		    };

			setlatlong(citylat, citylng);

			mapcenterchangeevent();
			
		}
		
		var userLocationFound = function(position){

			latLng = {
		        lat: position.coords.latitude,
		        lng: position.coords.longitude
		    };

		    lat = latLng.lat, lng = latLng.lng;
		    //console.log("User confirmed! Location found: " + latLng.lat + ", " + latLng.lng);

		    setlatlong(latLng.lat, latLng.lng);

			mapcenterchangeevent();
			
		}

		var userLocationNotFound = function(position){

			latLng = {
		        lat: 20.5937,
		        lng: 78.9629
		    };

		    lat = latLng.lat, lng = latLng.lng;

		    //console.log("User not confirmed! : 20.5937, 78.9629");

		    setlatlong(latLng.lat, latLng.lng);

			mapcenterchangeevent();
			
		}

		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapOptions);
		
		try{
			var GeoMarker = new GeolocationMarker(map);
			$('<div/>').addClass('centerMarker').appendTo(map.getDiv());
		}catch(e){

		}

		setCity();
		
		if(!citylat && !citylng)
			navigator.geolocation.getCurrentPosition(userLocationFound, userLocationNotFound, mapOptions);

		setTimeout(function () {
		    if(!latLng){
		        //console.log("No confirmation from user, using fallback");
		        userLocationNotFound();
		    }else{
		        //console.log("Location was set");
		    }
		}, mapOptions.timeout + 500); // Wait extra second
		
		searchBar(map);
		
	};

	function searchBar(map) {
		
		try{
			var input = /** @type {!HTMLInputElement} */(
			      document.getElementById('pac-input'));

			  var autocomplete = new google.maps.places.Autocomplete(input);
			  autocomplete.bindTo('bounds', map);

			  autocomplete.addListener('place_changed', function() {
			    var place = autocomplete.getPlace();
			    if (!place.geometry) {
			      window.alert("Autocomplete's returned place contains no geometry");
			      return;
			    }

			    // If the place has a geometry, then present it on a map.
			    if (place.geometry.viewport) {
			      map.fitBounds(place.geometry.viewport);
			    } else {
			      map.setCenter(place.geometry.location);
			      map.setZoom(17);  // Why 17? Because it looks good.
			    }

			    var address = '';
			    if (place.address_components) {
			      address = [
			        (place.address_components[0] && place.address_components[0].short_name || ''),
			        (place.address_components[1] && place.address_components[1].short_name || ''),
			        (place.address_components[2] && place.address_components[2].short_name || '')
			      ].join(' ');
			    }

			  });
		}catch(e){

		}
    };
	
	function mapcenterchangeevent(){
		google.maps.event.addListener(map, 'center_changed', function() {
			var NewMapCenter = map.getCenter();
			var customLat = NewMapCenter.lat();
			var customLng = NewMapCenter.lng();
			getAddress(customLat, customLng);
		});
	}

	function setlatlong(citylat , citylng){
		var userLatLng = new google.maps.LatLng(citylat, citylng);
		getAddress(citylat, citylng);
		map.setCenter(userLatLng);
	}
	
	function getAddress(lat, lng){
		$.ajax({
			type: "POST",
			url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true',
			dataType: 'json',
			success : function(data){
				 address = data.results[0].formatted_address;
			}
		});	
	}

	if(mode == 'create'){
		google.maps.event.addDomListener(window, 'load', initialize);
	
		google.maps.event.addDomListener(window, "resize", resizingMap());
	}
	
	$('#modal-6').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		
		//complaint registration map
		resizeMap();
	});
	
	function resizeMap() {
		if(typeof map =="undefined") return;
		setTimeout( function(){resizingMap();} , 400);
	}
	
	function resizingMap() {
		if(typeof map =="undefined") return;
		var center = map.getCenter();
		google.maps.event.trigger(map, "resize");
		map.setCenter(center); 
	}
	
	$('.btn-save-location').click(function(){
		var location = map.getCenter();
		lat = location.lat();
		lng = location.lng();
		$.ajax({
			type: "POST",
			url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true',
			dataType: 'json',
			success : function(data){
				 address = data.results[0].formatted_address;
				 $('#location').typeahead('val', address);
				 $('#latlngaddress').val(address);
			}
		});	
		//console.log(lat+'<-->'+lng);
		$('#lat').val(lat);
		$('#lng').val(lng);
		$('#address_id, #locationid').val('');
	});

	$('#modal-6').on('hidden.bs.modal', function () {
		$('#pac-input').val('');
	    var userLatLng = new google.maps.LatLng(lat, lng);
	    map.setCenter(userLatLng);
	});
	
});

function setImage(e,input, filename){
	var type = input.files[0].type.split('/')[0];
	var ind = fileid.split("file").pop();
	if(type == 'image'){
		var template = '<div id="file'+ind+'block" class="add-margin col-sm-4 col-xs-4">'+
						'<img src="'+e.target.result+'" alt="" width="100%"/>'+
						'<div class="remove-img" data-file-id="'+fileid+'"><i class="fa fa-times-circle"></i></div>'+
						'<div class="add-padding">'+filename+'</div>'+
						'</div>';
	}else if(type="video"){
		var template = '<div id="file'+ind+'block" class="add-margin col-sm-4 col-xs-4">'+
						'<video width="100%" controls>'+
						'<source src="'+e.target.result+'" type="video/mp4"></video>'+
						'<div class="remove-img" data-file-id="'+fileid+'"><i class="fa fa-times-circle"></i></div>'+
						'</div>';
	}
	$('.fileblock').append(template);
}

function clearimagepreview(){
	for(var i=1;i<=fileinputid.length;i++){
		$('#file'+i+'block, .preview-cross'+i+', #preview'+i+'').hide();
		$('#preview'+i+'').removeAttr("src");
		$('#filename'+i+'').html('');
	}
	filefilled = {};
	removedarray = [];
	$('#triggerFile').removeAttr('disabled');
}

function navigateToImage(obj){
	var position = obj.offset().top-($('.navbar-header').height()+40);
	$('html, body').animate({
      scrollTop: position
  	}, 300);
}