package cn.cutepikachu.biz.util;

import cn.cutepikachu.biz.model.enums.QrCodeFormat;
import cn.cutepikachu.biz.model.enums.QrCodeType;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import static com.google.zxing.EncodeHintType.*;

/**
 * 二维码工具类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-11 15:08-46
 */
public class QrCodeUtils {

    @Data
    public static class QrConfig {
        private static final Integer BLACK = 0xFF000000;
        private static final Integer WHITE = 0xFFFFFFFF;

        private QrCodeType type = QrCodeType.IMAGE;
        private QrCodeFormat format = QrCodeFormat.JPG;
        private Integer width = 256;
        private Integer height = 256;
        private Integer margin = 1;

        // 前景色
        private Integer onColor = BLACK;
        // 背景色
        private Integer offColor = WHITE;

        // 纠错程度
        private ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        // 字符编码
        private String characterSet = "UTF-8";
        // QR Code 版本
        private Integer qrVersion = 1;
        // QR Code 码掩码图案
        private Integer qrMaskPattern = -1;
        // 是否使用紧凑模式
        private Boolean qrCompact = Boolean.TRUE;
    }

    /**
     * 生成 Base64 二维码
     */
    public static String generateAsBase64(String content, QrConfig config) throws WriterException, IOException {
        QrCodeType type = config.getType();
        if (QrCodeType.IMAGE != type) {
            throw new IllegalArgumentException("必须为 BASE64 类型");
        }
        QrCodeFormat format = config.getFormat();
        byte[] qrCodeBytes = generate(content, config);
        String base64 = Base64.getEncoder().encodeToString(qrCodeBytes);
        return "data:image/" + format.name() + ";base64," + base64;
    }

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @param config  二维码配置
     * @return 二维码字节数组
     */
    public static byte[] generate(String content, QrConfig config) throws WriterException, IOException {
        Integer width = config.getWidth();
        Integer height = config.getHeight();
        QrCodeFormat format = config.getFormat();
        Map<EncodeHintType, ?> hints = getEncodeHintTypeMap(config);

        // 生成二维码的 BitMatrix
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // 使用 MatrixToImageConfig 设置自定义颜色
        Integer onColor = config.getOnColor();
        Integer offColor = config.getOffColor();
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);

        // 将 BitMatrix 写入 ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, format.name(), outputStream, matrixToImageConfig);

        // 返回生成的二维码字节数组
        return outputStream.toByteArray();
    }

    /**
     * 获取二维码配置 Map
     *
     * @param config 二维码配置
     * @return EncodeHintType 配置 Map
     */
    private static Map<EncodeHintType, ?> getEncodeHintTypeMap(QrConfig config) {
        Integer margin = config.getMargin();
        ErrorCorrectionLevel errorCorrectionLevel = config.getErrorCorrectionLevel();
        String characterSet = config.getCharacterSet();
        Integer qrVersion = config.getQrVersion();
        Integer qrMaskPattern = config.getQrMaskPattern();
        Boolean qrCompact = config.getQrCompact();

        return Map.of(
                ERROR_CORRECTION, errorCorrectionLevel,
                CHARACTER_SET, characterSet,
                QR_VERSION, qrVersion,
                QR_MASK_PATTERN, qrMaskPattern,
                QR_COMPACT, qrCompact,
                MARGIN, margin
        );
    }

}
