package com.gcitsolutions.libraryapp.Servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Service.BorrowerService;

/**
 * Servlet implementation class BorrowerServlet
 */
@WebServlet(name="/BorrowerServlet",urlPatterns={
		"/validateBorrower","/showCheckOut","/showReturn","/getReturnInfo","/performCheckOut","/performReturn",
		"/getAvailableBooksForABranch","/returnBook"
})
public class BorrowerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BorrowerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		if(request.getServletPath().equals("/validateBorrower")){
			response.setContentType("application/json");
			
			try {
				Borrower bor = validateBorrower(request);
				String jsonBor=mapper.writeValueAsString(bor);
				response.getWriter().write(jsonBor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(request.getServletPath().equals("/getAvailableBooksForABranch")){
			LibraryBranch branch=new LibraryBranch(Integer.parseInt(request.getParameter("branchId")));
			try {
				List<Book> bookList= getAvailableBooksForABranch(branch);
				response.setContentType("application/json");
				String jsonBookList = mapper.writeValueAsString(bookList);
				response.getWriter().write(jsonBookList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(request.getServletPath().equals("/getReturnInfo")){
			
			try {
				List<BookLoan> bookLoans=showReturnInfo(request);
				response.setContentType("application/json");
				String jsonBookLoanList = mapper.writeValueAsString(bookLoans);
				response.getWriter().write(jsonBookLoanList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(request.getServletPath().equals("/returnBook")){
			
			try {
				response.setContentType("text");
				response.getWriter().write(performReturn(request));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			doPost(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String function = request.getServletPath();
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
		try {
			switch (function) {
			case "/showCheckOut": {
				showCheckOutInfo(request);
				rd = getServletContext().getRequestDispatcher("/checkOut.jsp");
				break;
			}	
			case "/performCheckOut": {
				checkOut(request);
				request.setAttribute("result", "Book checkedout Succesfully!");
				break;
			}	
			case "/showReturn": {
				rd = getServletContext().getRequestDispatcher("/returnBook.jsp");
				break;
			}	
			case "/performReturn": {
				performReturn(request);
				request.setAttribute("result", "Book returned Succesfully!");
				break;
			}	
			default:
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("result", "Operation Failed! " + e.getMessage());
		}

		rd.forward(request, response);
	}
	
	protected List<Book> getAvailableBooksForABranch(LibraryBranch br) throws Exception{
		List<Book> bookList=BorrowerService.getInstance().getAvailableBooksForABranch(br);
		return bookList;
	}
	
	protected Borrower validateBorrower(HttpServletRequest request) throws Exception{
		Borrower bor=new Borrower(Integer.parseInt(request.getParameter("cardNum")));
		bor=BorrowerService.getInstance().validateBorrower(bor);
		return bor;
	}
	
	protected void showCheckOutInfo(HttpServletRequest request) throws Exception{
		List<LibraryBranch> branchList=BorrowerService.getInstance().getAllBranches();
		request.setAttribute("branchList", branchList);
	}
	
	protected void checkOut(HttpServletRequest request) throws Exception{
		Borrower bor=new Borrower(Integer.parseInt(request.getParameter("cardNum")));
		LibraryBranch branch=new LibraryBranch(Integer.parseInt(request.getParameter("branchId")));
		Book book=new Book(Integer.parseInt(request.getParameter("bookId")));
		BookLoan bl=new BookLoan(book,branch,bor);
		BorrowerService.getInstance().performCheckOut(bl);
	}
	
	
	protected List<BookLoan> showReturnInfo(HttpServletRequest request) throws Exception{
		Borrower bor=new Borrower(Integer.parseInt(request.getParameter("cardNum")));
		return BorrowerService.getInstance().getCheckedOutBooks(bor);
	}

	protected String performReturn(HttpServletRequest request) throws Exception{
		Borrower bor=new Borrower(Integer.parseInt(request.getParameter("cardNo")));
		LibraryBranch branch=new LibraryBranch(Integer.parseInt(request.getParameter("branchId")));
		Book book=new Book(Integer.parseInt(request.getParameter("bookId")));
		BookLoan bl=new BookLoan(book, branch, bor);
		if(BorrowerService.getInstance().performReturn(bl)){
			return "Return successful";
		}else{
			return "Return failure";
		}
	}
	
	


}
