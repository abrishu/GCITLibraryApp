lmsModule.controller("libController",function($scope,$http,$window,lmsRestService,lmsConstants){
	
	 $scope.currentPageNumber=0;
	 $scope.showSuccessAlert = true;
	 $scope.switchBool = function (value) {
	        $scope[value] = !$scope[value];
	 };
	
	 
	 //Getting Pagination
	 $scope.loadPaginationdata=function(){
		 	$scope.bookString=lmsRestService.frameSearchString($scope.searchStringBook);
		 	$scope.branchString=lmsRestService.frameSearchString($scope.searchStringBranch);
			lmsRestService.get(lmsConstants.BOOKLOAN_GET_PAGINATION_DATA + $scope.branchString+"/"+$scope.bookString ,function(num){
				$scope.number = num;
				$scope.getNumber = function(num) {
				    return new Array(num);   
				}
			});
	 }
	 
	 
	 $scope.goToPage=function(i){
				$scope.currentPageNumber=i;
				$scope.getBookCopiesData();
	 }
	 
	 //Highlighting
	 
	 $scope.highlight=function(i){
			lmsRestService.highlight(i);
	 }
	 
	 //Get Book Copies Data
	 $scope.getBookCopiesData=function(){
		 	$scope.$emit('LOAD');
		 	setTimeout(function(){ lmsRestService.get(lmsConstants.BOOKLOAN_SEARCH_BOOK_COPIES + $scope.branchString+"/"+$scope.bookString+"/"+($scope.currentPageNumber+1),function(data){
				$scope.$emit('UNLOAD');
				$scope.bookCopiesList=data;
				if(!$scope.currentPageNumber){
					$scope.highlight(0);
				}else{
					$scope.highlight($scope.currentPageNumber);
				}
			}); }, 0);
			
	 }
	 
	 
	$scope.loadPaginationdata();
	$scope.getBookCopiesData();
	 
	$scope.showEditModal=function(bookCopy){
		$scope.bookcopy=bookCopy;
		$scope.editModal=true;
	}
	
	$scope.closeEditModal=function(){
		$scope.bookcopy=[];
		$scope.editModal=false;
	}
	
	$scope.onChangeSearch=function(){
		$scope.bookString=lmsRestService.frameSearchString($scope.searchStringBook);
	 	$scope.branchString=lmsRestService.frameSearchString($scope.searchStringBranch);
	 	console.log($scope.searchStringBranch);
		$scope.loadPaginationdata();
		$scope.getBookCopiesData();
		
	}
	
	
	$scope.updateBookCopies=function(){
		lmsRestService.post(lmsConstants.BOOKCOPY_UPDATE_POST,$scope.bookcopy,function(data){
			$scope.message=data;
			$scope.loadPaginationdata();
			$scope.getBookCopiesData();
		});
	}
	
	$scope.updateBranchDetails=function(){
		lmsRestService.post(lmsConstants.BRANCHDETAILS_UPDATE_POST,$scope.bookcopy.branch,function(data){
			$scope.updateBookCopies();
		});
	}
	
	$scope.performUpdate=function(){
		$scope.updateBranchDetails();
		$scope.editModal=false;
	}
	
	$scope.performAdd=function(){
		$scope.bookcopy={book:$scope.book,branch:$scope.branch,noOfCopies:$scope.noOfCopies};
		$scope.updateBookCopies();
		$scope.addModal=false;
	}
	
	$scope.getAllBranches=function(){
		lmsRestService.get(lmsConstants.BRANCH_GET_ALL,function(data){
			$scope.branchList=data;
		});
	}
	
	$scope.getAllBooks=function(){
		lmsRestService.get(lmsConstants.GET_ALL_BOOKS,function(data){
			$scope.bookList=data;
		});
	}
	
	$scope.getBookCopiesInfo=function(){
		lmsRestService.get(lmsConstants.GET_BOOKCOPIES_INFO+$scope.book.id+"/"+$scope.branch.id,function(data){
			if(!data){
				$scope.noOfCopies=0;
			}else{
				$scope.noOfCopies=data.noOfCopies;
				console.log($scope);
			}
		});
	}
	
	$scope.showAddModal=function(){
		//Get the list of Branches from Backend
		$scope.getAllBranches();
		$scope.addModal=true;
	}
	
	$scope.onBranchChange=function(){
		$scope.noOfCopies='';
		$scope.getAllBooks();
	}
	
	$scope.onBookChange=function(){
		//Get No of Copies present for that branch
		$scope.getBookCopiesInfo();
	}
	
	$scope.closeAddModal=function(){
		$scope.addModal=false;
	}
	
	
	
	
	
}).	controller('loadingController',['$scope',function($scope){
	$scope.$on('LOAD',function(){ $scope.loading=true;});
	$scope.$on('UNLOAD',function(){$scope.loading=false});
}]);