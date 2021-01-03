package org.ifinal.finalframework.auto.coding;

import java.io.Writer;
import org.ifinal.finalframework.auto.coding.annotation.Template;
import org.ifinal.finalframework.util.Asserts;

/**
 * The generator of template code.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Coder {

    static Coder getDefaultCoder() {
        return new VelocityCoder();
    }

    default void coding(Object model, Writer writer) {
        Asserts.requiredNonNull(model, "the model must not ne null!");
        Asserts.requiredNonNull(writer, "the writer must not be null!");

        Template template = model.getClass().getAnnotation(Template.class);

        if (template == null) {
            throw new NullPointerException("the model must one Template annotation , model=" + model
                .getClass()
                .getName());
        }
        coding(template.value(), model, writer);
    }

    /**
     * coding the template code with data model by writer.
     *
     * @param template the name of template
     * @param model    the data model of template
     * @param writer   the writer of coding file
     */
    void coding(String template, Object model, Writer writer);

}
