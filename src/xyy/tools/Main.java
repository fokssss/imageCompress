package xyy.tools;

import java.io.*;

public class Main {


    private static FileFilter jpgFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String s = pathname.getName().toLowerCase();
            return s.endsWith(".jpg") || s.endsWith(".png");
        }
    };

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("imageCompress srcDir toDir");
            return;
        }

        String src = args[0];
        String obj = args[1];
//        String src = "/Users/xyy/githubs/backup/";
//        String obj = "/Users/xyy/githubs/test/";
        System.out.println(src);
        System.out.println(obj);
        System.out.println("------------------------------------------------");
        File objDir = new File(obj);
        if (objDir.exists()) {
            if (!objDir.isDirectory()) {
                System.out.println("来源目录已经存在且不是一个目录 - " + obj);
                return;
            }
        } else {
            if (!objDir.mkdirs()) {
                System.out.println("创建目录失败 - " + obj);
                return;
            }
        }
        File srcDir = new File(src);

//        try {
//            FileWriter writer = new FileWriter(obj + "compress.log");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File[] files = srcDir.listFiles(jpgFilter);
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            File from = files[i];
            String name = from.getName();
            File objFile = new File(obj + name);
            if (!objFile.exists()) {
                count++;
                try {
                    if (!ImageZipUtil.zipImageFile(from, objFile, 1600, 0, 0.7f)) {
                        System.out.println("copy - " + objFile.getName());
                        copyFile(from, objFile, false);
                    }
                } catch (Exception e) {
                    System.out.println("compress error - " + from.getName());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("------------------------------------------------");
        System.out.println("compress finish - " + count);
    }

    public static boolean copyFile(File srcFile, File destFile,
                                   boolean overlay) {
        String MESSAGE = "";

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        // 判断目标文件是否存在
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                destFile.delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
