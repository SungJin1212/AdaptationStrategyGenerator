package Logic;

import Data.LocalVariable;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalVariableGeneration {

    public static ArrayList<FieldSpec> getLocalFieldSpec(ArrayList<String> parsedLocalVariables) {
        ArrayList<String> types = new ArrayList<>(Arrays.asList("int", "float", "double", "String", "char"));
        String curType, preType = "", name;
        ArrayList<String> secondParsedLocalVariables = new ArrayList<>();
        ArrayList<FieldSpec> localFields = new ArrayList<>();
        ArrayList<LocalVariable> LocalVariables = new ArrayList<>();

        for(String statement : parsedLocalVariables) { // ex) statement = "int v1,v2"
            secondParsedLocalVariables.addAll(Arrays.asList(statement.split(",")));
        }

        for(String s : secondParsedLocalVariables) {
            String[] data = s.split(" ");
            if(data[0].contains("\n")) {
                data[0] = data[0].substring(1);
            }
            if(types.contains(data[0])) {
                curType = data[0];
                name = data[1];
                preType = curType;
            }
            else {
                curType = preType;
                name = data[0];
            }
            LocalVariables.add(new LocalVariable(curType,name));
        }

        for(LocalVariable eachVariable : LocalVariables) {
            localFields.add(FieldSpec.builder(TypeVariableName.get(eachVariable.getType()), eachVariable.getName()).addModifiers(Modifier.PRIVATE).build());
        }

        return localFields;
    }

}
