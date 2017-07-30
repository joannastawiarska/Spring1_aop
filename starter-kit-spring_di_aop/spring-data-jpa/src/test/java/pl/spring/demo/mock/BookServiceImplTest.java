package pl.spring.demo.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import pl.spring.demo.aop.BookDaoAdvisor;
import pl.spring.demo.common.Sequence;
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.service.impl.BookServiceImpl;
import pl.spring.demo.to.BookTo;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookDao bookDao;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldSaveBook() {
        // given
        BookTo book = new BookTo(null, "title", "author");
        Mockito.when(bookDao.save(book)).thenReturn(new BookTo(1L, "title", "author"));
        // when
        BookTo result = bookService.saveBook(book);
        // then
        Mockito.verify(bookDao).save(book);
        assertEquals(1L, result.getId().longValue());
    }
    
    BookDaoAdvisor bookDaoAdvisor = null;
    Sequence sequence = new Sequence();
	Set<BookTo> ALL_BOOKS = new HashSet<BookTo>();
    
	@Before
	public void setup()  {
		bookDaoAdvisor = new BookDaoAdvisor(sequence, ALL_BOOKS);
	}
    
    @Test
    public void testShouldSetId() {
    	//given
    	BookTo book = new BookTo(null, "title", "author");
    	
    	//when
    	//BookDaoAdvisor bookDaoAdvisor = new BookDaoAdvisor(sequence, ALL_BOOKS);
    	BookTo result = bookDaoAdvisor.setId(book);
    	
    	
    	//then
    	assertEquals(1L, result.getId().longValue());
    }
}
