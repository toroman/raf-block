package edu.raf.gef.services.beaneditor.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ova anotacija se opcionalno moze nakaciti na String property
 * radi detaljnijih podesavanja
 * @author Srecko Toroman
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringProperty {
	/**
	 * @return da li da se koristi multiline editor
	 */
	boolean multiline() default false;
	
}
