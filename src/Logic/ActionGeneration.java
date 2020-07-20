package Logic;

import Data.Transition;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class ActionGeneration {

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
}
