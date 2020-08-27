package CodeGeneration.CodeGenerationLogic.Behavior;

import CodeGeneration.XMLParseDataType.Behavior.ActionDescription;
import CodeGeneration.XMLParseDataType.Behavior.Transition;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashSet;

public class ActionCodeGeneration {

    public static ArrayList<MethodSpec> getAction(ArrayList<Transition> transitions, ArrayList<ActionDescription> actionCodes) {

        ArrayList<MethodSpec> actions = new ArrayList<>();

        HashSet<String> names = new HashSet<>(0);

        for(Transition t : transitions) {
            String[] actionList= t.getAction().split(",");
            for(String s : actionList) {
                if(!s.equals("NoAction")) {
                    names.add(s);
                }
            }
        }
        for(String s : names) {
            actions.add(getEachAction(s,actionCodes));
        }
//
//        for (Transition t : transitions) {
//            if(!t.getAction().equals("NoAction")) {
//                String[] actionList= t.getAction().split(",");
//                for(String actionName : actionList) {
//                    actions.add(getEachAction(actionName,actionCode));
//                }
//            }
//        }
        return actions;
    }

    private static MethodSpec getEachAction(String actionName, ArrayList<ActionDescription> actionCodes) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(actionName).addModifiers(Modifier.PRIVATE);

        for(ActionDescription actionDescription : actionCodes) {
            if(actionDescription.getActionName().equals(actionName)) {
                String effectCode = actionDescription.getEffect();
                CodeBlock code = CodeBlock.builder().addStatement(effectCode.substring(0, effectCode.length()-1)).build(); // 마지막 ";" 제외하고 생성
                builder.addCode(code);
            }
        }

//        int idx = actionCode.lastIndexOf(actionName);
//        if (idx != -1) {
//            idx += actionName.length();
//            //System.out.println(actionCode.charAt(idx));
//            int startIdx = 0, endIdx = 0;
//
//            for (int i = idx; i <= actionCode.length(); i++) {
//                if (isAlpha(actionCode.charAt(i))) { // find alphabet index
//                    startIdx = i;
//                    break;
//                }
//            }
//            for (int i = idx; i <= actionCode.length(); i++) {
//                if (actionCode.charAt(i) == '}') { // find end of the method index
//                    endIdx = i;
//                    break;
//                }
//            }
//            //System.out.println(actionCode.substring(startIdx + 1, endIdx - 1));
//
//            CodeBlock code = CodeBlock.builder().addStatement(actionCode.substring(startIdx, endIdx - 2)).build();


        return builder.build();
    }
    private static boolean isAlpha(char c) {
        int value = (int) c;
        boolean ret = false;
        if(value >= 65 && value <=122) ret = true;
        return ret;
    }
}
