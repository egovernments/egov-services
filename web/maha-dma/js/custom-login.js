var ulbsList=[];
var servicesList=[];
var groupByModuleServices=[];
var redirectUrl="";

Handlebars.registerHelper('eq', function(arg1, arg2, options) {
   return arg1 === arg2;
});

Handlebars.registerHelper("inc", function(value, options)
{
    return parseInt(value) + 1;
});

function arrayGroupByKey(arry, groupByKey){
		var resultData={};
		var result=arry.reduce(function(result, current) {
		    result[current[groupByKey]] = result[current[groupByKey]] || [];
		    result[current[groupByKey]].push(current);
		    return result;
		}, {});

		Object.keys(result).sort().forEach(function(key) {
		  resultData[key] = result[key];
		});

		return resultData;
}

$(document).ready(function(){

  $('.search-service, .track, .download').hide();

  $('.active-component ul li').click(function(){
    $('.active-component ul li').removeClass('active');
    var className = $(this).attr('class');
    $('.'+className).addClass('active');
    $('.citizen-login, .search-service, .track, .download').hide();
    $($(this).data('redirect')).show();
  });

  var servicesarray=[];

  $('ul.collapse').find('li.news-item div').each(function(i){
    var servicesList = {};
    servicesList['text']=$(this).find('a').html()+' - '+ $(this).parents('ul').data('header');
    servicesList['target']=$(this).find('a').data('target');
    servicesarray.push(servicesList);
  });

  for (var j = 0; j < servicesarray.length; j++){
    $('.search-service-ul').append('<li><a href="" data-toggle="modal" data-target='+servicesarray[j].target+'>'+servicesarray[j].text+'</a></li>')
  }

  $('#searchService').keyup(function(){
    var value = $(this). val();
    $('.search-service-ul').empty();
    for (var j = 0; j < servicesarray.length; j++){
      var result = servicesarray[j].text.toLowerCase().search(value.toLowerCase());
      if(result >= 0)
        $('.search-service-ul').append('<li><a href="" data-toggle="modal" data-target='+servicesarray[j].target+'>'+servicesarray[j].text+'</a></li>')
    }
  })

		var ulbOptionTemplate = Handlebars.compile($('#ulb-option-template').html());
    var documentsTemplate = Handlebars.compile($('#documents-body-template').html());
    var servicesMenuTempalte = Handlebars.compile($('#services-menu-template').html());

    if(localStorage.getItem('auth-token')){
      window.location.href = window.location.origin + "/app/v1/#/prd/dashboard";
    }

    $('#ulb-dropdown').popover();

    $('#ulb-dropdown').click(function(e){
        $(this).popover('hide');
    });

    function predicateBy(prop){
       return function(a,b){
          if( a[prop] > b[prop]){
              return 1;
          }else if( a[prop] < b[prop] ){
              return -1;
          }
          return 0;
       }
    }


    $.ajax({
			  url: 'data/ulbs.json',
				data:{ "_": $.now() },
				headers: {
				     'Cache-Control': 'max-age=1000'
				},
			  success: function(response){
          ulbsList = response.sort(predicateBy("ulbName"));
          loadUlbsList();
			  },
			  cache: false
			});

      // $.ajax({
  		// 	  url: 'data/servicesList.json',
  		// 		data:{ "_": $.now() },
  		// 		headers: {
  		// 		   'Cache-Control': 'max-age=1000'
  		// 		},
  		// 	  success: function(response){
      //       servicesList = response;
      //       groupByModuleServices = arrayGroupByKey(servicesList, "moduleName");
      //       loadServiceMenus();
  		// 	  },
  		// 	  cache: false
  		// 	});

      function loadUlbsList(){
        $('#ulb-dropdown, #ulb-dropdownForSignup, #ulb-dropdownForTracking, #ulb-dropdownForDownload').html(ulbOptionTemplate(ulbsList));
      }

      function loadServiceMenus(){
        // console.log('coming inside');
        // console.log('groupByModuleServices', groupByModuleServices);
        $('.service-table').html(servicesMenuTempalte(groupByModuleServices));
      }

      // $(document).on('click', 'a.action-item', function(e){
      //     //console.log('serviceName', $(this).data('service-name'));
      //     var serviceName = $(this).data('service-name');
      //     var moduleName = $(this).data('module-name');
      //     $('#servicesDetailModalLabel').html($(this).data('service-name')+" - "+moduleName);
      //
      //     var service = servicesList.find((service) => service.serviceName === serviceName
      //               && service.moduleName === moduleName);
      //
      //     var ulb = ulbsList.find((ulb)=>ulb.tenantId === $('#ulb-dropdown').val());
      //
      //     if(service && service.redirectUrl){
      //       // if(!service.slaTable){
      //       //   var uniqueKeys = service.slaTable.columns.reduce(function (acc, obj) {
      //       //       return acc.concat(acc.indexOf(obj.key) === -1? obj.key : undefined);
      //       //   }, []);
      //       //   service['uniqueKeys'] = uniqueKeys;
      //       // }
      //
      //       // console.log('service', service);
      //
      //       // console.log('docs response', documentsTemplate(service));
      //
      //       //documentsTempalte
      //       $('#apply-btn').attr('data-url', service.redirectUrl);
      //       $('#documents-body').html(documentsTemplate(service));
      //       $('#servicesDetailModal').modal('show');
      //     }
      //     else{
      //       //else TODO
      //       alert('This service is not available');
      //     }
      // });

      $('.apply-btn').click(function(e){
        alert('Please register yourself and then login.');
      })

      // $('#apply-btn').click(function(e){
      //     redirectUrl = $(this).attr('data-url');
      //     alert('Please register yourself and then login.');
      //     $('#servicesDetailModal').modal('hide');
      //
      //     // var serviceRedirectUrl=$(this).attr('data-url');
      //     // if(serviceRedirectUrl){
      //     //   $('#servicesDetailModal').modal('hide');
      //     //   window.open(serviceRedirectUrl);
      //     // }
      //
      //
      // });

      $('#create-account').click(function(e){
          // window.location = window.location.origin + '/app/v1/#/mh.roha?signup=true';
          if(!$('#ulb-dropdownForSignup').val()){
            $('#ulb-dropdownForSignup').popover('show');
            return;
          }
          else{
            $('#ulb-dropdownForSignup').popover('hide');
          }
          var tenantId = $("#ulb-dropdownForSignup").val();
          window.location = window.location.origin + '/app/v1/#/'+tenantId+'?signup=true';
      });

      $('#track-go').click(function(e){
        if(!$('#ulb-dropdownForTracking').val()){
          $('#ulb-dropdownForTracking').popover('show');
          return;
        }
        else{
          $('#ulb-dropdownForTracking').popover('hide');
        }
        if(!$('#trackApplication').val()){
          $('#trackApplication').popover('show');
          return;
        }
        var tenantId = $("#ulb-dropdownForTracking").val();
        localStorage.setItem("tenantId", tenantId);
        window.open(window.location.origin + '/app/v1/#/service/request/view/'+$.trim($('#trackApplication').val()));
      });

      $('#download-go').click(function(e){
        if(!$('#ulb-dropdownForDownload').val()){
          $('#ulb-dropdownForDownload').popover('show');
          return;
        }
        else{
          $('#ulb-dropdownForDownload').popover('hide');
        }
        if(!$('#downloadApplication').val()){
          $('#downloadApplication').popover('show');
          return;
        }
        var tenantId = $("#ulb-dropdownForDownload").val();
        localStorage.setItem("tenantId", tenantId);
        window.open(window.location.origin + '/app/v1/#/service/request/view/'+$.trim($('#downloadApplication').val()));
      });

      $('#trackApplication').on('input',function(e){
        $('#trackApplication').popover('hide');
      });

      $('#downloadApplication').on('input',function(e){
        $('#downloadApplication').popover('hide');
      });

      // $('#trackApplication').change(function(){
      //   $('#trackApplication').popover('hide');
      // });

      $('#loginBtn').click(function(e) {

          if(!$('#ulb-dropdown').val()){
            $('#ulb-dropdown').popover('show');
            return;
          }
          else{
            $('#ulb-dropdown').popover('hide');
          }

          if($("#mobileNumber").val() && $("#password").val()) {
            var mobileNo = $("#mobileNumber").val();
            var password = $("#password").val();
            var tenantId = $("#ulb-dropdown").val() || "default";
            waitingDialog.show('Please wait logging...');

            var params = new URLSearchParams();
            params.append('username', mobileNo);
            params.append('password', password);
            params.append('grant_type', 'password');
            params.append('scope', 'read');
            params.append('tenantId', tenantId);
            $.ajax({
              url: window.location.origin + "/user/oauth/token?"+params,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify({}),
              contentType: 'application/x-www-form-urlencoded',
              headers:{
                'Authorization' :'Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0'
              },
              success: function(response) {
                waitingDialog.hide();
                localStorage.setItem("auth-token", response.access_token);
                localStorage.setItem("token", response.access_token);
                localStorage.setItem("userRequest", JSON.stringify(response.UserRequest));
                localStorage.setItem("auth", response.access_token);
                localStorage.setItem("type", response.UserRequest.type);
                localStorage.setItem("id", response.UserRequest.id);
                localStorage.setItem("tenantId", response.UserRequest.tenantId);
                if(!redirectUrl){
                    window.location.href = window.location.origin + "/app/v1/#/prd/dashboard";
                }
                else{
                  window.location.href = window.location.origin + "/app/v1/#/prd/dashboard"+ redirectUrl;
                }

              },
              error: function(response) {
                waitingDialog.hide();
                //console.log('response', response.responseJSON.error_description);
                alert(response.responseJSON.error_description);
              }
            });
          }
          else{
            alert('Please enter your mobile no / login id and password!');
          }
      });

      $(document).on('click', '.toggle-header', function(e){
        $content = $($(this).data('target'));
        if($content.hasClass('collapse')){
          $content.removeClass('collapse');
          $(this).find('i.toggle-icon').html('keyboard_arrow_up')
        }
        else{
          $content.addClass('collapse');
          $(this).find('i.toggle-icon').html('keyboard_arrow_down')
        }
      });

});
