lmsModule.controller("bookloanAdminController",function($scope,$http,$window,lmsRestService,lmsConstants){
	 $scope.showSuccessAlert = true;
	 $scope.switchBool = function (value) {
	        $scope[value] = !$scope[value];
	 };
	 
	 /*Start : Date related settings*/
	 $scope.today=new Date();
	

	  $scope.minDate = new Date(
	      $scope.today.getFullYear(),
	      $scope.today.getMonth(),
	      $scope.today.getDate());
	 
	  /*End : Date related settings*/
	  
	 $scope.getBookLoanData=function(){
		 	$scope.$emit('LOAD');
		 	 lmsRestService.get(lmsConstants.BOOKLOAN_GET_CHECKEDOUT ,function(data){
				$scope.$emit('UNLOAD');
				$scope.bookLoanList=data;
		 	 });
	 }
	 
	 $scope.getBookLoanData();
	 
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
	 
	$scope.showModal=function(bookLoan){
		 $scope.bookLoan=bookLoan;
		 var date=$scope.bookLoan.dueDate;
		 $scope.dueDate=lmsRestService.getJSDate(date);
		 $scope.myDate = new Date( 
				  $scope.dueDate.getFullYear(),
			      $scope.dueDate.getMonth(),
			      $scope.dueDate.getDate()
		 )
		 $scope.bookLoanModal=true;
		 
	}
	
	$scope.closeModal=function(){
		$scope.bookcopy=[];
		$scope.bookLoanModal=false;
	}
	
	$scope.onChangeSearch=function(){
		$scope.bookString=lmsRestService.frameSearchString($scope.searchStringBook);
	 	$scope.branchString=lmsRestService.frameSearchString($scope.searchStringBranch);
	 	console.log($scope.searchStringBranch);
		$scope.loadPaginationdata();
		$scope.getBookCopiesData();
	}
	
	$scope.overrideLoan=function(){
		$scope.showSuccessAlert = true;
		$scope.bookLoan.dueDate=$scope.myDate;
		console.log($scope);
		lmsRestService.post(lmsConstants.BOOKLOAN_OVERRIDE,$scope.bookLoan ,function(data){
			$scope.message=data;
			$scope.getBookLoanData();
			$scope.bookLoanModal=false;
	 	 });
	}
	
	
}).	controller('loadingController',['$scope',function($scope){
	$scope.$on('LOAD',function(){ $scope.loading=true;});
	$scope.$on('UNLOAD',function(){$scope.loading=false});
}]);