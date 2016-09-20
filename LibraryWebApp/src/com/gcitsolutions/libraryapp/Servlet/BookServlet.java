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
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.Entity.Publisher;
import com.gcitsolutions.libraryapp.Service.Administrator;
import com.gcitsolutions.libraryapp.Service.Utility;

/**
 * Servlet implementation class BooKServlet
 */
@WebServlet(name = "/BookServlet", urlPatterns = { "/showAddBook", "/addBook", "/showUpdateBooks", "/updateBook",
		"/getBookInfo", "/displayBookDetails", "/deleteBook","/paginationBooks","/searchBooks" ,"/getBookViaAjax"})
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookServlet() {
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

		ObjectMapper mapper = new ObjectMapper();
		if (request.getServletPath().equals("/getBookInfo")) {
			try {
				// getSelectedBookInfo(request,response);
				Administrator admin = Administrator.getInstance();
				Book book = new Book(
						Integer.parseInt(Utility.encodeParameter(request.getParameter("bookId").toString())));
				book=admin.getBookInfo(book);
				book = admin.getPublisherForABook(book);
				book = admin.getAuthorsForABook(book);
				String jsonBook = mapper.writeValueAsString(book);
				response.setContentType("application/json");
				response.getWriter().write(jsonBook);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 	
		else if (request.getServletPath().equals("/getBookViaAjax")) {
			try {
				// getSelectedBookInfo(request,response);
				searchBooksAjax(request,response);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 	
		else if (request.getServletPath().equals("/paginationBooks")) {
			String searchString = request.getParameter("searchString");
			try {
					Integer pageCount=0;
					Integer noOfRecords=0;
					if(searchString!=null && !searchString.equals("")){
						noOfRecords=Administrator.getInstance().getCountOfBooks(searchString);
					}else{
						noOfRecords=Administrator.getInstance().getCountOfBooks("");
					}
					Integer pageSize=10;
					if(noOfRecords%10==0){
						if((noOfRecords/pageSize)==0){
							pageCount=1;
						}else{
							pageCount=(noOfRecords/pageSize);	
						}
					}else{
						pageCount=(noOfRecords/pageSize)+1;
					}
					
					response.getWriter().write(pageCount.toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (request.getServletPath().equals("/deleteBook")) {
			try {
				// getSelectedBookInfo(request,response);
				Administrator admin = Administrator.getInstance();
				Book book = new Book(
						Integer.parseInt(Utility.encodeParameter(request.getParameter("bookId").toString())));
				Message msg = admin.deleteBook(book);
				response.setContentType("application/json");
				response.getWriter().write(mapper.writeValueAsString(msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (request.getServletPath().equals("/searchBooks")){
			try {
				searchBooksAjax(request,response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			doPost(request, response);
		}
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

			case "/addBook": {
				addBook(request);
				request.setAttribute("result", "Book added Succesfully!");
				rd = getServletContext().getRequestDispatcher("/viewBooks.jsp");
				break;
			}

			case "/showAddBook": {
				showAddBook(request);
				rd = getServletContext().getRequestDispatcher("/addBook.jsp");
				break;
			}

			case "/showUpdateBooks": {
				getAllTheBooks(request);
				rd = getServletContext().getRequestDispatcher("/UpdateBook.jsp");
				break;
			}

			case "/displayBookDetails": {
				getAllTheBooks(request);
				rd = getServletContext().getRequestDispatcher("/displayBooks.jsp");
				break;
			}

			case "/updateBook": {
				updateBook(request);
				request.setAttribute("result", "Book updated Succesfully!");
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
	
	private void addBook(HttpServletRequest request) throws Exception {
		String title = Utility.encodeParameter(request.getParameter("bookTitle"));
		String[] authorIdList=request.getParameterValues("authorId");
		String pubId = Utility.encodeParameter(request.getParameter("pubId"));
		Administrator admin=Administrator.getInstance();
		Book b = new Book();
		Author a =null;
		Publisher p = new Publisher();
		List<Author> authorList=new ArrayList<Author>();
		b.setTitle(title);
		for(String strAuthor:authorIdList){
			a=new Author(Integer.parseInt(strAuthor));
			authorList.add(a);
		}
		p.setId(Integer.parseInt(pubId));
		b.setAuthorList(authorList);
		b.setPublisher(p);
		admin.addBook(b);
	}
	
	private void showAddBook(HttpServletRequest request) throws Exception {
		Administrator admin=Administrator.getInstance();
		List<Author> authorList = admin.getAllAuthors();
		List<Publisher> pubList = admin.getAllPublishers();
		request.setAttribute("authorList", authorList);
		request.setAttribute("pubList", pubList);
	}
	
	private void updateBook(HttpServletRequest request) throws Exception{
	Book book=new Book();
		book.setId(Integer.parseInt(Utility.encodeParameter(request.getParameter("bookId").toString())));
		book.setTitle(Utility.encodeParameter(request.getParameter("newName").toString()));
		List<Author> listAuthor=null;
		if(request.getParameterValues("authorId")==null){
			String[] strAuthor=request.getParameterValues("authorId[]");
			for(String s:strAuthor){
				 if(Integer.parseInt(s)!=0){
					if(listAuthor==null){
						listAuthor=new ArrayList<Author>();
					}
					listAuthor.add(new Author(Integer.parseInt(s)));
				}
			}
		}
		
		
		Publisher pub=new Publisher();
		int pubId=Integer.parseInt(Utility.encodeParameter(request.getParameter("pubId").toString()));
		if(pubId==0){
			pub.setId(null);
		}else{
			pub.setId(pubId);
		}
		book.setAuthorList(listAuthor);
		book.setPublisher(pub);
		Administrator.getInstance().updateBookInfo(book);
	}
	
	private void getAllTheBooks(HttpServletRequest request) throws Exception{
		Administrator admin=Administrator.getInstance();
		List<Book> bookList=admin.getAllBooks();
		request.setAttribute("bookList", bookList);
		request.setAttribute("authorList", admin.getAllAuthors());
		request.setAttribute("pubList", admin.getAllPublishers());
	}

	private void searchBooksAjax(HttpServletRequest request, HttpServletResponse response)  throws Exception  {
		List<Book> books = new ArrayList<Book>();
		String searchString = request.getParameter("searchString");
		Integer pageNo=1;
		if(request.getParameter("pageNo")!=null){
			pageNo=Integer.parseInt(request.getParameter("pageNo"));
		}
		ObjectMapper mapper=new ObjectMapper();
		books = Administrator.getInstance().getAllBooks(pageNo, 10, searchString);
		String jsonBooks=mapper.writeValueAsString(books);
		response.setContentType("application/json");
		response.getWriter().write(jsonBooks);
		
	}
	
}
