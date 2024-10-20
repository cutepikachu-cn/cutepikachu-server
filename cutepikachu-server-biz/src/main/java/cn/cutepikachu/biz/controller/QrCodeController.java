package cn.cutepikachu.biz.controller;

import cn.cutepikachu.biz.model.dto.QrCodeGenerateDTO;
import cn.cutepikachu.biz.model.enums.QrCodeFormat;
import cn.cutepikachu.biz.model.enums.QrCodeType;
import cn.cutepikachu.biz.util.QrCodeUtils;
import cn.cutepikachu.biz.util.QrCodeUtils.QrConfig;
import cn.cutepikachu.common.response.ErrorCode;
import cn.hutool.core.util.StrUtil;
import com.google.common.net.HttpHeaders;
import com.google.zxing.WriterException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

/**
 * 二维码服务 对外接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-11 20:03-18
 */
@RestController("/qr-code")
public class QrCodeController {

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generate(@RequestBody QrCodeGenerateDTO qrCodeGenerateDTO) throws IOException, WriterException {

        // 内容不能为空
        String content = qrCodeGenerateDTO.getContent();
        if (StrUtil.isBlank(content)) {
            throw bizException(ErrorCode.BAD_REQUEST, "内容不能为空");
        }

        // 构建二维码配置
        QrConfig config = buildConfig(qrCodeGenerateDTO);

        // 返回 Base64 字符串
        if (config.getType() == QrCodeType.BASE64) {
            String base64QrCode = QrCodeUtils.generateAsBase64(content, config);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(base64QrCode.getBytes());
        }

        // 返回图片
        byte[] qrCodeBytes = QrCodeUtils.generate(content, config);
        MediaType mediaType = MediaType.IMAGE_JPEG;
        String contentDisposition = QrCodeFormat.JPG.getContentDisposition();
        if (config.getFormat() == QrCodeFormat.PNG) {
            mediaType = MediaType.IMAGE_PNG;
            contentDisposition = QrCodeFormat.PNG.getContentDisposition();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(mediaType)
                .body(qrCodeBytes);
    }

    /**
     * 构建二维码配置
     *
     * @param qrCodeGenerateDTO 二维码生成参数
     * @return 二维码配置
     */
    private QrConfig buildConfig(QrCodeGenerateDTO qrCodeGenerateDTO) {
        QrConfig config = new QrConfig();

        QrCodeType type = qrCodeGenerateDTO.getType();
        if (type == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "二维码类型错误");
        }

        QrCodeFormat format = qrCodeGenerateDTO.getFormat();
        if (format == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "二维码格式错误");
        }

        config.setType(type).setFormat(format);
        return config;
    }

}
