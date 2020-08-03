package io.vgaur.vidya.mybatis;

import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom MyBatis Object Factory so we can handle Immutables.
 *
 * We only need to provide the create methods, the rest of them should be ok from
 * DefaultObjectFactory.
 *
 * Checks for the Immutable prefix, which is default for the Immutables library.
 *
 * When using Immutables in MyBatis mappers, always use constructor mapping.
 *
 */
public class ImmutablesFactory extends DefaultObjectFactory {

    // The prefix for immutable classes
    private static final String IMMUTABLE_CLASS_PREFIX = "Immutable";

    // The constructor method for immutables
    private static final String IMMUTABLE_CONSTRUCTOR_METHOD = "of";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutablesFactory.class);

    /**
     * This is the default call when not using the constructor configuration.  We should never
     * call this when dealing with immutable types from MyBatis.
     *
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> T create(Class<T> type) {
        if (type.getSimpleName().startsWith(IMMUTABLE_CLASS_PREFIX)) {
            throw new RuntimeException(
                    "Invalid attempt to call default create in MyBatis Object Factory for " + type.getSimpleName());
        }
        return super.create(type);
    }

    /**
     * This is the call MyBatis uses when constructor is used in the mapping.
     *
     * @param type
     * @param constructorArgTypes
     * @param constructorArgs
     * @param <T>
     * @return
     */
    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {

        if (type.getSimpleName().startsWith(IMMUTABLE_CLASS_PREFIX)) {
            try {
                return instantiateImmutable(type, constructorArgTypes, constructorArgs);
            } catch (NoSuchMethodException ex) {
                LOGGER.warn("Attempted to call method '" + IMMUTABLE_CONSTRUCTOR_METHOD
                        + "' for " + type.getSimpleName()
                        + ", but did not exist. Attempting DefaultObjectFactory constructor invocation", ex);
            }
        }
        return super.create(type, constructorArgTypes, constructorArgs);
    }

    /**
     * This function should send the arguments to the "of" static constructor method of
     * the immutable type.
     *
     * @param type
     * @param constructorArgTypes
     * @param constructorArgs
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T instantiateImmutable(
            Class<T> type,
            List<Class<?>> constructorArgTypes,
            List<Object> constructorArgs) throws NoSuchMethodException {

        // Get the constructor method, throws NoSuchMethodException if not found
        Method method = type
                .getDeclaredMethod(
                        IMMUTABLE_CONSTRUCTOR_METHOD,
                        (Class[]) constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));

        // Try to invoke the method, throws IllegalAccessException if it isn't public, and throws
        // InvocationTargetException if the constructor throws an error or the args are incorrect
        try {
            return (T) method.invoke(null, constructorArgs.toArray(new Object[constructorArgs.size()]));
        } catch (InvocationTargetException | IllegalAccessException ex) {

            String argTypes = constructorArgTypes
                    .stream()
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));

            String argValues = constructorArgs
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            throw new ReflectionException(
                    "Error instantiating " + type.getSimpleName() + " with invalid types (" + argTypes +
                            ") or values (" + argValues + "). Cause: " + ex,
                    ex);
        }
    }
}
