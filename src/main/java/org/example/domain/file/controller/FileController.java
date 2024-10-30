package org.example.domain.file.controller;
//
//import com.example.pags.config.constant.Constant;
//import com.example.pags.mysql.demo.start.IService.UploadFileRelIService;
//import com.example.pags.mysql.demo.start.dto.UploadFileDto;
//import com.example.pags.mysql.demo.start.entity.UploadFileEntity;
//import com.example.pags.mysql.demo.start.entity.UploadFileRelEntity;
//import com.example.pags.mysql.demo.start.service.UploadFileService;
//import com.example.pags.mysql.demo.sys.login.controller.LoginController;
//import com.example.pags.until.Result;
//import com.example.pags.until.UuidUtils;
//import com.example.pags.until.system.OsSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.consts.Constant;
import org.example.domain.file.IService.UploadFileRelIService;
import org.example.domain.file.dto.UploadFileDto;
import org.example.domain.file.entity.UploadFileEntity;
import org.example.domain.file.entity.UploadFileRelEntity;
import org.example.domain.file.service.UploadFileService;
import org.example.domain.login.controller.LoginController;
import org.example.until.Result;
import org.example.until.UuidUtils;
import org.example.until.system.OsSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Create by Administrator
 * Data 15:25 2021/10/3 星期日
 */
@RestController
@RequestMapping("/file")
@Api(tags = {"文件上传下载"})
@Validated
@Slf4j
public class FileController {

    @Value(value = "${uploadPath}")
    public String uploadPath;

    @Value(value = "${di-uploadPath}")
    public String di_uploadPath;

    @Value(value = "${ico-upload}")
    public String ico_upload;

    @Value(value = "${photo-upload}")
    public String photo_upload;

    @Value(value = "${downloadPath}")
    public String downloadPath;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UploadFileRelIService uploadFileRelIService;

    @GetMapping("/get-computer")
    @ApiOperation(value = "get-computer", notes = "get-computer", httpMethod = "GET")
    public String getComputer() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        System.out.println("fsv = " + fsv);
        File com = fsv.getHomeDirectory();
        System.out.println("com = " + com);
        return com.toString();
    }

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value(value = "${file.uri}")
    private String uri;

    @Value(value = "${file.uploadPath}")
    public String reluploadPath;

    @Autowired
    private HttpSession httpSession;


    @PostMapping(value = "/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.ofFail("文件为空");
            }
            byte[] bytes = file.getBytes();
            Resource resource = file.getResource();
            long size = file.getSize();
            String contentType = file.getContentType();
            String name = file.getName();
            log.info("bytes = {}", bytes);
            log.info("resource = {}", resource);
            log.info("size = {}", size);
            log.info("contentType = {}", contentType);
            log.info("name = {}", name);

            if (size > 10 * 1024 * 1024) {
                return Result.ofFail("文件过大，只支持10M以内的文件上传");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            int i = Objects.requireNonNull(fileName).lastIndexOf(".");
            String suffixName = null;
            if (i != -1) {
                suffixName = fileName.substring(i + 1);
            }
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径****************************************************
            final String directory = LocalDate.now().toString();
            String filePath = uploadPath + directory + "/";
            String uuid = UUID.randomUUID().toString();
            fileName = "[" + uuid + "]" + fileName;
            String path = filePath + fileName;
            log.info("path = {}", path);
            File dest = new File(new File(path).getAbsolutePath());// dest为文件，有多级目录的文件
            log.info("dest = {}", dest);
            log.info("dest.getParent() = {}", dest.getParent());
            log.info("dest.getParentFile().exists() = {}", dest.getParentFile().exists());
            log.info("dest.getParentFile() = {}", dest.getParentFile());
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();   //新建文件夹
            }
            file.transferTo(dest);// 文件写入
            UploadFileDto build = UploadFileDto.builder()
                    .directory(directory).equip(InetAddress.getLocalHost().toString())
                    .uuid(uuid).fileName(fileName).fileType(suffixName)
                    .createBy(InetAddress.getLocalHost().toString())
                    .build();
            uploadFileService.addOne(build);
            return Result.ofSuccess("上传成功", build);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return Result.ofFail("上传失败");
    }


    @PostMapping(value = "/upload-v")
    public Result uploadv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.ofFail("文件为空");
            }
            String serverIpPort = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":"
                    + httpServletRequest.getServerPort();
            log.info("serverIpPort = {}", serverIpPort);

            byte[] bytes = file.getBytes();
            Resource resource = file.getResource();
            long size = file.getSize();
            String contentType = file.getContentType();
            String name = file.getName();
            log.info("bytes = {}", bytes);
            log.info("resource = {}", resource);
            log.info("size = {}", size);
            log.info("contentType = {}", contentType);
            log.info("name = {}", name);

            if (size > 10 * 1024 * 1024) {
                return Result.ofFail("文件过大，只支持10M以内的文件上传");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            int i = Objects.requireNonNull(fileName).lastIndexOf(".");
            String suffixName = null;
            if (i != -1) {
                suffixName = fileName.substring(i + 1);
            }
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径****************************************************
            final String directory = LocalDate.now().toString();
            String filePath = reluploadPath + directory + "/";
//            String filePath = reluploadPath;
            String uuid = UUID.randomUUID().toString();
            fileName = uuid + "_" + fileName;
            String path = filePath + fileName;
            log.info("path = {}", path);
            if (OsSelect.isWindows()) {
                path = System.getProperty("user.dir").substring(0, 2) + path;
            }
            // visitPath
            String visitPath = serverIpPort + uri + directory + "/" + fileName;
            log.info("visitPath = {}", visitPath);

            File dest = new File(new File(path).getAbsolutePath());// dest为文件，有多级目录的文件
            log.info("dest = {}", dest);
            log.info("dest.getParent() = {}", dest.getParent());
            log.info("dest.getParentFile().exists() = {}", dest.getParentFile().exists());
            log.info("dest.getParentFile() = {}", dest.getParentFile());
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();   //新建文件夹
            }
            file.transferTo(dest);// 文件写入
            Object attribute = this.httpSession.getAttribute(Constant.SESSION.USER_ID);
            log.info("从session获取创建人={}",attribute);
            UploadFileDto build = UploadFileDto.builder()
                    .directory(directory).equip(InetAddress.getLocalHost().toString())
                    .previewUrl(visitPath)
                    .realUrl(path)
                    .uuid(uuid).fileName(fileName).fileType(suffixName)
                    .createBy(Optional.ofNullable(String.valueOf(attribute)).orElse("admin"))
                    .build();
            this.uploadFileService.addOne(build);
            return Result.ofSuccess("上传成功", build);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return Result.ofFail("上传失败");
    }


    @Autowired
    private LoginController loginController;

    @PostMapping(value = "/upload-v1")
    public Result uploadv1(@RequestParam("file") MultipartFile file,
                           @RequestParam(required = false) String belongModel,
                           @RequestParam(required = false) String masterId,
                           @RequestParam String hasLogin) {
        try {
            if (file.isEmpty()) {
                return Result.ofFail("文件为空");
            }
            String serverIpPort = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":"
                    + httpServletRequest.getServerPort();
            log.info("serverIpPort = {}", serverIpPort);

            byte[] bytes = file.getBytes();
            Resource resource = file.getResource();
            long size = file.getSize();
            String contentType = file.getContentType();
            String name = file.getName();
            log.info("bytes = {}", bytes);
            log.info("resource = {}", resource);
            log.info("size = {}", size);
            log.info("contentType = {}", contentType);
            log.info("name = {}", name);

            if (size > 10 * 1024 * 1024) {
                return Result.ofFail("文件过大，只支持10M以内的文件上传");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            int i = Objects.requireNonNull(fileName).lastIndexOf(".");
            String suffixName = null;
            if (i != -1) {
                suffixName = fileName.substring(i + 1);
            }
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径****************************************************
            final String directory = LocalDate.now().toString();
            String filePath = reluploadPath + directory + "/";
//            String filePath = reluploadPath;
            String uuid = UUID.randomUUID().toString();
            fileName = uuid + "_" + fileName;
            String path = filePath + fileName;
            log.info("path = {}", path);
            if (OsSelect.isWindows()) {
                path = System.getProperty("user.dir").substring(0, 2) + path;
            }
            // visitPath
            String visitPath = serverIpPort + uri + directory + "/" + fileName;
            log.info("visitPath = {}", visitPath);

            File dest = new File(new File(path).getAbsolutePath());// dest为文件，有多级目录的文件
            log.info("dest = {}", dest);
            log.info("dest.getParent() = {}", dest.getParent());
            log.info("dest.getParentFile().exists() = {}", dest.getParentFile().exists());
            log.info("dest.getParentFile() = {}", dest.getParentFile());
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();   //新建文件夹
            }
            file.transferTo(dest);// 文件写入
            Object attribute = this.httpSession.getAttribute(Constant.SESSION.USER_ID);
            Object data = this.loginController.custIp(httpServletRequest).getData();
            String createBy = String.valueOf(data);
            if(Objects.equals("1",hasLogin)){//无登录人员,跳过（0无1有）
                createBy = String.valueOf(attribute);
            }
            log.info("从session获取创建人={}",attribute);
            UploadFileDto build = UploadFileDto.builder()
                    .directory(directory).equip(InetAddress.getLocalHost().toString())
                    .previewUrl(visitPath)
                    .realUrl(path)
                    .uuid(uuid).fileName(fileName).fileType(suffixName)
                    .createBy(createBy)
                    .build();
            this.uploadFileService.addOne(build);

            if(Objects.nonNull(belongModel) && Objects.nonNull(masterId)){
                UploadFileRelEntity uploadFileRelEntity = new UploadFileRelEntity();
                uploadFileRelEntity.setId(UuidUtils.getUuid());
                uploadFileRelEntity.setMasterId(masterId);
                uploadFileRelEntity.setFileId(uuid);
                uploadFileRelEntity.setBelongModel(belongModel);
                uploadFileRelEntity.setCreateUser(createBy);
                uploadFileRelEntity.setCreateTime(LocalDateTime.now());
                this.uploadFileRelIService.save(uploadFileRelEntity);
            }

            return Result.ofSuccess("上传成功", build);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return Result.ofFail("上传失败");
    }

    @GetMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response,

                               @RequestParam(required = false) String id) {
//        fileName = "[be0f5f95-10a2-4c26-a02f-25b538c1e8ba]+神仙   1.png";// 文件名
//        fileName = "你好 .png";
        String fileName = "[c383896f-7c29-4a5f-95d2-3c232c8cdd48]834E6630-EBBD-4047-82F1-C2B8662FA96B.jpeg";
        String dp = downloadPath;
        System.out.println("id = " + id);
        if (Objects.nonNull(id)) {
            UploadFileEntity one = uploadFileService.getOne(id);
            if (Objects.nonNull(one)) {
                dp = dp + "/" + one.getDirectory();
                System.out.println("dp = " + dp);
                System.out.println("downloadPath = " + downloadPath);
                fileName = one.getFileName();
                System.out.println("fileName = " + fileName);
            }
        }
        if (fileName != null) {
            //设置文件路径
            File file = new File(dp + "/" + fileName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    response.setContentType("application/force-download");//设置强制下载不打开
                    response.addHeader("Content-Disposition", "attachment;fileName=" +
                            URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")
                    );//设置文件名
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {//做关闭操作
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";
    }


    @PostMapping(value = "/di-upload")
    public Result diUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.ofFail("文件为空");
            }
            byte[] bytes = file.getBytes();
            Resource resource = file.getResource();
            long size = file.getSize();
            String contentType = file.getContentType();
            String name = file.getName();
            log.info("bytes = {}", bytes);
            log.info("resource = {}", resource);
            log.info("size = {}", size);
            log.info("contentType = {}", contentType);
            log.info("name = {}", name);

            if (size > 10 * 1024 * 1024) {
                return Result.ofFail("文件过大，只支持10M以内的文件上传");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            int i = Objects.requireNonNull(fileName).lastIndexOf(".");
            String suffixName = null;
            if (i != -1) {
                suffixName = fileName.substring(i + 1);
            }
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径****************************************************
            final String directory = LocalDate.now().toString();
            String filePath = System.getProperty("user.dir") + di_uploadPath;
            fileName = UUID.randomUUID().toString() + "." + suffixName;
            String path = filePath + fileName;
            log.info("filePath = {}", filePath);
            log.info("path = {}", path);
            File dest = new File(new File(path).getAbsolutePath());// dest为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();   //新建文件夹
            }
            file.transferTo(dest);// 文件写入

            return Result.ofSuccess("上传成功", fileName);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return Result.ofFail("上传失败");
    }


    @PostMapping(value = "/ico-upload")
    public Result icoUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.ofFail("文件为空");
            }
            byte[] bytes = file.getBytes();
            Resource resource = file.getResource();
            long size = file.getSize();
            String contentType = file.getContentType();
            String name = file.getName();
            log.info("bytes = {}", bytes);
            log.info("resource = {}", resource);
            log.info("size = {}", size);
            log.info("contentType = {}", contentType);
            log.info("name = {}", name);

            if (size > 10 * 1024 * 1024) {
                return Result.ofFail("文件过大，只支持10M以内的文件上传");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            int i = Objects.requireNonNull(fileName).lastIndexOf(".");
            String suffixName = null;
            if (i != -1) {
                suffixName = fileName.substring(i + 1);
            }
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径****************************************************
            String uuid = UUID.randomUUID().toString();
            fileName = uuid + "." + suffixName;
            String path = ico_upload + fileName;
            if (OsSelect.isWindows()) {
                path = System.getProperty("user.dir") + ico_upload + fileName;
            }
            log.info("ico_uploadPath = {}", ico_upload);
            log.info("path = {}", path);
            File dest = new File(new File(path).getAbsolutePath());// dest为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();   //新建文件夹
            }
            file.transferTo(dest);// 文件写入

            return Result.ofSuccess("上传成功", fileName);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return Result.ofFail("上传失败");
    }


    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam(value = "file") MultipartFile[] file,
                                          HttpServletRequest requestNew) {
        log.info("批量上传文件开始==============");
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.length == 0) {
            result.put("msg", "上传失败,请先选择文件");
            return result;
        }
        long start = System.currentTimeMillis();
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            String dirName = sf.format(new Date());
            String filePath = System.getProperty("user.dir") + ico_upload + dirName;//excel生成的文件路径
            File fileNew = new File(filePath);
            if (!fileNew.exists()) {//判断目录是否存在
                fileNew.mkdirs();
            }
            List<String> showUrls = new ArrayList<>();
            for (MultipartFile singleFile : file) {
                log.info("上传单个文件开始;文件名:" + singleFile.getOriginalFilename());

                long localStartTime = System.currentTimeMillis();
                //生成文件名称已uuid命名
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String fileNameOld = singleFile.getOriginalFilename();
                String[] fileNameArr = fileNameOld.split("\\.");
                String fileName = uuid + "." + fileNameArr[fileNameArr.length - 1];
                String filePathNew = filePath + "/" + fileName;
                File newFile = new File(filePathNew);
                singleFile.transferTo(newFile);
                String basePath = requestNew.getScheme() + "://" + requestNew.getServerName();
                log.info("上传单个文件结束,【" + fileNameOld + "】所耗时间:" + (System.currentTimeMillis() - localStartTime) + "ms");
                String showUrl = basePath + "/production/" + dirName + "/" + fileName;
                showUrls.add(showUrl);
            }
            result.put("showUrls", showUrls);
            log.info("批量上传文件结束==============总耗时:" + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            log.error("批量上传到服务器出错！", e);
        }
        return result;
    }


    /**
     * 头像上传（上传到tomcat（本地，服务ok））
     * @param file
     * @return
     */
    @PostMapping(value = "/photo-upload")
    public Result photoUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.ofFail("文件为空");
            }
            byte[] bytes = file.getBytes();
            Resource resource = file.getResource();
            long size = file.getSize();
            String contentType = file.getContentType();
            String name = file.getName();
            log.info("bytes = {}", bytes);
            log.info("resource = {}", resource);
            log.info("size = {}", size);
            log.info("contentType = {}", contentType);
            log.info("name = {}", name);

            if (size > 10 * 1024 * 1024) {
                return Result.ofFail("文件过大，只支持10M以内的文件上传");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            int i = Objects.requireNonNull(fileName).lastIndexOf(".");
            String suffixName = null;
            if (i != -1) {
                suffixName = fileName.substring(i + 1);
            }
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径****************************************************
            String uuid = UUID.randomUUID().toString();
            fileName = uuid + "." + suffixName;
            String path = photo_upload + fileName;
            if (OsSelect.isWindows()) {
                path = photo_upload + fileName;
            }
            log.info("photo_upload = {}", photo_upload);
            log.info("path = {}", path);
            File dest = new File(new File(path).getAbsolutePath());// dest为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();   //新建文件夹
            }
            file.transferTo(dest);// 文件写入

            return Result.ofSuccess("上传成功", fileName);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return Result.ofFail("上传失败");
    }
}
