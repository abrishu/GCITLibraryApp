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
import com.gcitsolutions.libraryapp.Service.Administrator;
import com.gcitsolutions.libraryapp.Service.Utility;

/**
 * Servlet implementation class Author
 */

@WebServlet(name = "AuthorServlet", urlPatterns = { "/addAuthor", "/showUpdateAuthors", "/updateAuthor", "/viewAuthors",
		"/pageAuthors", "/searchAuthors","/paginationAuthors" })
public class AuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthorServlet() {
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
		if (request.getServletPath().equals("/paginationAuthors")) {
			String searchString = request.getParameter("searchString");
			try {
					Integer pageCount=0;
					Integer noOfRecords=0;
					if(searchString!=null && !searchString.equals("")){
						noOfRecords=Administrator.getInstance().getCountOfAuthors(searchString);
					}else{
						noOfRecords=Administrator.getInstance().getCountOfAuthors("");
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
		} 
		
		else if (request.getServletPath().equals("/pageAuthors")) {
			Integer pageNo;
			String searchString = request.getParameter("searchString");
			try {
				if (request.getParameter("pageNo") != null) {
					pageNo = Integer.parseInt(request.getParameter("pageNo"));
					List<Author> authorList = Administrator.getInstance().getAllAuthors(pageNo, 10, searchString);
					request.setAttribute("authorList", authorList);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/viewAuthors.jsp");
					rd.forward(request, response);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (request.getServletPath().equals("/searchAuthors")){
			try {
				searchAuthorsAjax(request,response);
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

			/** Author **/
			case "/addAuthor": {
				addAuthor(request);
				request.setAttribute("result", "Author Added Succesfully!");
				break;
			}

			case "/viewAuthors": {
				getAllTheAuthors(request);
				rd = getServletContext().getRequestDispatcher("/viewAuthors.jsp");
				break;
			}

			case "/showUpdateAuthors": {
				getAllTheAuthors(request);
				rd = getServletContext().getRequestDispatcher("/updateAuthor.jsp");
				break;
			}

			case "/updateAuthor": {
				updateAuthor(request);
				request.setAttribute("result", "Author updated Succesfully!");
				break;
			}

			case "/searchAuthors": {
				searchAuthors(request);
				rd = getServletContext().getRequestDispatcher("/viewAuthors.jsp");
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

	private void searchAuthors(HttpServletRequest request) throws Exception {
		String searchString = request.getParameter("searchString");
		List<Author> authors = new ArrayList<Author>();
		authors = Administrator.getInstance().getAllAuthors(1, 10, searchString);
		request.setAttribute("authorList", authors);
		request.setAttribute("searchString", searchString);
	}
	
	private void searchAuthorsAjax(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String searchString = request.getParameter("searchString");
		Integer pageNo=1;
		if(request.getParameter("pageNo")!=null){
			pageNo=Integer.parseInt(request.getParameter("pageNo"));
		}
		List<Author> authors = new ArrayList<Author>();
		ObjectMapper mapper=new ObjectMapper();
		
		authors = Administrator.getInstance().getAllAuthors(pageNo, 10, searchString);
		String jsonAuthors=mapper.writeValueAsString(authors);
		response.setContentType("application/json");
		response.getWriter().write(jsonAuthors);
	}

	/* Author functions */
	private void addAuthor(HttpServletRequest request) throws Exception {

		String title = Utility.encodeParameter(Utility.encodeParameter(request.getParameter("authorName")));
		Author a = new Author();
		a.setName(title);
		Administrator.getInstance().addAuthor(a);
	}

	private void getAllTheAuthors(HttpServletRequest request) throws Exception {
		Administrator admin = Administrator.getInstance();
		Integer pageNo = null;
		List<Author> authorList = null;
		if (request.getParameter("pageNo") != null) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		if (pageNo != null) {
			authorList = admin.getAllAuthors(pageNo, 10, "");
		} else {
			authorList = admin.getAllAuthors(1, 10, "");
		}
		request.setAttribute("authorList", authorList);
	}

	private void updateAuthor(HttpServletRequest request) throws Exception {
		Author author = new Author();
		author.setId(Integer.parseInt(Utility.encodeParameter(request.getParameter("authorId").toString())));
		author.setName(Utility.encodeParameter(request.getParameter("newName").toString()));
		Administrator.getInstance().updateAuthor(author);
	}

}
