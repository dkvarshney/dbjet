package dk.dbjet.factory;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnIsPrimary;
import dk.dbjet.annotation.JetTable;
import dk.dbjet.common.JetModel;

public final class JetReflectionFactory {

	private final static JetReflectionFactory INSTANCE = new JetReflectionFactory();
	private final static Map<Class<?>, String> CLASS_TO_TABLE 				= new ConcurrentHashMap<>();
	private final static Map<Class<?>, List<Field>> CLASS_TO_FIELDS 		= new ConcurrentHashMap<>();
	private final static Map<Class<?>, List<Field>> CLASS_TO_PKFIELDS 		= new ConcurrentHashMap<>();
	private final static Map<Class<?>, List<JetColumn>> CLASS_TO_COLUMNS 	= new ConcurrentHashMap<>();
	private final static Map<Class<?>, List<String>> CLASS_TO_COLUMNS_NAME 	= new ConcurrentHashMap<>();
	
	public static JetReflectionFactory getInstance() {
		return INSTANCE;
	}
	
	public String getTableName(Class<? extends JetModel> klass) {
		if (CLASS_TO_TABLE.containsKey(klass)) {
			return CLASS_TO_TABLE.get(klass);
		} else {
			if (klass.isAnnotationPresent(JetTable.class)) {
				JetTable jtable = klass.getAnnotation(JetTable.class);
				CLASS_TO_TABLE.put(klass, jtable.name());
				return jtable.name();
			} else {
				throw new RuntimeException("Missing @JetTable?");
			}
		}
	}
	
	public List<Field> getPrimaryFields(Class<? extends JetModel> klass) {
		if (CLASS_TO_PKFIELDS.containsKey(klass)) {
			return CLASS_TO_PKFIELDS.get(klass);
		} else {
			List<Field> allfields = getColumnFields(klass);
			List<Field> columns = new LinkedList<Field>();
			for (Field field : allfields) {
				if (field.isAnnotationPresent(JetColumnIsPrimary.class)) {
					columns.add(field);
				}
			}
			CLASS_TO_PKFIELDS.put(klass, columns);
			return columns;
		}
	}
	
	public List<String> getColumnsName(Class<? extends JetModel> klass) {
		if (CLASS_TO_COLUMNS_NAME.containsKey(klass)) {
			return CLASS_TO_COLUMNS_NAME.get(klass);
		} else {
			List<JetColumn> columns = getColumns(klass);
			List<String> names = new LinkedList<String>();
			for (JetColumn column : columns) {
				names.add(column.name());
			}
			CLASS_TO_COLUMNS_NAME.put(klass, names);
			return names;
		}
	} 
	
	public List<JetColumn> getColumns(Class<? extends JetModel> klass) {
		if (CLASS_TO_COLUMNS.containsKey(klass)) {
			return CLASS_TO_COLUMNS.get(klass);
		} else {
			List<Field> fields = getColumnFields(klass);
			List<JetColumn> columns = new LinkedList<JetColumn>();
			for (Field field : fields) {
				JetColumn column = field.getAnnotation(JetColumn.class);
				columns.add(column);
			}
			CLASS_TO_COLUMNS.put(klass, columns);
			CLASS_TO_FIELDS.put(klass, fields);
			return columns;
		}
	}
	
	public List<Field> getColumnFields(Class<? extends JetModel> klass) {
		if (CLASS_TO_FIELDS.containsKey(klass)) {
			return CLASS_TO_FIELDS.get(klass);
		} else {
			List<Field> fields = findClassFields(klass);
			List<JetColumn> columns = new LinkedList<JetColumn>();
			for (Field field : fields) {
				JetColumn column = field.getAnnotation(JetColumn.class);
				columns.add(column);
			}
			CLASS_TO_COLUMNS.put(klass, columns);
			CLASS_TO_FIELDS.put(klass, fields);
			return fields;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Field> findClassFields(Class<? extends JetModel> klass) {
	    List<Field> set = new LinkedList<Field>();
	    while (klass != null) {
	        for (Field field : klass.getDeclaredFields()) {
	            if (field.isAnnotationPresent(JetColumn.class)) {
	                set.add(field);
	            }
	        }
	        if (JetModel.class.isAssignableFrom(klass.getSuperclass())) {
	        	klass = (Class<? extends JetModel>) klass.getSuperclass();	        	
	        } else {
	        	klass = null;
	        }
	    }
	    return set;
	}
}
