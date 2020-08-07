package CodeGeneration.CodeGenerationLogic.Bahavior;

import com.squareup.javapoet.CodeBlock;

public class AnnotationGeneration {
    /**
     *
     * @param url name of the xml file
     * @return Annotation Code
     */
    public static CodeBlock getAnnotation(String url) {
        CodeBlock.Builder annotationBuilder = CodeBlock.builder();

        annotationBuilder.addStatement("Generated State Machine Code From: " + url);

        return annotationBuilder.build();
    }
}
