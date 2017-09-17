package com.jusenr.toolslibrary.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * File manipulation tool class
 * Created by guchenkai on 2015/12/10.
 */
public final class FileUtils {
    public static final String TAG = FileUtils.class.getSimpleName();
    public static final int BYTE = 1024;

    /**
     * Formatting unit
     *
     * @param size files size
     * @return Format files size
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / BYTE;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / BYTE;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / BYTE;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / BYTE;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * Gets the cache file size
     *
     * @param context context
     * @return Cache file size
     */
    public static String getTotalCacheSize(Context context) {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * Clear the application cache file
     *
     * @param context context
     */
    public static void clearAllCache(Context context) {
        delete(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            delete(context.getExternalCacheDir());
        }
    }

    /**
     * 获取文件大小
     *
     * @param file file
     * @return file sizr
     * @throws Exception exception
     */
    //Context.getExternalFilesDir (/files/) package name your application directory -- > SDCard/Android/data/, generally put some long time saved data.
    //(Context.getExternalCacheDir) -- > SDCard/Android/data/ your application package name /cache/ directory, the general store temporary data cache.
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // If there are more files
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Read file content
     *
     * @param file    file
     * @param charset charset
     * @return file content
     */
    public static String readFile(File file, String charset) {
        String fileContent = "";
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), charset);
            BufferedReader reader = new BufferedReader(read);
            String line = "";
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i == 0)
                    fileContent = line;
                else
                    fileContent = fileContent + "\n" + line;
                i++;
            }
            read.close();
        } catch (Exception e) {
            Log.e("Error reading file", e.getMessage());
        }
        return fileContent;
    }

    /**
     * Read file content
     *
     * @param file file
     * @return file content
     */
    public static String readFile(File file) {
        return readFile(file, "UTF-8");
    }

    /**
     * Get the SHA1 value of the file
     *
     * @param file file
     * @return The SHA1 value of the file
     */
    public static String getSHA1ByFile(File file) {
        if (file == null || !file.exists()) return "FileNotFoundException";
        long time = System.currentTimeMillis();
        InputStream in = null;
        String value = null;
        try {
            in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            int numRead = 0;
            while (numRead != -1) {
                numRead = in.read(buffer);
                if (numRead > 0) digest.update(buffer, 0, numRead);
            }
            byte[] sha1Bytes = digest.digest();
            String t = new String(buffer);
            value = convertHashToString(sha1Bytes);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }
        return value;
    }

    /**
     * byte[] to  String
     *
     * @param hashBytes byte[]
     * @return string
     */
    private static String convertHashToString(byte[] hashBytes) {
        String returnVal = "";
        for (int i = 0; i < hashBytes.length; i++) {
            returnVal += Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toLowerCase();
    }

    /**
     * Gets the file name of the uploaded file
     *
     * @param filePath filePath
     * @return file name
     */
    public static String getFileName(String filePath) {
        String filename = new File(filePath).getName();
        if (filename.length() > 80) {
            filename = filename.substring(filename.length() - 80, filename.length());
        }
        return filename;
    }

    /**
     * mkdirs
     *
     * @param mkdirs mkdirs
     * @return result [boolean]
     */
    public static boolean createMkdirs(File mkdirs) {
        return mkdirs.mkdirs();
    }

    /**
     * mkdirs
     *
     * @param file file
     * @return result [boolean]
     */
    public static boolean createFile(File file) {
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Get file name
     *
     * @param url url
     * @return file name
     */
    public static String getDownloadFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * Get the picture saved directory
     *
     * @param context context
     * @return picture saved directory
     */
    public static String getPicDirectory(Context context) {
        File picFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picFile != null) {
            return picFile.getAbsolutePath();
        } else {
            return context.getFilesDir().getAbsolutePath() + "/Pictures";
        }
    }

    /**
     * Unzip the assets zip compressed file to the specified directory
     *
     * @param context       context
     * @param assetName     zip file names
     * @param outputDirPath outputDirPath
     * @param isReWrite     param cover
     * @throws IOException exception
     */
    public static void unZipInAsset(Context context, String assetName, String outputDirPath, boolean isReWrite) throws IOException {
        // Creating a decompression target directory.
        File file = new File(outputDirPath);
        // If the destination directory does not exist, create.
        if (!file.exists()) file.mkdirs();
        // Open zip file.
        InputStream inputStream = ResourcesUtils.getAssets(context).open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // Read an entry point.
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // Using 1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // Byte count at decompression
        int count = 0;
        // If the entry point is empty that has all the traversal of compressed files and directories.
        while (zipEntry != null) {
            // If it is a directory
            if (zipEntry.isDirectory()) {
                file = new File(outputDirPath + File.separator + zipEntry.getName());
                // The file needs to be covered or the file does not exist
                if (isReWrite || !file.exists()) file.mkdir();
            } else {
                // If it is a file
                file = new File(outputDirPath + File.separator + zipEntry.getName());
                // The file needs to be overridden or the file does not exist, then unpack the file.
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0)
                        fileOutputStream.write(buffer, 0, count);
                    fileOutputStream.close();
                }
            }
            // Locate next file entry.
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * Unzip the assets zip compressed file to the specified directory
     *
     * @param zipPath       zip file Path
     * @param outputDirPath outputDirPath
     * @param isReWrite     param cover
     * @throws IOException exception
     */
    public static void unZipInSdCard(String zipPath, String outputDirPath, boolean isReWrite) throws IOException {
        // Creating a decompression target directory.
        File file = new File(outputDirPath);
        // If the destination directory does not exist, create.
        if (!file.exists()) file.mkdirs();
        // Open zip file.
        InputStream inputStream = new FileInputStream(new File(zipPath));
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // Read an entry point.
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // Using 1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // Byte count at decompression
        int count = 0;
        // If the entry point is empty that has all the traversal of compressed files and directories.
        while (zipEntry != null) {
            // If it is a directory
            if (zipEntry.isDirectory()) {
                file = new File(outputDirPath + File.separator + zipEntry.getName());
                // The file needs to be covered or the file does not exist
                if (isReWrite || !file.exists()) file.mkdir();
            } else {
                // If it is a file
                file = new File(outputDirPath + File.separator + zipEntry.getName());
                // The file needs to be overridden or the file does not exist, then unpack the file
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0)
                        fileOutputStream.write(buffer, 0, count);
                    fileOutputStream.close();
                }
            }
            // Locate next file entry.
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * Delete file
     *
     * @param filePath filePath
     * @return Delete redult [boolean]
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        return delete(file);
    }

    /**
     * Delete file
     *
     * @param file file
     * @return Delete redult [boolean]
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists()) return false;
        if (file.isFile()) {
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            to.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0)
                for (File innerFile : files) {
                    delete(innerFile);
                }
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            return to.delete();
        }
        return false;
    }

    /**
     * Get file content
     *
     * @param filePath filePath
     * @return file content
     */
    public static String getFileContent(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));//Construct a BufferedReader class to read the file.
                String result = null;
                String s = null;
                while ((s = br.readLine()) != null) {//Using the readLine method, read one line at a time.
                    result = result + "\n" + s;
                }
                br.close();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Save text to file
     *
     * @param fileName fileName
     * @param content  content
     * @param append   append
     * @return if you are successful [boolean]
     */
    public static boolean saveTextValue(String fileName, String content, boolean append) {
        try {
            File textFile = new File(fileName);
            if (!append && textFile.exists()) textFile.delete();
            FileOutputStream os = new FileOutputStream(textFile);
            os.write(content.getBytes("UTF-8"));
            os.close();
        } catch (Exception ee) {
            return false;
        }
        return true;
    }

    /**
     * Delete all files in the directory
     *
     * @param Path path
     */
    public static void deleteAllFile(String Path) {
        // Delete all files in the directory
        File path = new File(Path);
        File files[] = path.listFiles();
        if (files != null)
            for (File tfi : files) {
                if (tfi.isDirectory())
                    System.out.println(tfi.getName());
                else
                    tfi.delete();
            }
    }

    /**
     * save file
     *
     * @param in       file input stream
     * @param filePath output directory
     */
    public static File saveFile(InputStream in, String filePath) {
        File file = new File(filePath);
        byte[] buffer = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        try {
            FileUtils.createFile(file);
            fos = new FileOutputStream(file);
            while ((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return file;
    }

    /**
     * file exists
     *
     * @param path path
     * @return file exists
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * File Base64 encryption
     *
     * @param path path
     * @return Base64 encryption string
     */
    public static String fileToBase64String(String path) {
        FileInputStream inputStream = null;
        try {
            File file = new File(path);
            inputStream = new FileInputStream(file);
            byte[] fileBytes = new byte[inputStream.available()];
            inputStream.read(fileBytes);
            String base64String = Base64.encodeToString(fileBytes, Base64.DEFAULT);
            return base64String;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
