package CodeGeneration.CodeGenerationLogic;

import CodeGeneration.XMLParseDataType.State;
import CodeGeneration.XMLParseDataType.Synchronization;
import CodeGeneration.XMLParseDataType.Transition;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Set;

public class RunGeneration {

    public static MethodSpec getRun(ArrayList<State> states, ArrayList<Transition> transitions, Set<Synchronization> allTransition) {
        double temp;
        MethodSpec.Builder runCode = MethodSpec.methodBuilder("run").addModifiers(Modifier.PUBLIC);

        runCode.addStatement("Status aStatus = status");
        runCode.beginControlFlow("switch(aStatus)");

        for(State s : states) {


            ArrayList<Transition> tempTransitions = new ArrayList<>(0);
            for(Transition t : transitions) {
                if(s.getStateName().equals(t.getFrom())) {
                    tempTransitions.add(t);
                }
            }

            runCode.beginControlFlow("case " + s.getStateName() + ":");
            if(!s.getTime().equals("1")) { // Time 1이 아니면.
                runCode.beginControlFlow("if(--$NTime == 0)", s.getStateName());
            }
            if(!s.getTime().equals("1")) { // Time 1이 아니면.
                runCode.addStatement("$NTime = $N", s.getStateName(), s.getTime());
            }
            if (tempTransitions.size() == 1) {
                Transition t = tempTransitions.get(0);
                if(!t.getProbability().equals("1")) {
                    runCode.beginControlFlow("if(Math.random() < $N)", t.getProbability());
                }
                if (t.getTrigger().contains("!")) {
                    String call = getSendCallStatement(allTransition, t);
                    runCode.addStatement(call);
                    // 같은 이름인 채널 call. 타입검사 해야됨.
                }
                else{
                    runCode.addStatement(t.getTrigger() + "()");
                }

                runCode.addStatement("break");
                if(!t.getProbability().equals("1")) {
                    runCode.endControlFlow();
                }
            }
            else if (tempTransitions.size() >= 2 && tempTransitions.get(0).getProbability().equals("1")) { //확률이 모두다 1 이지만 guard 로 precondition을 체크해야 하는 경우
                for (Transition t : tempTransitions) {
                    runCode.beginControlFlow("if ($N)",t.getGuard());
                    if (t.getTrigger().contains("!")) {
                        String call = getSendCallStatement(allTransition, t);
                        runCode.addStatement(call);
                        // 같은 이름인 채널 call. 타입검사 해야됨.
                    }
                    else{
                        runCode.addStatement(t.getTrigger() + "()");
                    }
                    if(!s.getTime().equals("1")) { // Time 1이 아니면.
                        runCode.addStatement("$NTime = $N", s.getStateName(), s.getTime());
                    }
                    runCode.addStatement("break");
                    runCode.endControlFlow();
                }
            }
            else { //확률이 1미만인 transition 들
                temp = 0;
                runCode.addStatement("double pro = Math.random()");
                for(Transition t : tempTransitions) {
                    if(temp == 0) {
                        runCode.beginControlFlow("if(pro < $N)", t.getProbability());
                        if (t.getTrigger().contains("!")) {
                            String call = getSendCallStatement(allTransition, t);
                            runCode.addStatement(call);
                            // 같은 이름인 채널 call. 타입검사 해야됨.
                        }
                        else{
                            runCode.addStatement(t.getTrigger() + "()");
                        }
                        if(!s.getTime().equals("1")) { // Time 1이 아니면.
                            runCode.addStatement("$NTime = $N", s.getStateName(), s.getTime());
                        }                        runCode.addStatement("break");
                        temp += Double.parseDouble(t.getProbability());
                        runCode.endControlFlow();
                    }
                    else{
                        runCode.beginControlFlow("else if(pro > $N && pro <= $N)", String.valueOf(temp), String.valueOf(temp + Double.parseDouble(t.getProbability())));
                        if (t.getTrigger().contains("!")) {
                            String call = getSendCallStatement(allTransition, t);
                            runCode.addStatement(call);
                            // 같은 이름인 채널 call. 타입검사 해야됨.
                        }
                        else{
                            runCode.addStatement(t.getTrigger() + "()");
                        }
                        if(!s.getTime().equals("1")) { // Time 1이 아니면.
                            runCode.addStatement("$NTime = $N", s.getStateName(), s.getTime());
                        }                        runCode.addStatement("break");
                        temp += Double.parseDouble(t.getProbability());
                        runCode.endControlFlow();
                    }
                }

            }
            if(!s.getTime().equals("1")) { // Time 1이 아니면.
                runCode.endControlFlow();
            }
            runCode.endControlFlow();
        }



        runCode.endControlFlow();
        return runCode.build();
    }

    private static String getSendCallStatement(Set<Synchronization> allTransition, Transition t) {
        String call = "";
        ArrayList<Synchronization> callee = new ArrayList<>(0);
        for(Synchronization syn : allTransition) {
            if (isSameChannel(t.getTrigger(), syn.getTrigger())) {
                callee.add(syn);
            }
        }
        for (Synchronization syn : callee) {
            call += getChannelName(syn.getTrigger()) + "((" + syn.getName() + ")passiveEnvironmentModelList.get(String.format("
                    +"\"" + syn.getName() + "(" + getChannelParameter(syn.getTrigger()) + ")))";
        }
        return call;
    }

    private static String getChannelParameter(String trigger) {

        System.out.println(trigger);
        String parameters = "";
        String format = "";

        for(int i =0; i<trigger.length(); i++) {
            char c = trigger.charAt(i);

            if(c == '[') {
                format += "%d";
                parameters += trigger.charAt(i+1);
                format += ",";
                parameters += ",";
            }
        }

        String ret = format.substring(0,format.length()-1) + ")" + "\"" + "," + parameters.substring(0,parameters.length()-1);

        return ret;
    }

    private static String getChannelName(String trigger) {
        int idx = trigger.indexOf("[");

        return trigger.substring(0,idx);
    }


    private static boolean isSameChannel(String send, String receive) {
        if (send.substring(0,send.length() - 1).equals(receive.substring(0,receive.length()-1))) {
            return true;
        }
        else {
            return false;
        }
    }

}
