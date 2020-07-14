package Logic;

import Data.State;
import Data.Transition;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class BasicSkeletonGeneration {

    public static MethodSpec getSetter() {
        CodeBlock setterCode = CodeBlock.builder().addStatement("status = aStatus").build();
        return MethodSpec.methodBuilder("setStatus").addParameter(TypeVariableName.get("Status"), "aStatus").addCode(setterCode).addModifiers(Modifier.PRIVATE).build();
    }

    public static MethodSpec getConstructor(String className, String initialStateName) {
        CodeBlock constructorCode = CodeBlock.builder().addStatement("setStatus(Status." + initialStateName + ")").build();
        return MethodSpec.methodBuilder(className).addCode(constructorCode).build();
    }

    public static MethodSpec getGetter() {
        CodeBlock getterCode = CodeBlock.builder().addStatement("return status").build();
        return MethodSpec.methodBuilder("getStatus").addModifiers(Modifier.PUBLIC).returns(TypeVariableName.get("Status")).addCode(getterCode).build();
    }

    public static TypeSpec enumGeneration(ArrayList<State> states) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder("Status").addModifiers(Modifier.PUBLIC);
        for (State s : states) {
            builder.addEnumConstant(s.getStateName());
        }
        return builder.build();
    }

    public static String getInitialStateName(ArrayList<State> states) {
        String Name = "";
        for (State s : states) {
            if(s.getInitialState().equals("1")){
                Name = s.getStateName();
            }
        }
        return Name;
    }
    public static ArrayList<FieldSpec> getFieldSpec(ArrayList<Transition> transitions) {
        ArrayList<FieldSpec> fields = new ArrayList<>();

        FieldSpec fieldSpec = FieldSpec.builder(TypeVariableName.get("Status"), "status").addModifiers(Modifier.PRIVATE).build();

        fields.add(fieldSpec);

        for(Transition t : transitions) {
            if(!t.getGuard().equals("")) {
                fields.add(getGuardField(t.getGuard()));
            }
        }
        return fields;
    }
    private static FieldSpec getGuardField(String name) {
        FieldSpec.Builder builder = FieldSpec.builder(TypeName.BOOLEAN, name).addModifiers(Modifier.PRIVATE);

        return builder.build();
    }

}
