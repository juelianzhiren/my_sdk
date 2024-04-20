package com.ztq.sdk.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    public static void unzipRecur(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // 解压文件
                    extractFile(zipIn, filePath);
                } else {
                    // 创建目录
                    File dirNew = new File(filePath);
                    dirNew.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    public static boolean zipSingleFile(String singleFiePath, String outZipPath) {
        try {
            //输出压缩包
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            //被压缩文件
            File fileToZip = new File(singleFiePath);
            FileInputStream fis = new FileInputStream(fileToZip);

            //向压缩包中添加文件
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[4096];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.close();
            fis.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean zipMultiFiles(List<String> srcFiles, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            //向压缩包中添加多个文件
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                if (fileToZip.isDirectory()) {
                    zipDirectory(srcFile, zipOut);
                    continue;
                }
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[4096];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean zipDirectory(String dirPath, String outZipPath) {
        try {
            //压缩结果输出，即压缩包
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(dirPath);
            //递归压缩文件夹
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            //关闭输出流
            zipOut.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean zipDirectory(String dirPath, ZipOutputStream zipOut) {
        try {
            //压缩结果输出，即压缩包
            File fileToZip = new File(dirPath);
            //递归压缩文件夹
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            //关闭输出流
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将fileToZip文件夹及其子目录文件递归压缩到zip文件中
     *
     * @param fileToZip 递归当前处理对象，可能是文件夹，也可能是文件
     * @param fileName  fileToZip文件或文件夹名称
     * @param zipOut    压缩文件输出流
     * @throws IOException
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        //不压缩隐藏文件夹
        if (fileToZip.isHidden()) {
            return;
        }
        //判断压缩对象如果是一个文件夹
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                //如果文件夹是以“/”结尾，将文件夹作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                //如果文件夹不是以“/”结尾，将文件夹结尾加上“/”之后作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            //遍历文件夹子目录，进行递归的zipFile
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            //如果当前递归对象是文件夹，加入ZipEntry之后就返回
            return;
        }
        //如果当前的fileToZip不是一个文件夹，是一个文件，将其以字节码形式压缩到压缩包里面
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[4096];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}