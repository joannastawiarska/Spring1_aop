package pl.spring.demo.aop;


import org.springframework.aop.MethodBeforeAdvice;
import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.common.Sequence;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.BookTo;
import pl.spring.demo.to.IdAware;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class BookDaoAdvisor implements MethodBeforeAdvice {

	private Sequence sequence;
	private Set<BookTo> ALL_BOOKS = new HashSet<>();
	
	public BookDaoAdvisor(Sequence sequence, Set<BookTo> ALL_BOOKS){
    	this.sequence = sequence;
    	this.ALL_BOOKS = ALL_BOOKS;
    }
	
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {

        if (hasAnnotation(method, o, NullableId.class)) {
            checkNotNullId(objects[0]);
        }
    }

    private void checkNotNullId(Object o) {
        if (o instanceof IdAware && ((IdAware) o).getId() != null) {
            throw new BookNotNullIdException();
        }
    }

    private boolean hasAnnotation (Method method, Object o, Class annotationClazz) throws NoSuchMethodException {
        boolean hasAnnotation = method.getAnnotation(annotationClazz) != null;

        if (!hasAnnotation && o != null) {
            hasAnnotation = o.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(annotationClazz) != null;
        }
        return hasAnnotation;
    }
    
    public BookTo setId (BookTo book) {
    	if (book.getId() == null) {
           book.setId(sequence.nextValue(ALL_BOOKS));
        }
    	return book;
    }
    
}
