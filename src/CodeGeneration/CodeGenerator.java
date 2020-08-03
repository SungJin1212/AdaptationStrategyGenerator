package CodeGeneration;

import CodeGeneration.XMLParseDataType.State;
import CodeGeneration.XMLParseDataType.Synchronization;
import CodeGeneration.XMLParseDataType.Transition;
import Model.AbstactClass.CS;
import Model.AbstactClass.Environment;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static CodeGeneration.CodeGenerationLogic.ActionGeneration.getAction;
import static CodeGeneration.CodeGenerationLogic.AnnotationGeneration.getAnnotation;
import static CodeGeneration.CodeGenerationLogic.BasicSkeletonGeneration.*;
import static CodeGeneration.CodeGenerationLogic.LocalVariableGeneration.getLocalFieldSpec;
import static CodeGeneration.CodeGenerationLogic.RunGeneration.getRun;
import static CodeGeneration.CodeGenerationLogic.TimeVariableGeneration.getTimeFieldSpec;
import static CodeGeneration.CodeGenerationLogic.TransitionGeneration.getTransition;
import static CodeGeneration.Parser.Parser.*;

public class CodeGenerator {

    public static void main(String[] args) {


        Set<Synchronization> AllTransition = new HashSet<>();
        ArrayList<String> urlList = new ArrayList<>();
//
//        urlList.add("Human.xml");
//        urlList.add("bulb.xml");
//        urlList.add("MoppingRobot_LocalVariableExtended.xml");

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
        } //모든 model에서 "?" 있는 채널 검사 후 "AllTransition"에 추가


        /*print Synchronization classes */

//        for(Synchronization s : AllTransition) {
//            System.out.println(String.format("%s %s \n", s.getName(), s.getTrigger() ));
//        }



        for (String url : urlList) {

            ArrayList<State> States = getStateInformation(url); //get state information
            ArrayList<Transition> Transitions = getTransitionInformation(url); //get transition information
            ArrayList<String> Parameters = getParameterInformation(url);
            String actionCode = getActionCode(url);
            String type = getType(url);
            ArrayList<String> localVariables = getLocalVariableInformation(url);



            /* print parsed data */

            System.out.println("State Machine: " + url);
            System.out.println(type);
            for (State s : States) {
                System.out.println(String.format("State: %s %s %s", s.getInitialState(), s.getStateName(), s.getTime()));
            }
            for (Transition t : Transitions) {
                System.out.println(String.format("Transition: %s %s %s %s %s %s", t.getFrom(), t.getTo(), t.getGuard(), t.getProbability(), t.getAction(), t.getTrigger()));
            }
            for(String i : Parameters) {
                System.out.print("Parameters: ");
                System.out.println(i);
            }
            System.out.println("--------------------");



            codeGeneration(url,type,actionCode, localVariables, States, Transitions, Parameters,AllTransition);
        }

    }



    private static void codeGeneration(String url, String type, String actionCode, ArrayList<String> localVariables, ArrayList<State> states, ArrayList<Transition> transitions, ArrayList<String> parameters, Set<Synchronization> allTransition) {


        String className = url.substring(0,url.lastIndexOf(".")); //get state machine name
        String initialStateName = getInitialStateName(states); // get name of the initial state
        TypeSpec enumTypeSpec = enumGeneration(states); //enum generation

        MethodSpec constructor = getConstructor(initialStateName,parameters,states); //get "constructor" code
        MethodSpec getter = getGetter(); // get "getter" code
        MethodSpec setter = getSetter(); // get "setter" code


        CodeBlock annotations = getAnnotation(url); // get annotations

        ArrayList<FieldSpec> GuardsParametersFields = getFieldSpec(transitions,parameters); //get field variable
        ArrayList<FieldSpec> LocalVariablesFields = getLocalFieldSpec(localVariables);
        ArrayList<FieldSpec> TimeVariablesFields = getTimeFieldSpec(states);
        ArrayList<MethodSpec> trans = getTransition(transitions,allTransition); // get transition code
        ArrayList<MethodSpec> actions = getAction(transitions,actionCode);

        //MethodSpec.Builder builder = MethodSpec.methodBuilder(curTriggerName).addModifiers(Modifier.PUBLIC).returns(boolean.class);


        TypeSpec.Builder builder = TypeSpec.classBuilder(className);

        builder.addModifiers(Modifier.PUBLIC)
                .addType(enumTypeSpec)
                .addFields(LocalVariablesFields)
                .addFields(GuardsParametersFields)
                .addFields(TimeVariablesFields)
                .addMethods(Arrays.asList(constructor,getter,setter))
                .addMethods(trans)
                .addMethods(actions)
                .addJavadoc(annotations);

        if(type.equals("CS")) {
            builder.superclass(CS.class);
            MethodSpec run = getRun(states,transitions,allTransition);
            builder.addMethod(run);
        }
        else if (type.equals("EnvironmentCondition")) {
            builder.superclass(Environment.class);

        }
        builder.build();




        JavaFile javaFile = JavaFile.builder("Model.GeneratedCode.Behavior", builder.build())
                .build();
        try {
            javaFile.writeTo(Paths.get("./src"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }




}
