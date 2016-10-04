lmsModule.controller("bookController", function($rootScope, $scope, $http,
		$window, lmsRestService, lmsConstants) {


	
	$scope.showSuccessAlert = true;
	$scope.switchBool = function(value) {
		$scope[value] = !$scope[value];
	};

	/** ***********************Get Pagination data********************* */
	$scope.loadPaginationdata = function(searchString) {
		lmsRestService.get(
				lmsConstants.BOOK_GET_PAGINATION_DATA + searchString, function(
						num) {
					$scope.number = num;
					$scope.getNumber = function(num) {
						return new Array(num);
					}
				});
	}

	
	$scope.loadPaginationdata("null");
	$scope.currentPageNumber = 1;

	
	/** **********************Highlighting the Cells *********************** */
	$scope.highlight = function(i) {
		lmsRestService.highlight(i);
	}
	/** **********************On Change Event ********************* */
	$scope.onChangeBook = function() {
		var searchString=$scope.searchString;
		if (!searchString) {
			searchString = 'null';
		} else if (searchString == '') {
			searchString = 'null';
		}
		$scope.loadPaginationdata(searchString);
		$scope.getBooksData(searchString, $scope.currentPageNumber);
	}

	$scope.goToPage = function(i, searchString) {
		var url = '';
		if (!searchString) {
			searchString = 'null';
		} else if (searchString == '') {
			searchString = 'null';
		}
		url = lmsConstants.BOOK_GET_FIRST_PAGE + searchString + '/' + i;
		lmsRestService.get(url, function(data) {
			$scope.booksList = data;
			$scope.currentPageNumber = i;
		});
	}

	/** ************************Get Authors data*********************** */
	$scope.getBooksData = function(searchString, pageNo) {
		lmsRestService.get(lmsConstants.BOOK_GET_FIRST_PAGE + searchString
				+ "/" + pageNo, function(data) {
			$scope.booksList = data;
			lmsRestService.highlight(0);
		});
	}

	$scope.getBooksData(null, 1);

	/** ***********************Add Author************************* */
	// Get All the List of Publishers
	$scope.listPublishers = function() {
		lmsRestService.get(lmsConstants.GET_ALL_PUBLISHERS, function(data) {
			$scope.pubList = data;
		});
	}

	// Get all the list of Authors
	$scope.listAuthors = function() {
		lmsRestService.get(lmsConstants.GET_ALL_AUTHORS, function(data) {
			if(data!=null){
				$scope.authorList = data;
			}else{
				$scope.authorList = [];
			}
			
		});
	}

	$scope.resetModalFields=function(){
		$scope.genresList = [];
		$scope.authorList = [];
		$scope.selGenresList = [];
		$scope.selAuthorList = [];
		$scope.selPubList = '';
		$scope.newName = '';
	}
	
	// get All the List of Genres
	$scope.listGenres = function() {
		lmsRestService.get(lmsConstants.GET_ALL_GENRES, function(data) {
			if(data!=null){
				$scope.genresList = data;
			}else{
				$scope.genresList = [];
			}
		});
	}

	$scope.showAddBook = function() {
		$scope.listPublishers();
		$scope.listAuthors();
		$scope.listGenres();
		$scope.addBookModal = true;
	}

	$scope.closeAddBook = function() {
		$scope.resetModalFields();
		$scope.addBookModal = false;
	}

	$scope.addBook = function() {
		$scope.showSuccessAlert = true;
		var book = {
			"title" : $scope.newName,
			"genresList" : $scope.selGenresList,
			"authorList" : $scope.selAuthorList,
			"publisher" : {
				"id" : $scope.selPubList
			}
		}
		lmsRestService.post(lmsConstants.BOOK_POST_ADD, book, function(data) {
			$scope.message = data;
			console.log($scope.message);
			$scope.loadPaginationdata("null");
			$scope.getBooksData(null, 1);
			$scope.closeAddBook();
		})

	}

	/** **********************Update Author*********************** */
	$scope.showEditBook = function(bookId) {
		lmsRestService.get(lmsConstants.BOOK_GET_BY_ID + bookId,
				function(data) {
					$scope.listPublishers();
					$scope.listAuthors();
					$scope.listGenres();
					$scope.book = data;
					if($scope.book.authorList==null){
						$scope.book.authorList=[];
					}
					if($scope.book.genresList==null){
						$scope.book.genresList=[];
					}
					$scope.editBookModal = true;
				});
	}

	$scope.closeEditBook = function(bookId) {
		$scope.resetModalFields();
		$scope.editBookModal = false;
	};

	$scope.updateBook = function() {
		// id,genresList,title,authorList
		$scope.showSuccessAlert = true;
		var book = {
			id : $scope.book.id,
			title : $scope.book.title,
			authorList : $scope.book.authorList,
			genresList : $scope.book.genresList,
			publisher : {
				id : $scope.book.publisher.id
			}
		};
		lmsRestService.post(lmsConstants.BOOK_POST_UPDATE, book,
				function(data) {
					$scope.message = data;
					$scope.getBooksData(null, 1);
					$scope.genresList = [];
					$scope.authorList = [];
					$scope.editBookModal = false;
				})
	}

	/** **********************Delete Author********************* */

	$scope.showDeleteBook = function(bookId) {
		lmsRestService.get(lmsConstants.BOOK_GET_BY_ID + bookId,
				function(data) {
					$scope.book = data;
					$scope.deleteBookModal = true;
				});
	}

	$scope.closeDeleteBook = function() {
		$scope.deleteBookModal = false;
	};

	$scope.deleteBook = function() {
		$scope.showSuccessAlert = true;
		lmsRestService.post(lmsConstants.BOOK_POST_DELETE, $scope.book, function(
				data) {
						$scope.message = data;
						$scope.loadPaginationdata("null");
						$scope.getBooksData(null, 1);
						$scope.deleteBookModal = false;
						$window.scrollTo(0, 0);
		})
	}

})