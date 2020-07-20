import Data.State;
import Data.Synchronization;
import Data.Transition;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static Logic.ActionGeneration.getAction;
import static Logic.AnnotationGeneration.getAnnotation;
import static Logic.BasicSkeletonGeneration.*;
import static Logic.TransitionGeneration.getTransition;
import static Parser.Parser.*;

public class CodeGenerator {

    public static void main(String[] args) {


        Set<Synchronization> AllTransition = new HashSet<>();
        ArrayList<String> urlList = new ArrayList<>();
//        urlList.add("Human.xml");
//        urlList.add("bulb.xml");

        urlList.add("MoppingRobot.xml");
        urlList.add("SweepingRobot.xml");
        urlList.add("Tile.xml");


        for(String url : urlList) {
            ArrayList<Transition> trans = getTransitionInformation(url);
            for(Transition t : trans) {
                String curTrigger = t.getTrigger();
                if(curTrigger.charAt(curTrigger.length()-1) == '?'){ // Synchronization check
                    AllTransition.add(new Synchronization(url.substring(0, url.lastIndexOf(".")), t.getTrigger()));
                }
            }
        }


        /*print Synchronization classes */

//        for(Synchronization s : AllTransition) {
//            System.out.println(String.format("%s %s \n", s.getName(), s.getTrigger() ));
//        }



        for (String url : urlList) {

            ArrayList<State> States = getStateInformation(url); //get state information
            ArrayList<Transition> Transitions = getTransitionInformation(url); //get transition information
            ArrayList<String> Parameters = getParameterInformation(url);

            /* print parsed data */
            System.out.println("State Machine: " + url);
            for (State s : States) {
                System.out.println(String.format("State: %s %s", s.getInitialState(), s.getStateName()));
            }
            for (Transition t : Transitions) {
                System.out.println(String.format("Transition: %s %s %s %s %s %s", t.getFrom(), t.getTo(), t.getGuard(), t.getProbability(), t.getAction(), t.getTrigger()));
            }
            for(String i : Parameters) {
                System.out.print("Parameters: ");
                System.out.println(i);
            }
            System.out.println("--------------------");



            codeGeneration(url, States, Transitions, Parameters,AllTransition);
        }

    }

    private static void codeGeneration(String url, ArrayList<State> states, ArrayList<Transition> transitions, ArrayList<String> parameters, Set<Synchronization> allTransition) {


        String className = url.substring(0,url.lastIndexOf(".")); //get state machine name
        String initialStateName = getInitialStateName(states); // get name of the initial state
        TypeSpec enumTypeSpec = enumGeneration(states); //enum generation

        MethodSpec constructor = getConstructor(className, initialStateName,parameters); //get "constructor" code
        MethodSpec getter = getGetter(); // get "getter" code
        MethodSpec setter = getSetter(); // get "setter" code

        CodeBlock annotations = getAnnotation(url); // get annotations

        ArrayList<FieldSpec> fields = getFieldSpec(transitions,parameters); //get field variable
        ArrayList<MethodSpec> trans = getTransition(transitions,allTransition); // get transition code
        ArrayList<MethodSpec> actions = getAction(transitions);

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addType(enumTypeSpec)
                .addFields(fields)
                .addMethods(Arrays.asList(constructor,getter,setter))
                .addMethods(trans)
                .addMethods(actions)
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
