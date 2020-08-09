package io.vgaur.vidya.models.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * A Jackson module that filters out properties that are annotated with @InternalField.
 */
@Provider
public class VidyaInternalModule extends Module {
    private static final Logger LOG = LoggerFactory.getLogger(VidyaInternalModule.class);

    @Override
    public String getModuleName() {
        return "VidyaInternalModule";
    }

    @Override
    public Version version() {
        return new Version(0, 0, 1, "SNAPSHOT", "io.vgaur",
                "vidya-backend");
    }

    @Override
    public void setupModule(SetupContext context) {
        LOG.info("initalizing VidyaInternalModule");
        context.addBeanSerializerModifier(new VidyaInternalBeanSerializerModifier());
        // context.addBeanDeserializerModifier(new VidyaInternalBeanDeserializerModifier());
    }

    /**
     * Filters out properties marked with @InternalField during Serialization
     */
    public static class VidyaInternalBeanSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(
                SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            List<BeanPropertyWriter> keepProperties = new ArrayList<>();
            for (BeanPropertyWriter p : beanProperties) {
                if (p.getAnnotation(InternalField.class) == null) {
                    keepProperties.add(p);
                }
            }
            return keepProperties;
        }
    }

//    /**
//     * Filters out properties marked with @InternalField during De-serialization
//     */
//    public static class VidyaInternalBeanDeserializerModifier extends BeanDeserializerModifier {
//
//        @Override
//        public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc,
//                                                     BeanDeserializerBuilder builder) {
//            List<SettableBeanProperty> removeProperties = new ArrayList<>();
//            for (SettableBeanProperty p : Iterators.toArray(builder.getProperties(), SettableBeanProperty.class)) {
//                if (p.getAnnotation(InternalField.class) != null) {
//                    removeProperties.add(p);
//                }
//            }
//            for (SettableBeanProperty p : removeProperties) {
//                builder.removeProperty(p.getFullName());
//            }
//            return builder;
//        }
//    }
}
