var lmsModule = angular.module("libraryApp",["ngRoute","ui.bootstrap.modal","multipleSelect",
                                             "angularUtils.directives.dirPagination","ngMaterial","ngMessages","chart.js"]);

lmsModule.config(["$routeProvider",function($routeProvider){
	return $routeProvider.when('/',{
		redirectTo:"/home"
	}).when("/home",{
		templateUrl:"home.html"
	}).when("/admin",{
		templateUrl:"admin.html"
	}).when("/addAuthor",{
		templateUrl:"addAuthor.html"
	}).when("/viewAuthor",{
		templateUrl:"viewAuthors.html"
	}).when("/viewBooks",{
		templateUrl:"viewBooks.html"
	}).when("/viewBookLoan",{
		templateUrl:"viewBookLoan.html"
	}).when("/viewBookCopies",{
		templateUrl:"viewBookCopies.html"
	}).when("/viewBookLoanAdmin",{
		templateUrl:"viewBookLoanAdmin.html"
	}).when("/test",{
		templateUrl:"TestMaterials.html"
	})
}])

