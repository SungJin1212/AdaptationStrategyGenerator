package Logic;

import com.squareup.javapoet.CodeBlock;

public class AnnotationGeneration {
    public static CodeBlock getAnnotation(String url) {
        CodeBlock.Builder annotationBuilder = CodeBlock.builder();

        annotationBuilder.addStatement("Generated State Machine Code: " + url);

        return annotationBuilder.build();
    }
}
