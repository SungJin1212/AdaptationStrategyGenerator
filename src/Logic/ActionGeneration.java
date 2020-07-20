package Logic;

import Data.Transition;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class ActionGeneration {

    public static ArrayList<MethodSpec> getAction(ArrayList<Transition> transitions, String actionCode) {

        ArrayList<MethodSpec> actions = new ArrayList<>();

        for (Transition t : transitions) {
            if(!t.getAction().equals("NoAction")) {
                String[] actionList= t.getAction().split(",");
                for(String actionName : actionList) {
                    actions.add(getEachAction(actionName,actionCode));
                }
            }
        }
        return actions;
    }

    private static MethodSpec getEachAction(String actionName, String actionCode) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(actionName).addModifiers(Modifier.PRIVATE);

        int idx = actionCode.lastIndexOf(actionName);
        if (idx != -1) {
            idx += actionName.length();
            System.out.println(actionCode.charAt(idx));
            int startIdx = 0, endIdx = 0;

            for (int i = idx; i <= actionCode.length(); i++) {
                if (isAlpha(actionCode.charAt(i))) { // find alphabet index
                    startIdx = i;
                    break;
                }
            }
            for (int i = idx; i <= actionCode.length(); i++) {
                if (actionCode.charAt(i) == '}') { // find end of the method index
                    endIdx = i;
                    break;
                }
            }
            //System.out.println(actionCode.substring(startIdx + 1, endIdx - 1));

            CodeBlock code = CodeBlock.builder().addStatement(actionCode.substring(startIdx, endIdx - 2)).build();

            builder.addCode(code);
        }

        return builder.build();
    }
    private static boolean isAlpha(char c) {
        int value = (int) c;
        boolean ret = false;
        if(value >= 65 && value <=122) ret = true;
        return ret;
    }
}
