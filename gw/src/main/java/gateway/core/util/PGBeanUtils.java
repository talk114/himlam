package gateway.core.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;

/**
 *
 * @author vinhnt
 *
 */
public class PGBeanUtils extends BeanUtilsBean{

    @Override
    public void copyProperty(Object dest, String name, Object value) throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        super.copyProperty(dest, name, value);
    }

}