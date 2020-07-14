package Logic;

import com.squareup.javapoet.CodeBlock;

public class Annotation {
    public static CodeBlock getAnnotation() {
        CodeBlock.Builder annotationBuilder = CodeBlock.builder();

        annotationBuilder.addStatement("Generated State Machine Code");

        return annotationBuilder.build();
    }
}
