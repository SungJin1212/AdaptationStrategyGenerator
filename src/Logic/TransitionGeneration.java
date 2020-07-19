package Logic;

import Data.Synchronization;
import Data.Transition;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TransitionGeneration {


    public static ArrayList<MethodSpec> getTransition(ArrayList<Transition> transitions, Set<Synchronization> allTransition) {

        Set<String> dependentClass = new HashSet<>();
        ArrayList<MethodSpec> trans = new ArrayList<>();
        Set<String> transitionNames = new HashSet<>();

        for(Transition t : transitions) {
            char last = getLastChar(t.getTrigger());
            if (last == '!' || last == '?') {
                transitionNames.add(getMethodName(t.getTrigger()));
            }
            else {
                transitionNames.add(t.getTrigger());
            }
        }

        for(Transition t : transitions) {
            char last = getLastChar(t.getTrigger());
            if (last == '!') {
                for(Synchronization s : allTransition) {
                    if(getLastChar(s.getTrigger()) == '?') {
                        dependentClass.add(s.getName());
                    }
                }
            }
        }
        Iterator<String> it = transitionNames.iterator();

        while(it.hasNext()) {
            trans.add(getEachTransition(transitions,it.next(),dependentClass));
        }

        return trans;
    }

    public static ArrayList<MethodSpec> getAction(ArrayList<Transition> transitions) {

        ArrayList<MethodSpec> actions = new ArrayList<>();

        for (Transition t : transitions) {
            if(!t.getAction().equals("NoAction")) {
                String[] actionList= t.getAction().split(",");
                for(String s : actionList) {
                    actions.add(getEachAction(s));
                }
            }
        }
        return actions;
    }

    private static MethodSpec getEachAction(String s) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(s).addModifiers(Modifier.PRIVATE);

        return builder.build();

    }

    private static MethodSpec getEachTransition(ArrayList<Transition> transitions, String name, Set<String> dependentClass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name).addModifiers(Modifier.PUBLIC).returns(boolean.class);

        for(String s : dependentClass) {
            builder.addParameter(TypeVariableName.get(s), getFirstChar(s)).build();
        }

        ArrayList<Transition> curTransitions = new ArrayList<>();

        for(Transition t : transitions) {
            if(getMethodName(t.getTrigger()).equals(name)) {
                curTransitions.add(t);
            }
        }

        CodeBlock transitionCode = getEachTransitionCode(curTransitions,dependentClass);

        builder.addCode(transitionCode);
        return builder.build();

    }

    private static CodeBlock getEachTransitionCode(ArrayList<Transition> curTransitions, Set<String> dependentClass) {
        boolean isGuard;
        boolean isProbability;

        CodeBlock.Builder builder = CodeBlock.builder();

        builder.addStatement("boolean wasEventProcessed = false");
        builder.addStatement("Status aStatus = status");
        builder.beginControlFlow("switch(aStatus)");

        for(Transition t : curTransitions) {
            isGuard = false;
            isProbability = false;

            if(!t.getGuard().equals("")) {
                isGuard = true;
            }

            builder.beginControlFlow("case " + t.getFrom() + ":");

            if(Double.parseDouble(t.getProbability()) < 1) {
                isProbability = true;
            }

            if (isGuard) {
                builder.beginControlFlow("if(" + t.getGuard() + ")");
            }

            if (isProbability) {
                builder.beginControlFlow("if(Math.random() < " + t.getProbability() + ")");
            }

            for(String s : dependentClass) {
                builder.addStatement(getFirstChar(s) + "." + getMethodName(t.getTrigger()) + "()");
            } //call dependent classes.

            if(!t.getAction().equals("NoAction")) {
                String[] actionList = t.getAction().split(",");
                for(String s : actionList) {
                    builder.addStatement(s + "()");
                }
            }

            builder.addStatement("setStatus(Status." + t.getTo() + ")");
            builder.addStatement("wasEventProcessed = true");
            builder.addStatement("break");
            if(isProbability) builder.endControlFlow();
            if(isGuard) builder.endControlFlow();
            builder.endControlFlow();
        }

        builder.endControlFlow();

        builder.addStatement("return wasEventProcessed");

        return builder.build();
    }

    private static String getMethodName(String channel) {
        StringBuilder methodName = new StringBuilder();

        for(int i=0; i<channel.length(); i++) {
            int idx = channel.charAt(i);

            if(idx >= 65 && idx <=122) {
                if (idx == 91) break;
                methodName.append(channel.charAt(i));
            }
        }
        return methodName.toString();

    }
    private static char getLastChar(String str) {
        return str.charAt(str.length()-1);
    }
    private static String getFirstChar(String str) {
        return String.valueOf(str.charAt(0));
    }

}