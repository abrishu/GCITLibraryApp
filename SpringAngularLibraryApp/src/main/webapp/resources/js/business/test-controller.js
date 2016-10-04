lmsModule.controller("testCtrl",['$mdToast','$scope', '$timeout',function($mdToast,$scope,$timeout){
	this.var1="hello";
	this.var2=" world";
	this.var3=" this is me";
	var4="  this is kakesh";
	
	this.showMeSomething=function(){
		this.var3=var4;
	}
	
	this.showToast1== function($event) {
		alert('asdasd');
	    $mdToast.show($mdToast.simple().textContent('Hello!'));
	  };
	  
	  $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
	  $scope.series = ['Series A', 'Series B'];
	  $scope.data = [
	    [65, 59, 80, 81, 56, 55, 40],
	    [28, 48, 40, 19, 86, 27, 90]
	  ];
	  $scope.onClick = function (points, evt) {
	    console.log(points, evt);
	  };

	  // Simulate async data update
	  $timeout(function () {
	    $scope.data = [
	      [28, 48, 40, 19, 86, 27, 90],
	      [65, 59, 80, 81, 56, 55, 40]
	    ];
	  }, 6000);
}]);