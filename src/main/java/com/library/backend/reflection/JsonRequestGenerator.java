package com.library.backend.reflection;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.util.*;
import java.lang.reflect.Modifier;

@NoArgsConstructor
public class JsonRequestGenerator {

    private static JsonRequestGenerator instance;

    public static JsonRequestGenerator getInstance(){
        if (instance == null) {
            instance = new JsonRequestGenerator();
        }
        return instance;
    }


    public void generate(Class<?> clas) {
        if (clas.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)) {

            StringBuilder mapping = new StringBuilder();
            if(clas.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class)){
                for(String s: clas.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class).value()){
                    mapping.append(s);
                }
            }

            File folder = new File("JsonEndpoints");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File file = new File(folder, clas.getSimpleName() + ".txt");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(clas.getSimpleName() + "\n");
                for(Method m: clas.getDeclaredMethods()){
                    writer.write("\n" +getMappingMethod(m)+ " "+ m.getName() +" : " + mapping + getMappingExtendPath(m) + "\n");
                    if(m.getParameters().length != 0)
                        for(Parameter p: m.getParameters()) {
                            writer.write(this.extractParametersToJson(p));
                    }
                }

                System.out.println("Plik " + file.getAbsolutePath() + " został utworzony.");
            } catch (IOException e) {
                System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
            }
        }else {
            System.out.println(clas.getName() + " nie jest kontrolerem!");
        }
    }

    private String extractParametersToJson(Parameter p) {

        String res = "";
        String row = "";
        if(p.isAnnotationPresent(org.springframework.web.bind.annotation.RequestHeader.class)){
            res += "{\n" + this.jsonNameInType(p.getDeclaredAnnotation(RequestHeader.class).value(),
                    p.getType(), row) + "\n}\n";
        }
        else
            if(p.isAnnotationPresent(org.springframework.web.bind.annotation.RequestBody.class)){
            res += "{\n\"body\": " + jsonNameInType(p.getName(), p.getType(), row) + "\n}\n" ;
        }
        else if(p.isAnnotationPresent(org.springframework.web.bind.annotation.RequestParam.class)){
            res += "{\n" + jsonNameInType(p.getName(), p.getType(), row) + "\n}\n";
        }
        else if(p.isAnnotationPresent(org.springframework.web.bind.annotation.PathVariable.class)){
            res = "";
        }
        else {
            res = "{\n";
        }
        return res ;
    }


    private String jsonNameInType(String name, Class<?> type, String row) {
        row += row + " ";
        if(Number.class.isAssignableFrom(type)){
            return row + "\"" + name + "\": 0";
        }else if(Arrays.asList( String.class, Character.class, char.class).contains(type)){
            return row + "\"" + name + "\": \"\"";
        }else if(Arrays.asList(boolean.class).contains(type) ){
            return row + "\"" + name + "\": false";
        }else if(Arrays.asList(LocalDateTime.class).contains(type) ){
        return row + "\"" + name + "\": \"" + LocalDateTime.now() + "\"";
        }
        else{
                String res = "";
                for(int i =0; i<type.getDeclaredFields().length; i++){
                    if(Collection.class.isAssignableFrom(type.getDeclaredFields()[i].getType())){
                        res += row + row + row +"\"" + type.getDeclaredFields()[i].getName() + "\": [ "
                               + row + this.jsonCollection(type.getDeclaredFields()[i].getName(), type.getDeclaredFields()[i].getType()
                                , type, row ) + row + row + row +"]\n";
                    }else {
                        res += jsonNameInType(type.getDeclaredFields()[i].getName(),
                                    type.getDeclaredFields()[i].getType(), row );
                        if(i != type.getDeclaredFields().length - 1){res += ",\n";};
                    }
                }
                return   "\n" + row + "{\n" + res + row +"}\n" ;
        }
    }

    private String jsonCollection(String name, Class<?> type, Class<?> parentType, String row) {
        row += row + " ";
        try {
            Type fieldType = parentType.getDeclaredField(name).getGenericType();

            if (fieldType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 0) {
                    Type elementType = typeArguments[0];

                    if (elementType instanceof Class<?>) {
                        Class<?> elementClass = (Class<?>) elementType;
                       return row + jsonNameInType(elementClass.getName(), elementClass, row );
                    }
                }
            }
            else return "o";
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return "k";
    }


    public Optional<Annotation> extractMapping(Method method){
            List<Class<? extends Annotation>> expectedAnnotations = Arrays.asList(
                    PostMapping.class,
                    DeleteMapping.class,
                    PutMapping.class,
                    GetMapping.class,
                    PatchMapping.class);

            Optional<Annotation> found = Arrays.stream(method.getDeclaredAnnotations())
                    .filter(annotation -> expectedAnnotations.contains(annotation.annotationType()))
                    .findFirst();
            return found;
    }

    public String getMappingMethod(Method method){
        Optional<Annotation> found = extractMapping(method);
        return found.map(annotation ->
                annotation.annotationType().getSimpleName().replaceAll("Mapping$", "").toUpperCase(Locale.ROOT)
        ).orElse("Nieprzewidziany Mapping");
    }

    public String getMappingExtendPath(Method method) {
        Optional<Annotation> found = extractMapping(method);
        return found.map(annotation -> {
            try {
                String[] paths = (String[]) annotation.annotationType().getDeclaredMethod("path").invoke(annotation);

                if (paths.length == 0) {
                    paths = (String[]) annotation.annotationType().getDeclaredMethod("value").invoke(annotation);
                }
                String res = String.join("/...", paths);
                for(Parameter p: method.getParameters()){
                    if(p.isAnnotationPresent(org.springframework.web.bind.annotation.PathVariable.class)){
                        String reg = "{"+p.getName()+"}";
                        String rep = "{"+p.getType().getSimpleName()+ " " + p.getName()+ "}";
                        res = res.replace(reg, rep);
                    }
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }).orElse("Brak");
    }

}
