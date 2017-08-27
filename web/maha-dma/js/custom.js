var ulbsList=[];
var servicesList=[];
var groupByModuleServices=[];

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

		var ulbOptionTemplate = Handlebars.compile($('#ulb-option-template').html());
    var documentsTempalte = Handlebars.compile($('#documents-body-template').html());
    var servicesMenuTempalte = Handlebars.compile($('#services-menu-template').html());

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
			  url: 'data/ulbsList.json',
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

      $.ajax({
  			  url: 'data/servicesList.json',
  				data:{ "_": $.now() },
  				headers: {
  				   'Cache-Control': 'max-age=1000'
  				},
  			  success: function(response){
            servicesList = response;
            groupByModuleServices = arrayGroupByKey(servicesList, "moduleName");
            console.log('groupByModuleServices', groupByModuleServices);
            loadServiceMenus();
  			  },
  			  cache: false
  			});

      function loadUlbsList(){
        $('#ulb-dropdown').html(ulbOptionTemplate(ulbsList));
      }

      function loadServiceMenus(){
        console.log('calling!');
        console.log('output', servicesMenuTempalte(groupByModuleServices));
        $('.service-table').html(servicesMenuTempalte(groupByModuleServices));
      }

      $(document).on('click', 'a.action-item', function(e){
          //console.log('serviceName', $(this).data('service-name'));

          if(!$('#ulb-dropdown').val()){
            $('#ulb-dropdown').popover('show');
            return;
          }
          else{
            $('#ulb-dropdown').popover('hide');
          }

          var serviceName = $(this).data('service-name');
          var moduleName = $(this).data('module-name');
          $('#servicesDetailModalLabel').html($(this).data('service-name')+" - "+moduleName);

          var service = servicesList.find((service) => service.serviceName === serviceName
                    && service.moduleName === moduleName);
          var ulb = ulbsList.find((ulb)=>ulb.ulbName === $('.ulb-dropdown').val());

          if(service){
            var uniqueKeys = service.slaTable.columns.reduce(function (acc, obj) {
                return acc.concat(acc.indexOf(obj.key) === -1? obj.key : undefined);
            }, []);

            console.log('uniqueKeys', uniqueKeys);

            service['uniqueKeys'] = uniqueKeys;

            //documentsTempalte
            $('#apply-btn').attr('data-url', ulb.url+service.redirectUrl);
            $('#documents-body').html(documentsTempalte(service));
            $('#servicesDetailModal').modal('show');
          }
          else{
            //else TODO
          }
      });

      $('#apply-btn').click(function(e){
          var serviceRedirectUrl=$(this).attr('data-url');
          if(serviceRedirectUrl){
            $('#servicesDetailModal').modal('hide');
            window.open(serviceRedirectUrl);
          }
      });


});
