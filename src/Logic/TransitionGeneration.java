package Logic;

import Data.Transition;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TransitionGeneration {


    public static ArrayList<MethodSpec> getTransition(ArrayList<Transition> transitions) {

        ArrayList<MethodSpec> trans = new ArrayList<>();
        Set<String> transitionNames = new HashSet<>();

        for(Transition t : transitions) {
            transitionNames.add(t.getTrigger());
        }

        Iterator<String> it = transitionNames.iterator();

        while(it.hasNext()) {
            trans.add(getEachTransition(transitions,it.next()));
        }

        return trans;

    }

    private static MethodSpec getEachTransition(ArrayList<Transition> transitions, String name) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name).addModifiers(Modifier.PUBLIC).returns(boolean.class);

        ArrayList<Transition> curTransitions = new ArrayList<>();

        for(Transition t : transitions) {
            if(t.getTrigger().equals(name)) {
                curTransitions.add(t);
            }
        }

        CodeBlock transitionCode = getEachTransitionCode(curTransitions);

        builder.addCode(transitionCode);
        return builder.build();

    }

    private static CodeBlock getEachTransitionCode(ArrayList<Transition> curTransitions) {
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

            if(isProbability) {
                builder.beginControlFlow("if(Math.random() < " + t.getProbability() + ")");
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
}