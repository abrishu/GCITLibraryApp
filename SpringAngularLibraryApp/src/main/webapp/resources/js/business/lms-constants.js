lmsModule.constant("lmsConstants",{
	AUTHOR_GET:"http://localhost:8085/library/getAllAuthorsRest/",
	AUTHOR_GET_FIRST_PAGE:"http://localhost:8085/library/searchAuthors/",
	AUTHOR_GET_PAGINATION_DATA:"http://localhost:8085/library/paginationAuthors/",
	AUTHOR_GET_BY_ID:"http://localhost:8085/library/getAuthorById/",
	AUTHOR_POST:"http://localhost:8085/library/addAuthorRest/",
	AUTHOR_POST_UPDATE:"http://localhost:8085/library/updateAuthorRest/",
	AUTHOR_POST_DELETE:"http://localhost:8085/library/deleteAuthorRest/",
	
	GET_ALL_AUTHORS:"http://localhost:8085/library/getAllAuthorsRest/",
	GET_ALL_PUBLISHERS:"http://localhost:8085/library/getAllPublishers/",
	GET_ALL_GENRES:"http://localhost:8085/library/getAllGenres/",
	
	BOOK_GET_FIRST_PAGE:"http://localhost:8085/library/searchBooksRest/",
	BOOK_GET_PAGINATION_DATA:"http://localhost:8085/library/paginationBooks/",
	BOOK_GET_BY_ID:"http://localhost:8085/library/getBookById/",
	BOOK_POST_ADD:"http://localhost:8085/library/addBookRest/",
	BOOK_POST_UPDATE:"http://localhost:8085/library/updateBookRest/",
	BOOK_POST_DELETE:"http://localhost:8085/library/deleteBookRest/",
	
	VALIDATE_BORROWER:"http://localhost:8085/library/validateBorrowerRest/",
	BOOKLOAN_GET_PAGINATION_DATA:"http://localhost:8085/library/paginationBookLoans/",
	BOOKLOAN_SEARCH_BOOK_COPIES:"http://localhost:8085/library/searchBookCopies/",
	BOOKCOPIES_INFO:"http://localhost:8085/library/getBookCopiesInfo/",
	BOOKLOAN_RETURN_INFO:"http://localhost:8085/library/getReturnInfo/",
	BOOKLOAN_CHECKOUT_POST:"http://localhost:8085/library/performCheckOut/",
	BOOKLOAN_RETURN_POST:"http://localhost:8085/library/performReturn/",
	
	BOOKCOPY_UPDATE_POST:"http://localhost:8085/library/addBookCopiesToBranch/",
	BRANCHDETAILS_UPDATE_POST:"http://localhost:8085/library/updateBranchDetails/",
	
	BRANCH_GET_ALL:"http://localhost:8085/library/getAllBranches/",
	GET_ALL_BOOKS:"http://localhost:8085/library/getAllBooksRest/",
	GET_BOOKCOPIES_INFO:"http://localhost:8085/library/getBookCopiesInfo/",
	
	BOOKLOAN_GET_CHECKEDOUT:"http://localhost:8085/library/getAllCheckedOutBooks/",
	
	BOOKLOAN_OVERRIDE:"http://localhost:8085/library/overrideBookLoan/",
	
	BORROWER_UPDATE_POST:"http://localhost:8085/library/updateBorrowerInfo/",
})