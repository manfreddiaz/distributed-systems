package ca.concordia.encs.distributed.util;

import ca.concordia.encs.distributed.exception.AccessViolationException;
import ca.concordia.encs.distributed.exception.InvalidOperationException;
import ca.concordia.encs.distributed.model.Record;
import ca.concordia.encs.distributed.security.Modifiable;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveFieldModifier {
    public static void modifyRecordField(Record record, String fieldName, String fieldValue) throws InvalidOperationException,
            AccessViolationException {

        Class recordClass = record.getClass();
        Field field = FieldUtils.getField(recordClass, fieldName, true);
        if(field == null) {  throw new InvalidOperationException(); }

        Modifiable annotation = field.getDeclaredAnnotation(Modifiable.class);
        if(annotation == null) { throw new AccessViolationException(); }

        try {
            Method setter = recordClass.getMethod(String.format("set%s", WordUtils.capitalize(fieldName)), field.getType());
            Class type = field.getType();

            if(type.isEnum()) {
                try {
                    setter.invoke(record, Enum.valueOf(type, fieldValue));
                }
                catch (IllegalAccessException e) {
                    throw new AccessViolationException();
                } catch (InvocationTargetException e) {
                    throw new InvalidOperationException();
                }
                catch (IllegalArgumentException e) {
                    throw new InvalidOperationException();
                }
            }
            else if(type.isAssignableFrom(Integer.class)) {
                try {
                    setter.invoke(record, Integer.parseInt(fieldValue));
                }
                catch (IllegalAccessException e) {
                    throw new AccessViolationException();
                }
                catch(NumberFormatException e) {
                    throw new InvalidOperationException();
                } catch (InvocationTargetException e) {
                    throw new InvalidOperationException();
                }
            } else if(type.isAssignableFrom(Boolean.class)) {
                try {
                    setter.invoke(record, Boolean.parseBoolean(fieldValue));
                }
                catch (IllegalAccessException e) {
                    throw new AccessViolationException();
                } catch (InvocationTargetException e) {
                    throw new InvalidOperationException();
                }
            } else {
                try {
                    field.set(record, fieldValue);
                }
                catch (IllegalAccessException e) {
                    throw new AccessViolationException();
                }
            }
        }
        catch (NoSuchMethodException e) {
            throw new InvalidOperationException();
        }
        catch (SecurityException e) {
            throw new AccessViolationException();
        }
    }
}
