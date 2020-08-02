package CodeGeneration.CodeGenerationLogic;

import CodeGeneration.XMLParseDataType.State;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class TimeVariableGeneration {

    public static ArrayList<FieldSpec> getTimeFieldSpec(ArrayList<State> states) {

        String timeName = "";
        ArrayList<FieldSpec> timeFields = new ArrayList<>();

        for(State s : states) {
            if (!s.getTime().equals("1")) { // if the time is not 1
                timeName = s.getStateName() + "Time";
                timeFields.add(FieldSpec.builder(TypeName.INT, timeName).addModifiers(Modifier.PRIVATE).build());
            }
        }
        return timeFields;
    }

}
