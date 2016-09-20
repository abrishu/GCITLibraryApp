package com.gcitsolutions.libraryapp.Servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.gcitsolutions.libraryapp.Data.BookDAO;
import com.gcitsolutions.libraryapp.Data.ConnectionUtils;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Service.Administrator;
import com.gcitsolutions.libraryapp.Service.Librarian;

/**
 * Servlet implementation class LibrarianServlet
 */
@WebServlet(name = "/LibrarianServlet", urlPatterns = { "/updateLibraryBranch", "/showLibraryBranch",
		"/showAddBookCopies", "/getBookCopiesInfo", "/addBookCopies", "/searchBookCopies", "/paginationBookCopies" })
public class LibrarianServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LibrarianServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		if (request.getServletPath().equals("/getBookCopiesInfo")) {
			getNoOfCopies(request, response);
		} else if (request.getServletPath().equals("/searchBookCopies")) {
			try {
				searchBookCopies(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (request.getServletPath().equals("/paginationBookCopies")) {
			try {
				paginateBookCopies(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			doPost(request, response);
		}

	}
	
	private String replaceNullWithEmpty(String data){
		if(data==null){
			return "";
		}else{
			return data;
		}
	}


	private void paginateBookCopies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String searchStringBranch =replaceNullWithEmpty(request.getParameter("searchStringBranch"));
		String searchStringBook = replaceNullWithEmpty(request.getParameter("searchStringBook"));
		Integer pageCount = 0;
		Integer noOfRecords = 0;
		noOfRecords = Librarian.getInstance().getCountOfBookCopies(searchStringBranch, searchStringBook);
		Integer pageSize = 10;
		if (noOfRecords % 10 == 0) {
			if ((noOfRecords / pageSize) == 0) {
				pageCount = 1;
			} else {
				pageCount = (noOfRecords / pageSize);
			}
		} else {
			pageCount = (noOfRecords / pageSize) + 1;
		}
		response.getWriter().write(pageCount.toString());

	}

	private void searchBookCopies(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<BookCopies> bookCopiesList = new ArrayList<BookCopies>();
		String searchStringBranch = request.getParameter("searchStringBranch");
		String searchStringBook = request.getParameter("searchStringBook");
		Integer pageNo = 1;
		if (request.getParameter("pageNo") != null) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		ObjectMapper mapper = new ObjectMapper();
		bookCopiesList = Librarian.getInstance().getAllBookCopies(pageNo, 10, searchStringBranch, searchStringBook);
		String jsonBooks = mapper.writeValueAsString(bookCopiesList);
		response.setContentType("application/json");
		response.getWriter().write(jsonBooks);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String function = request.getServletPath();
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
		try {
			switch (function) {

			case "/showAddBookCopies": {
				showLibraryBranch(request);
				showBooks(request);
				rd = getServletContext().getRequestDispatcher("/addBookCopies.jsp");
				break;
			}

			case "/addBookCopies": {
				addBookCopies(request);
				request.setAttribute("result", "Book copies succesfully added!");
				break;
			}

			case "/getBookCopiesInfo": {
				updateLibraryBranch(request);
				request.setAttribute("result", "Books added Succesfully!");
				break;
			}
			case "/showLibraryBranch": {
				showLibraryBranch(request);
				rd = getServletContext().getRequestDispatcher("/updateLibraryBranch.jsp");
				break;
			}

			case "/updateLibraryBranch": {
				updateLibraryBranch(request);
				request.setAttribute("result", "Branch updated Succesfully!");
				break;
			}

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("result", "Operation Failed! " + e.getMessage());
		}

		rd.forward(request, response);
	}

	private void showLibraryBranch(HttpServletRequest request) throws Exception {
		List<LibraryBranch> branchList = Administrator.getInstance().getAllLibraryBranch();
		request.setAttribute("branchList", branchList);
	}

	private void showBooks(HttpServletRequest request) throws Exception {
		List<Book> bookList = Administrator.getInstance().getAllBooks();
		request.setAttribute("bookList", bookList);
	}

	private void getNoOfCopies(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BookCopies bcop = new BookCopies();
		bcop.setBook(new Book(Integer.parseInt(request.getParameter("bookId"))));
		bcop.setBranch(new LibraryBranch(Integer.parseInt(request.getParameter("branchId"))));
		try {
			bcop = Librarian.getInstance().getBookCopiesForABook(bcop);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bcop != null) {
			response.getWriter().write(String.valueOf(bcop.getNoOfCopies()));
		} else {
			response.getWriter().write(0);
		}

	}

	private void addBookCopies(HttpServletRequest request) throws Exception {
		BookCopies bcop = new BookCopies();
		bcop.setBook(new Book(Integer.parseInt(request.getParameter("bookId"))));
		bcop.setBranch(new LibraryBranch(Integer.parseInt(request.getParameter("branchId"))));
		bcop.setNoOfCopies(Integer.parseInt(request.getParameter("addCopies")));
		Librarian.getInstance().addBookCopiesToBranch(bcop);
	}

	private void updateLibraryBranch(HttpServletRequest request) throws Exception {
		LibraryBranch branch = new LibraryBranch();
		branch.setId(Integer.parseInt(request.getParameter("selBranch")));
		branch.setName(request.getParameter("name"));
		branch.setAddress(request.getParameter("address"));
		Administrator.getInstance().updateBranchInfo(branch);
	}

}
