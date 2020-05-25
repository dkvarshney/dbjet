package dk.dbjet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dk.dbjet.marshaller.JetMarshaller;
import dk.dbjet.marshaller.NoOperationMarshaller;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JetColumn {

	/**
	 * Column name.
	 * 
	 * @return
	 */
	String name();
	
	/**
	 * Column data type.
	 * By default type is JetColumnType.TEXT
	 * 
	 * @return
	 */
	JetColumnType type() default JetColumnType.TEXT;
	
	/**
	 * Column size mainly is column is TEXT/VARCHAR type.
	 * By default size is 255.
	 * 
	 * @return
	 */
	int size() default 255;
	
	/**
	 * Default value to be inserted.
	 * By default the value is JetColumnValue.NONE.
	 * 
	 * @return
	 */
	JetColumnValue defaultValue() default JetColumnValue.NONE;
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends JetMarshaller> marshaller() default NoOperationMarshaller.class;

}
