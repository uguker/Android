package com.uguke.android.util;

import java.io.*;

/**
 * 文件工具类
 * @author LeiJue
 */
public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("can't instantiate me...");
    }

    public static boolean isExists(String path) {
        return !CheckUtils.isEmpty(path) && isExists(new File(path));
    }

    public static boolean isExists(File file) {
        return file != null && file.exists();
    }

    public static long length(String path) {
        return CheckUtils.isEmpty(path) ? -1 : length(new File(path));
    }

    public static long length(File file) {
        long totalSize = -1;
        if (file != null && file.exists()) {
            totalSize = 0;
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children != null) {
                    for (File f : children) {
                        totalSize += length(f);
                    }
                }
            } else {
                totalSize += file.length();
            }
        }
        return totalSize;
    }

    public static boolean mkDirs(String dir) {
        return !CheckUtils.isEmpty(dir) && mkDirs(new File(dir));
    }

    public static boolean mkDirs(File dir) {
        return dir != null && !dir.exists() && dir.mkdirs();
    }

    public static boolean mkFile(String path) {
        return !CheckUtils.isEmpty(path) && mkFile(new File(path));
    }

    public static boolean mkFile(File file) {
        return mkNewFile(file, false);
    }

    public static boolean mkNewFile(String path) {
        return !CheckUtils.isEmpty(path) && mkNewFile(new File(path));
    }

    public static boolean mkNewFile(File file) {
        return mkNewFile(file, true);
    }

    public static boolean mkNewFile(String path, boolean createNewFile) {
        return !CheckUtils.isEmpty(path) && mkNewFile(new File(path), createNewFile);
    }

    public static boolean mkNewFile(File file, boolean createNewFile) {
        if (file != null) {
            File parent = file.getParentFile();
            if (parent.exists() && createNewFile) {
                file.delete();
            } else if (!parent.exists()) {
                if (!mkDirs(file.getParentFile())) {
                    return false;
                }
            } else {
                return true;
            }
            try {
                return file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean delete(String path) {
        return !CheckUtils.isEmpty(path) && delete(new File(path));
    }

    public static boolean delete(File file) {
        if (isExists(file)) {
            if (file.isFile()) {
                return file.delete();
            }
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        if (!delete(f)) {
                            return false;
                        }
                    } else {
                        if (!f.delete()) {
                            return false;
                        }
                    }
                }
            }
            return file.delete();
        }
        return false;
    }

    public static boolean rename(String sourcePath, String newName) {
        return !CheckUtils.isEmpty(sourcePath) && rename(new File(sourcePath), newName);
    }

    public static boolean rename(File source, String newName) {
        return !(!isExists(source) || CheckUtils.isEmpty(newName)) && source.renameTo(new File(source.getParent(), newName));
    }

    public static boolean move(String sourcePath, String dirPath) {
        return !CheckUtils.isEmpty(sourcePath, dirPath) && move(new File(sourcePath), new File(dirPath));
    }

    public static boolean move(File source, File dir) {
        if (!isExists(source)) {
            return false;
        }
        if (!isExists(dir)) {
            mkDirs(dir);
        }
        return source.renameTo(new File(dir, source.getName()));
    }

    public static boolean copy(String sourcePath, String dirPath) {
        return !CheckUtils.isEmpty(sourcePath, dirPath) && copy(new File(sourcePath), new File(dirPath));
    }

    public static boolean copy(File source, File dir) {
        if (!isExists(source) || dir == null) {
            return false;
        }
        if (!isExists(dir)) {
            dir.mkdirs();
        }
        if (source.isFile()) {
            return copyFile(source, dir);
        } else {
            File newDir = new File(dir, source.getName());
            if (newDir.exists()) {
                return false;
            }
            if (mkDirs(newDir)) {
                File [] files = source.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (!copyFile(f, newDir)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 复制文件
     * @param source 源文件
     * @param dir    目标文件夹
     */
    private static boolean copyFile(File source, File dir) {
        if (!isExists(source) || dir == null) {
            return false;
        }
        if (isExists(new File(dir, source.getName()))) {
            return false;
        }
        if (!isExists(dir)) {
            dir.mkdirs();
        }
        try {
            //创建输入流对象
            FileInputStream fis = new FileInputStream(source);
            //创建输出流对象
            FileOutputStream fos = new FileOutputStream(new File(dir, source.getName()));
            //创建搬运工具
            byte [] bytes = new byte[1024 * 8];
            //创建长度
            int len = 0;
            //循环读取数据
            while((len = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
