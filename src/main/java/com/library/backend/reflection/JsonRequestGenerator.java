package com.library.backend.reflection;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.collection.spi.CollectionSemantics;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

@NoArgsConstructor
public class JsonRequestGenerator {


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
                    writer.write("\n" +getMappingMethod(m) +" : " + mapping + getMappingExtendPath(m) + "\n");
                    writer.write("{");
                    if(m.getParameters().length == 0){
                        writer.write("\nnone");
                    }else {
                        for(Parameter p: m.getParameters()) {
                            writer.write(this.extractParametersToJson(p));
                        }
                    }
                    writer.write("\n}\n");
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
            res += "\n" + this.JsonNameInType(p.getDeclaredAnnotation(RequestHeader.class).value(),
                    p.getType(), row);
        }
        else if(p.isAnnotationPresent(org.springframework.web.bind.annotation.RequestBody.class)){
            res += "\n\"body\": " + JsonNameInType(p.getName(), p.getType(), row) ;
        }
        else {
            res = "{\n";
        }
        return res ;
    }


    private String JsonNameInType(String name, Class<?> type, String row) {
        row += row + " ";
        if(Number.class.isAssignableFrom(type)){
            return row + "\"" + name + "\": 0";
        }else if(Arrays.asList( String.class, Character.class, char.class).contains(type)){
            return row + "\"" + name + "\": \"\"";
        }else if(Arrays.asList(boolean.class).contains(type) ){
            return row + "\"" + name + "\": false";
        }
        else{
                String res = "";
                for(int i =0; i<type.getDeclaredFields().length; i++){
                    if(Collection.class.isAssignableFrom(type.getDeclaredFields()[i].getType())){
                        res += row + row + row +"\"" + type.getDeclaredFields()[i].getName() + "\": [ "
                               + row + this.JsonCollection(type.getDeclaredFields()[i].getName(), type.getDeclaredFields()[i].getType()
                                , type, row ) + row + row + row +"]\n";
                    }else {
                        res += JsonNameInType(type.getDeclaredFields()[i].getName(),
                                type.getDeclaredFields()[i].getType(), row );
                        if(i != type.getDeclaredFields().length - 1){res += ",\n";};
                    }
                }
                return   "\n" + row + "{\n" + res + row +"}\n" ;
        }
    }

    private String JsonCollection(String name, Class<?> type, Class<?> parentType, String row) {
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
                       return row + JsonNameInType(elementClass.getName(), elementClass, row );
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

                return String.join("/", paths);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }).orElse("Brak");
    }

}
