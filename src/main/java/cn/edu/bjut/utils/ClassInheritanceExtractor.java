package cn.edu.bjut.utils;

import cn.edu.bjut.config.ParserJavaSrcConfig;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ParseResult;


import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class ClassInheritanceExtractor {

    public static void main(String[] args) throws IOException {
        // 项目源码路径
        String projectSrcPath1 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\daytrader-ee7\\src\\main\\java";

        String projectSrcPath2 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\jPetStore\\src\\main\\java";

        ParserJavaSrcConfig parserJavaSrcConfig1 = new ParserJavaSrcConfig(projectSrcPath1);
        List<ParseResult<CompilationUnit>> compilationUnits1 = parserJavaSrcConfig1.getCompilationUnits();

        ParserJavaSrcConfig parserJavaSrcConfig2 = new ParserJavaSrcConfig(projectSrcPath2);
        List<ParseResult<CompilationUnit>> compilationUnits2 = parserJavaSrcConfig2.getCompilationUnits();

       /* traverseParserResultAndExtractClassInheritance(compilationUnits1);
        System.out.println("--------------------------------------------------------------");
        traverseParserResultAndExtractClassInheritance(compilationUnits2);*/

        parserAndGetClassName(compilationUnits1);
        System.out.println("--------------------------------------------------------------");
        parserAndGetClassName(compilationUnits2);

    }

    public static HashSet<String> parserAndGetClassName(List<ParseResult<CompilationUnit>> compilationUnits){
        HashSet<String> classNameSet = new HashSet<>();
        HashSet<String> interfaceNameSet = new HashSet<>();
        int interfaceCount = 0;
        int classCount = 0;
        // 遍历解析结果并提取类间继承关系
        for (ParseResult<CompilationUnit> result : compilationUnits) {
            if (result.isSuccessful()) {
                CompilationUnit cu = result.getResult().get();
                List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
                for (ClassOrInterfaceDeclaration clazz : classes) {
                    String className = clazz.getNameAsString();
                    String type = clazz.isInterface() ? "Interface" : "Class"; // 判断是类还是接口
                    if(type.equals("Interface")){
                        interfaceCount++;
                        interfaceNameSet.add(className);
                    }else{
                        classNameSet.add(className);
                        classCount++;
                    }
                }
            } else {
                System.out.println("Failed to parse file: " + result.getResult().get());
            }
        }
        System.out.println("classCount: " + classCount);
        System.out.println("interfaceCount: " + interfaceCount);
        return null;
    }

    public static void traverseParserResultAndExtractClassInheritance(List<ParseResult<CompilationUnit>> compilationUnits){
        // 遍历解析结果并提取类间继承关系
        for (ParseResult<CompilationUnit> result : compilationUnits) {
            if (result.isSuccessful()) {
                CompilationUnit cu = result.getResult().get();
                extractClassInheritance(cu);
            } else {
                System.out.println("Failed to parse file: " + result.getResult().get());
            }
        }
    }

    // 提取类的继承关系
    public static void extractClassInheritance(CompilationUnit cu) {
        // 获取所有的类或接口声明
        List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration clazz : classes) {
            String className = clazz.getNameAsString();
            String type = clazz.isInterface() ? "Interface" : "Class"; // 判断是类还是接口
            String superclass = "None"; // 默认为没有继承父类
            String interfaces = "None"; // 默认为没有实现接口

            // 获取继承的父类（如果有的话）
            if (clazz.getExtendedTypes().size() > 0) {
                ClassOrInterfaceType extendedType = clazz.getExtendedTypes().get(0);
                superclass = extendedType.toString(); // 将继承关系转为字符串
            }

            // 获取实现的接口（如果有的话）
            if (clazz.getImplementedTypes().size() > 0) {
                List<ClassOrInterfaceType> implementedTypes = clazz.getImplementedTypes();
                interfaces = implementedTypes.toString(); // 将实现的接口转为字符串
            }

            // 打印类名、父类及其实现的接口（如果有）
            System.out.println(type + ": " + className + " Inherits from: " + superclass + " Implements: " + interfaces);
        }
    }
}
