package com.gcitsolutions.libraryapp.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Entity.Publisher;
import com.gcitsolutions.libraryapp.Service.Administrator;
import com.gcitsolutions.libraryapp.Service.BorrowerService;
import com.gcitsolutions.libraryapp.Service.Utility;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet(name="AdminServlet",
		urlPatterns={"/admin", "/addPublisher","/addLibraryBranch",
				"/addBorrower","/overrideDueDate",
				"/getBranchInfo","/getAllPublishers","/getAllAuthors",
				"/showPublishers","/getPublisherInfo","/updatePublisher","/showOverrideScreen",
				}
)
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		ObjectMapper mapper = new ObjectMapper();

		if(request.getServletPath().equals("/getBranchInfo")){
			int branchId=Integer.valueOf(Utility.encodeParameter(request.getParameter("branchId").toString()));
			try {
				LibraryBranch branch=Administrator.getInstance().getBranchInfo(new LibraryBranch(branchId));
				response.setContentType("application/json");
				String jsonBranch = mapper.writeValueAsString(branch);
				response.getWriter().write(jsonBranch);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getServletPath().equals("/getAllPublishers")){
			try {
				List<Publisher> pubList=Administrator.getInstance().getAllPublishers();
				response.setContentType("application/json");
				String jsonBranch = mapper.writeValueAsString(pubList);
				response.getWriter().write(jsonBranch);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getServletPath().equals("/getAllAuthors")){
			try {
				List<Author> authorList=Administrator.getInstance().getAllAuthors();
				response.setContentType("application/json");
				String jsonBranch = mapper.writeValueAsString(authorList);
				response.getWriter().write(jsonBranch);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (request.getServletPath().equals("/getPublisherInfo")){
			try {
				//getSelectedBookInfo(request,response);
				Administrator admin=Administrator.getInstance();
				Publisher pub =new Publisher(Integer.parseInt(Utility.encodeParameter(request.getParameter("publisherId").toString())));
				pub=admin.getPublisherInfo(pub);
				String jsonPub=mapper.writeValueAsString(pub);
				response.setContentType("application/json");
				response.getWriter().write(jsonPub);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		else if (request.getServletPath().equals("/getBookLoanInfo")){
			try {
				List<BookLoan> bookLoans=showBookLoanInfo(request);
				response.setContentType("application/json");
				String jsonBookLoanList = mapper.writeValueAsString(bookLoans);
				response.getWriter().write(jsonBookLoanList);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
			else{
			doPost(request,response);
		}
	}
	
	public void mapRequestToActions(HttpServletRequest request,HttpServletResponse response) throws Exception{ 

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
			
			/**Book**/
			
			/**Library Branch**/
			case "/addLibraryBranch":{
				addLibraryBranch(request);
				request.setAttribute("result", "Branch Added Succesfully!");
				break;
			}
			
			
			/**Publisher**/
			case "/addPublisher":{
				addPublisher(request);
				request.setAttribute("result", "Publisher Added Succesfully!");
				break;
			}
			
			case "/showPublishers":{
				getPublishers(request);
				rd = getServletContext().getRequestDispatcher("/updatePublisher.jsp");
				break;
			}
			
			case "/updatePublisher":{
				updatePublisher(request);
				request.setAttribute("result", "Publisher updated Succesfully!");
				break;
			}
			
			
			/**Borrower**/
			
			case "/addBorrower":{
				addBorrower(request);
				request.setAttribute("result", "Borrower Added Succesfully!");
				break;
			}
			
			
			/**Override Functionality**/
			case "/showOverrideScreen": {
				rd = getServletContext().getRequestDispatcher("/overrideLoan.jsp");
				break;
			}
			case "/overrideDueDate":{
				overrideDueDate(request);
				request.setAttribute("result", "Book was successfully overriden!");
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

	
	
	
	/*Library Branch Function*/
	private void addLibraryBranch(HttpServletRequest request) throws Exception {
		LibraryBranch branch=new LibraryBranch();
		branch.setName(Utility.encodeParameter(request.getParameter("name")));
		branch.setAddress(Utility.encodeParameter(request.getParameter("address")));
		Administrator.getInstance().addLibraryBranch(branch);
	}
	
	
	/**Publisher Functions*/

	private void getPublishers(HttpServletRequest request) throws Exception {
		Administrator admin=Administrator.getInstance();
		List<Publisher> pubList = admin.getAllPublishers();
		request.setAttribute("pubList", pubList);
	}
	
	
	private void addPublisher(HttpServletRequest request) throws Exception {
		Publisher pub=new Publisher();
		pub.setName(Utility.encodeParameter(request.getParameter("name")));
		pub.setAddress(Utility.encodeParameter(request.getParameter("address")));
		pub.setPhone(Utility.encodeParameter(request.getParameter("phone")));
		Administrator.getInstance().addPublisher(pub);
	}
	
	private void updatePublisher(HttpServletRequest request) throws Exception{
		Publisher pub= new Publisher();
		pub.setId(Integer.parseInt(Utility.encodeParameter(request.getParameter("publisherId").toString())));
		pub.setName(Utility.encodeParameter(request.getParameter("name").toString()));
		pub.setAddress(Utility.encodeParameter(request.getParameter("address").toString()));
		pub.setPhone(Utility.encodeParameter(request.getParameter("phone").toString()));
		Administrator.getInstance().updatePublisher(pub);
	}
	
	
	/**Borrower Functions*/
	private void addBorrower(HttpServletRequest request) throws Exception {
		Borrower bor=new Borrower();
		bor.setName(Utility.encodeParameter(request.getParameter("name")));
		bor.setAddress(Utility.encodeParameter(request.getParameter("address")));
		bor.setPhone(Utility.encodeParameter(request.getParameter("phone")));
		Administrator.getInstance().addBorrowerEntity(bor);
	}
	
	
	/**Override Due date for Books
	 * Functionality for Administrator
	 * */
	private void overrideDueDate(HttpServletRequest request) throws Exception {
		Borrower bor=new Borrower(Integer.parseInt(Utility.encodeParameter(request.getParameter("cardNo"))));
		LibraryBranch branch=new LibraryBranch(Integer.parseInt(Utility.encodeParameter(request.getParameter("branchId"))));
		Book book=new Book(Integer.parseInt(Utility.encodeParameter(request.getParameter("bookId"))));
		BookLoan bl=new BookLoan(book,branch,bor);
		bl.setDueDate(Utility.parseStringToDate(Utility.encodeParameter(request.getParameter("dueDate"))));
		Administrator.getInstance().overrideBookLoan(bl);
	}
	
	
	
	/**
	 * Book Loan Info*/
	
	private List<BookLoan> showBookLoanInfo(HttpServletRequest request) throws Exception {
		Borrower bor=new Borrower(Integer.parseInt(Utility.encodeParameter(request.getParameter("cardNum"))));
		return BorrowerService.getInstance().getCheckedOutBooks(bor);
	}
	
	
}
