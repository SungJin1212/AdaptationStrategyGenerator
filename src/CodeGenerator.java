import Data.State;
import Data.Transition;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static Logic.Annotation.getAnnotation;
import static Logic.BasicSkeletonGeneration.*;
import static Logic.TransitionGeneration.getTransition;
import static Parser.Parser.getStateInformation;
import static Parser.Parser.getTransitionInformation;

public class CodeGenerator {

    public static void main(String[] args) {

        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("bulb.xml");

        for (String url : urlList) {

            ArrayList<State> States = getStateInformation(url); //get state information
            ArrayList<Transition> Transitions = getTransitionInformation(url); //get transition information

            /* print parsed data */
            /*
            for (State s : States) {
                System.out.println(String.format("%s %s \n", s.getInitialState(), s.getStateName()));
            }
            for (Transition t : Transitions) {
                System.out.println(String.format("%s %s %s %s %s %s %s \n", t.getFrom(), t.getTo(), t.getGuard(), t.getProbability(), t.getTime(), t.getAction(), t.getTrigger()));
            } */

            codeGeneration(url, States, Transitions);
        }
    }

    private static void codeGeneration(String url, ArrayList<State> states, ArrayList<Transition> transitions) {


        String className = url.substring(0,url.lastIndexOf(".")); //get state machine name
        String initialStateName = getInitialStateName(states); // get name of the initial state
        TypeSpec enumTypeSpec = enumGeneration(states); //enum generation

        ArrayList<FieldSpec> fields = getFieldSpec(transitions); //get field variable

        MethodSpec constructor = getConstructor(className, initialStateName); //get "constructor" code
        MethodSpec getter = getGetter(); // get "getter" code
        MethodSpec setter = getSetter(); // get "setter" code

        ArrayList<MethodSpec> trans = getTransition(transitions); // get transition code

        CodeBlock annotations = getAnnotation(); // get annotations

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addType(enumTypeSpec)
                .addFields(fields)
                .addMethods(Arrays.asList(constructor,getter,setter))
                .addMethods(trans)
                .addJavadoc(annotations)
                .build();

        JavaFile javaFile = JavaFile.builder("GeneratedCode", typeSpec)
                .build();
        try {
            javaFile.writeTo(Paths.get("./src"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


}
