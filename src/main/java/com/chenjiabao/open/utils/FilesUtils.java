package com.chenjiabao.open.utils;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件类
 */
public class FilesUtils {

    //项目目录
    public static String classesPath = System.getProperty("user.dir");

    /**
     * -- GETTER --
     *  获取文件对象
     */
    File file;

    public FilesUtils() {
        this.file = null;
    }

    public FilesUtils(String pathname) {
        this.file = new File(classesPath+pathname);
    }

    /**
     * 创建文件夹
     */
    public boolean createDirectory(){
        if(!isHasFile()){
            return this.file.mkdirs();
        }else {
            return true;
        }
    }
    public boolean createDirectory(File directory){
        if(!isHasFile()){
            return directory.mkdirs();
        }else {
            return true;
        }
    }

    /**
     * 判断文件是否存在
     */
    public boolean isHasFile(){
        //exists()方法可以校验是否存在该文件，防止抛出异常
        return this.file.exists();
    }

    /**
     * 创建文件
     */
    public boolean createFile(){
        boolean flag;
        try{
            //创建父目录，若存在
            File parentFile = this.file.getParentFile();
            if(!parentFile.exists()){
                createDirectory(parentFile);
            }
            //创建文件
            flag = this.file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(){
        return this.file.delete();
    }

    /**
     * 字符流 缓冲流 读取文件
     */
    public ArrayList<String> readerFile(String file){

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("999");

        Reader fr = null;
        BufferedReader br = null;

        try{
            fr = new FileReader(file);
            br = new BufferedReader(fr);//构造方法需要一个FileReader对象

            String data = null;
            while ((data = br.readLine()) != null){
                //readLine()方法可以一行一行地读取
                arrayList.add(data);
            }
        } catch (IOException e) {
            arrayList.set(0,"-1");//获取失败！
            e.printStackTrace();
        } finally {
            //关闭流
            closeFile(br);
            closeFile(fr);
        }

        return arrayList;
    }

    /**
     * 写入文件
     * @param append 是否开启追加模式
     */
    public boolean writerFile(String content,boolean append){
        FileWriter fw = null;
        BufferedWriter bw = null;

        try{
            fw = new FileWriter(this.file,append);
            bw = new BufferedWriter(fw);

            bw.write(content);
            bw.newLine(); // 换行
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            closeFile(bw);
            closeFile(fw);
        }
    }
    public boolean writerFile(String file,String content,boolean append){
        FileWriter fw = null;
        BufferedWriter bw = null;

        try{
            fw = new FileWriter(file,append);
            bw = new BufferedWriter(fw);

            bw.write(content);
            bw.newLine(); // 换行
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            closeFile(bw);
            closeFile(fw);
        }
    }

    /**
     *关闭流
     */
    public boolean closeFile(Reader fileReader){
        if(fileReader == null){
            return true;
        }
        try {
            fileReader.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean closeFile(BufferedReader bufferedReader){
        if(bufferedReader == null){
            return true;
        }
        try {
            bufferedReader.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean closeFile(FileWriter fileWriter){
        if(fileWriter == null){
            return true;
        }
        try {
            fileWriter.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean closeFile(BufferedWriter bufferedWriter){
        if(bufferedWriter == null){
            return true;
        }
        try {
            bufferedWriter.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

}
