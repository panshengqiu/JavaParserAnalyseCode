package cn.edu.bjut.test;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InternalClassCallAnalyzer {
    public static void main(String[] args) throws Exception {
        // 系统源码目录（根目录）
        String projectSrcPath1 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\daytrader-ee7\\src\\main\\java";
        String projectSrcPath2 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\jPetStore\\src\\main\\java";

        // 配置 JavaParser 的符号解析器
        // 设置类型解析器
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(Paths.get(projectSrcPath1)));

        // 设置JavaParser解析器
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        // 指定Spring Boot项目的源代码目录
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectSrcPath1));
        sourceRoot.getParserConfiguration().setSymbolResolver(symbolSolver);
        // 解析项目中的所有Java文件
        List<ParseResult<CompilationUnit>> compilationUnits = sourceRoot.tryToParse("");
        // 访问所有类和方法调用
        Set<String> calledClasses = new HashSet<>();
        compilationUnits.forEach(cu -> cu.ifSuccessful(compilationUnit ->
                compilationUnit.getTypes().stream()
                        .forEach(type -> {
                            // 遍历方法，查找方法调用
                            type.findAll(MethodDeclaration.class).forEach(method -> {
                                method.findAll(MethodCallExpr.class).forEach(methodCall -> {
                                    // 尝试解析调用的方法
                                    try {
                                        ResolvedMethodDeclaration resolvedMethod = methodCall.resolve();
                                        String declaringClass = resolvedMethod.getPackageName() + "." + resolvedMethod.getClassName();
                                        if(declaringClass.contains("com.ibm.websphere.samples.daytrader"))
                                            calledClasses.add(declaringClass);
                                    } catch (Exception e) {
                                        // 忽略解析失败的调用

                                    }
                                });
                            });
                        })));

        // 打印结果
//        System.out.println("Classes called by " + targetClassName + ":");
        calledClasses.forEach(System.out::println);
        System.out.println("size: " + calledClasses.size());
    }
}
