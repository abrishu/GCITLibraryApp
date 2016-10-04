package com.gcitsolutions.libraryapp.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcitsolutions.libraryapp.Data.AuthorDAO;
import com.gcitsolutions.libraryapp.Data.BookCopiesDAO;
import com.gcitsolutions.libraryapp.Data.BookDAO;
import com.gcitsolutions.libraryapp.Data.BookLoanDAO;
import com.gcitsolutions.libraryapp.Data.BorrowerDAO;
import com.gcitsolutions.libraryapp.Data.GenreDAO;
import com.gcitsolutions.libraryapp.Data.LibraryBranchDAO;
import com.gcitsolutions.libraryapp.Data.PublisherDAO;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Entity.Message;

public class Librarian {

	@Autowired
	AuthorDAO adao;
	@Autowired
	BookDAO bdao;
	@Autowired
	PublisherDAO pubdao;
	@Autowired 
	LibraryBranchDAO libdao;
	@Autowired
	BookLoanDAO bldao;
	@Autowired
	BookCopiesDAO bcopdao;
	@Autowired
	GenreDAO genredao;
	@Autowired
	BorrowerDAO bordao;
	@Autowired
	Message message;
	
private static Librarian libInstance=null;
	
	
	public Librarian() {
	}
	
	public static Librarian getInstance(){
		if(libInstance==null){
			synchronized(Librarian.class){
				if(libInstance==null){
					return new Librarian();
				}
			}
		}
		return libInstance;
	}
	
	public List<BookCopies> getAllBookCopies(Integer pageNo,Integer pageSize,String searchStringBranch,String searchStringBook) throws Exception{	
		List<BookCopies> bcopList= bcopdao.readAll(pageNo,pageSize, searchStringBranch,searchStringBook);
		if(bcopList!=null){
			if(!bcopList.isEmpty()){
				for(BookCopies bcop:bcopList){
					bcop.setBook(bdao.read(new Integer[]{bcop.getBook().getId()}));
					bcop.setBranch(libdao.read(new Integer[]{bcop.getBranch().getId()}));
				}
			}
		}
		return bcopList;
	}
	
	@Transactional
	public void updateLibraryBranch(LibraryBranch branch) throws Exception{
		libdao.update(branch);
	}
	
	public BookCopies getBookCopiesForABook(BookCopies bcop) throws Exception {
			return bcopdao.read(new Integer[]{bcop.getBook().getId(),bcop.getBranch().getId()});
	}
	
	@Transactional
	public void addBookCopiesToBranch(BookCopies bcop) throws Exception{
		
		if(bcopdao.read(new Integer[]{bcop.getBook().getId(),bcop.getBranch().getId()})!=null){
			bcopdao.update(bcop);
		}else{
			bcopdao.create(bcop);
		}	
	}
	
	public Integer getCountOfBookCopies(String searchStringBranch,String searchStringBook) throws Exception {
		return bcopdao.getCountOfBookCopies(searchStringBranch, searchStringBook);
	}
	
	

}
