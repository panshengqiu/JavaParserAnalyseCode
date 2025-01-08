package cn.edu.bjut.utils;


import cn.edu.bjut.config.ParserJavaSrcConfig;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.IOException;
import java.util.List;

public class ClassAndMethodExtractor {
    public static void main(String[] args) throws IOException {
        // 项目源码路径
        String projectSrcPath1 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\daytrader-ee7\\src\\main\\java";

        String projectSrcPath2 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\jPetStore\\src\\main\\java";

        ParserJavaSrcConfig parserJavaSrcConfig1 = new ParserJavaSrcConfig(projectSrcPath1);
        List<ParseResult<CompilationUnit>> compilationUnits1 = parserJavaSrcConfig1.getCompilationUnits();

        ParserJavaSrcConfig parserJavaSrcConfig2 = new ParserJavaSrcConfig(projectSrcPath2);
        List<ParseResult<CompilationUnit>> compilationUnits2 = parserJavaSrcConfig2.getCompilationUnits();

        traverseParserResultAndExtractClassMethodName(compilationUnits1);
        System.out.println("--------------------------------------------------------------");
        traverseParserResultAndExtractClassMethodName(compilationUnits2);
    }

    public static void traverseParserResultAndExtractClassMethodName(List<ParseResult<CompilationUnit>> compilationUnits){
        // 遍历解析结果并提取类间继承关系
        for (ParseResult<CompilationUnit> result : compilationUnits) {
            if (result.isSuccessful()) {
                CompilationUnit cu = result.getResult().get();
                extractClassAndMethods(cu);
            } else {
                System.out.println("Failed to parse file: " + result.getResult().get());
            }
        }
    }

    // 提取类及方法名称
    public static void extractClassAndMethods(CompilationUnit cu) {
        // 获取所有类声明
        List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);

        for (ClassOrInterfaceDeclaration clazz : classes) {
            // 获取类名
            String className = clazz.getNameAsString();
            System.out.println("Class: " + className);

            // 获取类中的所有方法
            List<MethodDeclaration> methods = clazz.getMethods();
            for (MethodDeclaration method : methods) {
                String methodName = method.getNameAsString();
                System.out.println("  Method: " + methodName);
            }
        }
    }
}

