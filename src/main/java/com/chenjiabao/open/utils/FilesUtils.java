package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.enums.RequestCode;
import com.chenjiabao.open.utils.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * 文件类
 * @author ChenJiaBao
 */
public class FilesUtils {

    //项目目录
    public static String classesPath = System.getProperty("user.dir");
    private final LibraryProperties properties;
    private final TimeUtils timeUtils;
    private final StringUtils stringUtils;

    @Autowired
    public FilesUtils(LibraryProperties libraryProperties, TimeUtils timeUtils, StringUtils stringUtils) {
        this.properties = libraryProperties;
        this.timeUtils = timeUtils;
        this.stringUtils = stringUtils;
    }

    /**
     * 创建文件目录及父目录
     */
    public boolean createDirectory(File directory){
        if(!isHasFile(directory)){
            return directory.mkdirs();
        }else {
            return true;
        }
    }

    /**
     * 判断文件是否存在
     * @param directory 文件目录
     */
    public boolean isHasFile(File directory){
        //exists()方法可以校验是否存在该文件，防止抛出异常
        return directory.exists();
    }

    /**
     * 创建文件
     */
    public boolean createFile(File file){
        boolean flag;
        try{
            //创建父目录，若存在
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                createDirectory(parentFile);
            }
            //创建文件
            flag = file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(File file){
        return file.delete();
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

    /**
     * 检查文件是否合法
     * @param file 文件
     * @return ApiResponse
     */
    public ApiResponse checkFile(MultipartFile file){
        if (file.isEmpty()) {
            return ApiResponse.error(RequestCode.CODE_406, "文件为空").build();
        }

        //获取文件名
        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.contains(".")) {
            return ApiResponse.error(RequestCode.CODE_406, "文件名异常").build();
        }

        //获取文件后缀
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        //判断是否支持的类型
        if (!properties.getFile().getFormat().contains(fileSuffix)) {
            return ApiResponse.error(RequestCode.CODE_406, "文件格式不支持").build();
        }

        long size = file.getSize();
        // 10MB
        if(size > properties.getFile().getMaxSize()){
            return ApiResponse.error(RequestCode.CODE_406, "文件过大").build();
        }

        return ApiResponse.success().build();
    }

    /**
     * 保存文件
     * @return null为失败，成功则为路径
     */
    public String saveFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        Paths.get("");

        String fileSuffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        //生成文件目录
        String dir = timeUtils.getNowTime("yyyyMMdd");
        String newFileName = timeUtils.getNowTime("yyyyMMddHHmmss");
        //生成随机5长度数字字符串
        String randomNumber = stringUtils.generateRandomNumberString(5);
        // 拼接新的文件名
        newFileName = newFileName + randomNumber + fileSuffix;

        String path = properties.getFile().getPath() + "/" + dir + "/";

        //自动创建父目录，若不存在
        if(!createDirectory(new File(classesPath + path))){
            return null;
        }

        // 拼接完整文件名
        String allPath = classesPath + path + newFileName;
        // 将文件放进指定目录
        try{
            file.transferTo(new File(allPath));
        } catch (IOException e) {
            return null;
        }

        return path + newFileName;
    }

}
