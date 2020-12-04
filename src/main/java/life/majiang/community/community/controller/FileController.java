package life.majiang.community.community.controller;

import life.majiang.community.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Controller
public class FileController {
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file) {
        FileDTO fileDTO = new FileDTO();
        try {
//            String osName = System.getProperty("os.name");
            String path = "/Users/tangyao/Downloads/community/src/main/resources/static/upload/";
            File myfile = new File(path);
            if (!myfile.exists()) {
                myfile.mkdirs();
            }

            //获取上传图片名称
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid + "-" + fileName;
            file.transferTo(new File(path, fileName));

            fileDTO.setSuccess(1);
            fileDTO.setUrl("/upload/" + fileName);
            fileDTO.setMessage("上传成功");
        } catch (Exception e) {
            System.out.println(e);
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
        }
        return fileDTO;
    }
}
