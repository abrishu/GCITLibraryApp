lmsModule.service("lmsRestService",function($http){
	
	this.get=function(url,callback){
		$http.get(url).success(callback);
	};
	
	this.post=function(url,obj,callback){
		$http.post(url,obj)
		.success(callback)
	};
	
	this.getJSDate=function(str){
		var date = new Date();
		var dateArray = str.split("-");
		date.setFullYear(parseInt(dateArray[0]));
		date.setMonth(parseInt(dateArray[1])-1);  // months indexed as 0-11, substract 1
		date.setDate(parseInt(dateArray[2])); // setDate sets the month of day 
		return date;
	}
	
	this.frameSearchString=function(searchString){
		if (!searchString) {
			searchString = 'null';
		} else if (searchString == '') {
			searchString = 'null';
		}
		return searchString;
	};
	
	
	
	this.highlight=function(i){
		if($('.pagination li')[i]!=null){
			$('.pagination li').each(function(){
				this.setAttribute("class","inactive")
			});
			$('.pagination li')[i].setAttribute("class","active")	
		}
	}
	
});