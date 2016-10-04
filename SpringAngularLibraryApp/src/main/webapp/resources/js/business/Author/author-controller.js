
lmsModule.controller("authorController",function($rootScope,$scope,$http,$window,lmsRestService,lmsConstants){
	
	 $scope.showSuccessAlert = true;
	 $scope.switchBool = function (value) {
	        $scope[value] = !$scope[value];
	 };
	
	
	/*************************Get Pagination data**********************/
	$scope.loadPaginationdata=function(searchString){
		lmsRestService.get(lmsConstants.AUTHOR_GET_PAGINATION_DATA + searchString ,function(num){
			$scope.number = num;
			$scope.getNumber = function(num) {
			    return new Array(num);   
			}
		});
	}
	
	
	$scope.loadPaginationdata("");
	$scope.currentPageNumber=1;
	
	/************************Highlighting the Cells ************************/
	$scope.highlight=function(i){
		lmsRestService.highlight(i);
	}
	/************************On Change Event **********************/
	$scope.onChangeAuthor=function(){
		$scope.loadPaginationdata($scope.searchString);
		$scope.getAuthorsData($scope.searchString,$scope.currentPageNumber);
	}
	
	
	$scope.goToPage=function(i){
		lmsRestService.get(lmsConstants.AUTHOR_GET_FIRST_PAGE + i,function(data){
			$scope.authorList=data;
			$scope.currentPageNumber=i;
		});
	}
	
	
	/** ************************Get Authors data*********************** */
	$scope.getAuthorsData=function(searchString,pageNo){
		lmsRestService.get(lmsConstants.AUTHOR_GET_FIRST_PAGE + searchString+"/"+pageNo,function(data){
			$scope.authorList=data;
			lmsRestService.highlight(0);
		});
	}
	
	$scope.getAuthorsData("",1);
	
	/*************************Add Author**************************/
	$scope.addAuthor=function(){
		$scope.showSuccessAlert = true;
		var author={
				name:$scope.authorName};
			lmsRestService.post(lmsConstants.AUTHOR_POST,author,function(data){
				$rootScope.message=data;
				$window.location.href="#/viewAuthor";
			})
	}
	
	
	/************************Update Author************************/
	$scope.showEditAuthor = function(authorId){
		lmsRestService.get(lmsConstants.AUTHOR_GET_BY_ID + authorId,function(data){
			$scope.author=data;
			$scope.editAuthorModal=true;
		});
	}
	
	$scope.closeEditAuthor = function(authorId){
		$scope.editAuthorModal=false;
	};
	
	$scope.updateAuthor=function(){
		$scope.showSuccessAlert = true;
		lmsRestService.post(lmsConstants.AUTHOR_POST_UPDATE,$scope.author,function(data){
			$scope.message=data;
			$scope.getAuthorsData("",1);
			$scope.editAuthorModal=false;
		})
	}
	
	/************************Delete Author**********************/
	
	$scope.showDeleteAuthor = function(authorId){
		lmsRestService.get(lmsConstants.AUTHOR_GET_BY_ID + authorId,function(data){
			$scope.author=data;
			$scope.deleteAuthorModal=true;
		});
	}
	
	$scope.closeDeleteAuthor = function(authorId){
		$scope.deleteAuthorModal=false;
	};
	
	$scope.deleteAuthor=function(){
		$scope.showSuccessAlert = true;
		var author={id:$scope.author.id};
		lmsRestService.post(lmsConstants.AUTHOR_POST_DELETE,author,function(data){
			$scope.message=data;
			$scope.getAuthorsData("",1);
			$scope.deleteAuthorModal=false;
			$window.scrollTo(0,0);
		})
	}
	
})

