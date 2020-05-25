package dk.dbjet.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonUtils {

	public static Set<Field> findClassFields(Class<?> classs, Class<? extends Annotation> ann) {
		Set<Field> set = new HashSet<>();
		Class<?> klass = classs;
		while (klass != null) {
			for (Field field : klass.getDeclaredFields()) {
				if (field.isAnnotationPresent(ann)) {
					set.add(field);
				}
			}
			klass = klass.getSuperclass();
		}
		return set;
	}

	public static boolean isNotEmpty(String string) {
		return !(string == null || string.isEmpty());
	}

	public static boolean isNotEmpty(String[] array) {
		return !(array == null || array.length == 0);
	}

	public static boolean isNotEmpty(List<?> array) {
		return !(array == null || array.isEmpty());
	}

	public static void validateEqualsOrGreaterThan(int source, int target) {
		if (source < target) {
			throw new RuntimeException(source + " should be >= " + target);
		}		
	}

	public static boolean stringContainsAnyOfCharacter(String string, char[] charArray) {
		for (char inChar : string.toCharArray()) {
			for (char cpChar : charArray) {
				if (inChar == cpChar) {
					return true;
				}
			}
		}
		return false;
	}
}
