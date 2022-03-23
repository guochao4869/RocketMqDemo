package com.example.newtest.service.javautlis;

import java.io.*;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: GC
 * @Date: 2021年11月2日 17:53:32
 * @Description:
 */

@Slf4j
public class FileUtils {

    /**
     *
     * @param file 文件
     * @param path   文件存放路径
     * @param fileName 原文件名
     * @return
     */
    public static String upload(MultipartFile file, String path, String fileName){

        // 生成新的文件名
        String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 下载文件
     * @param path 文件路径
     * @return
     */
    public static String download(String path, HttpServletResponse response){
        File file = new File(path);
        if (!file.exists()) {
            log.error("文件不存在");
            return null;
        }
        String fileName = file.getName();
        log.info("文件名字:" + fileName);
        // 获取名字后缀名
        String substring = path.substring(path.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
        log.info("文件的后缀名：" + substring);
        // 文件写入输出流
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream inputStream = new BufferedInputStream(fileInputStream);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            fileInputStream.close();
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(bytes);
            outputStream.flush();
            return "ok";
        }catch (Exception e)  {
            log.error("出错了");
            return null;
        }
    }
}
