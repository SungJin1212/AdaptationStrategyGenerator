package Logic;

import com.squareup.javapoet.CodeBlock;

public class AnnotationGeneration {
    public static CodeBlock getAnnotation() {
        CodeBlock.Builder annotationBuilder = CodeBlock.builder();

        annotationBuilder.addStatement("Generated State Machine Code");

        return annotationBuilder.build();
    }
}
