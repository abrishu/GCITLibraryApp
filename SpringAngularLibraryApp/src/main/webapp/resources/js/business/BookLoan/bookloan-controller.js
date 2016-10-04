lmsModule.controller("bookloanController",function($scope,$http,$window,lmsRestService,lmsConstants){
	
	 $scope.currentPageNumber=0;
	 $scope.showSuccessAlert = true;
	 $scope.switchBool = function (value) {
	        $scope[value] = !$scope[value];
	 };
	 
	 
	 //Validations and get checked-out books
	 $scope.validateBorrower=function(){
		 lmsRestService.get(lmsConstants.VALIDATE_BORROWER+$scope.cardNumber,function(data){
			 $scope.validBorrower=data;
			 $scope.loadPaginationdata();
			 $scope.getBookCopiesData();
			 $scope.getCheckedOutBooksForABorrower();
		 }); 
	 }
	
	 
	 $scope.getCheckedOutBooksForABorrower=function(){
		 lmsRestService.get(lmsConstants.BOOKLOAN_RETURN_INFO+$scope.cardNumber,function(data){
			 $scope.checkedOutBooks=data;
		 });
	 }
	 
	 
	 $scope.checkValidity=function(){
		 if($scope.cardNumber!=''){
			 if($scope.validBorrower){
				 if($scope.validBorrower.cardNo){
					 return true;
				 }else{
					 return false;
				 }
			 }
		 }else{
			 return false;
		 }
	 }
	 
	 
	 
	 
	 
	 
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
	 
	 
	 $scope.checkOutReturn=function(bookcopy){
				if (!$scope.checkedOutBooks || $scope.checkedOutBooks == null) {
					$scope.buttonDef="success";
					return false;
				} else {
					if ($.grep($scope.checkedOutBooks, function(n,i) {
						return n.book.id ==bookcopy.book.id && n.branch.id ==bookcopy.branch.id
					}) == '') {
						$scope.buttonDef="success";
						return false;
					} else {
						$scope.buttonDef="danger";
						return true;
					}
		 }
	 }
	 
	$scope.performReturn=function(bookCopy){
		$scope.showSuccessAlert = true;
		$scope.$emit('LOAD');
		var bookLoan={book:bookCopy.book,branch:bookCopy.branch,bor:{
			cardNo:$scope.cardNumber
		}};
		lmsRestService.post(lmsConstants.BOOKLOAN_RETURN_POST ,bookLoan,function(data){
			$scope.$emit('UNLOAD')
			$scope.message=data;
			$scope.getCheckedOutBooksForABorrower();
			$scope.getBookCopiesData();
			$scope.closeModal();
		});
		
	};
	
	$scope.performCheckout=function(bookCopy){
		$scope.showSuccessAlert = true;
		$scope.$emit('LOAD');
		var bookLoan={book:bookCopy.book,branch:bookCopy.branch,bor:{
			cardNo:$scope.cardNumber
		}};
		lmsRestService.post(lmsConstants.BOOKLOAN_CHECKOUT_POST ,bookLoan,function(data){
			$scope.$emit('UNLOAD')
			$scope.message=data;
			$scope.getCheckedOutBooksForABorrower();
			$scope.getBookCopiesData();
			$scope.closeModal();
		});
	}
	 
	$scope.showModal=function(bookCopy,type){
		$scope.bookcopy=bookCopy;
		$scope.type=type;
		$scope.bookLoanModal=true;
	}
	
	$scope.closeModal=function(){
		$scope.bookcopy=[];
		$scope.bookLoanModal=false;
	}
	 

	$scope.isReturn=function(){
		return ($scope.type=='return');
	}
	
	
	$scope.onChangeSearch=function(){
		$scope.bookString=lmsRestService.frameSearchString($scope.searchStringBook);
	 	$scope.branchString=lmsRestService.frameSearchString($scope.searchStringBranch);
	 	console.log($scope.searchStringBranch);
		$scope.loadPaginationdata();
		$scope.getBookCopiesData();
		
	}
	
	$scope.showBorrowerInfoModal=function(){
		$scope.borrowerInfoModal=true;
	}
	
	$scope.closeBookInfoModal=function(){
		$scope.borrowerInfoModal=false;
	}
	
	
	$scope.updateBorrowerInfo=function(){
		$scope.showSuccessAlert = true;
		lmsRestService.post(lmsConstants.BORROWER_UPDATE_POST,$scope.validBorrower,function(data){
			$scope.message=data;
			$scope.closeBookInfoModal();
		})
	}
	
}).	controller('loadingController',['$scope',function($scope){
	$scope.$on('LOAD',function(){ $scope.loading=true;});
	$scope.$on('UNLOAD',function(){$scope.loading=false});
}]);