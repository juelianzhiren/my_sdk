package danny.jiang.plugin;

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import danny.jiang.asm.LifecycleClassVisitor
import groovy.io.FileType
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class LifeCycleTransform extends Transform {
    @Override
    public String getName() {
        return "LifeCycleTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        // 拿到所有的class文件
        Collection<TransformInput> transformInputs = transformInvocation.inputs;
        TransformOutputProvider outputProvider = transformInvocation.outputProvider;
        transformInputs.each {
            TransformInput transformInput ->
                transformInput.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        File dir = directoryInput.file;
                        if (dir) {
                            dir.traverse(type : FileType.FILES, nameFilter: ~/.*\.class/) {
                                File file ->
                                    System.out.println("find class: " + file.name);
                                    //对class文件进行读取与解析
                                    ClassReader classReader = new ClassReader(file.bytes);
                                    // 对class文件的写入
                                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                                    // 访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
                                    ClassVisitor classVisitor = new LifecycleClassVisitor(classWriter);
                                    //依次调用ClassVisitor接口的各个方法
                                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                                    //toByteArray方法将最终修改的字节码以byte数组形式返回
                                    byte[] bytes = classWriter.toByteArray();
                                    //通过文件流写入方式覆盖原先的内容，实现class文件的改写
                                    FileOutputStream fos = new FileOutputStream(file.path);
                                    fos.write(bytes);
                                    fos.close();
                            }
                        }
                        //处理完输入文件后输出传给下一个文件
                        def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY);
                        FileUtils.copyDirectory(directoryInput.file, dest);
                }
        }
//        for(TransformInput input: transformInputs) {
//            for(DirectoryInput directoryInput: input.getDirectoryInputs()) {
//                File dir = directoryInput.getFile();
//                if (dir != null) {
//                    if (dir.getAbsolutePath().endsWith(".class")) {
//                        System.out.println("find class: " + dir.getName());
//                    }
//                }
//            }
//        }
    }
}