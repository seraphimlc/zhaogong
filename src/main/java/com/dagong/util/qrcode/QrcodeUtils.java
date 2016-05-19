package com.dagong.util.qrcode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.zxing.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类
 * <p>
 * 作者: zhoubang 日期：2015年3月26日 下午1:30:38
 */
public class QrcodeUtils {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(QrcodeUtils.class);

    private static transient String DEFAULT_FORMAT = "png";
    private static transient int DEFAULT_WIDTH = 300;
    private static transient int DEFAULT_HEIGHT = 300;




    /**
     * 生成二维码
     *
     * @param content  二维码文本内容
     * @param destFile 目的文件
     * @param logoFile 中间logo文件
     */
    public static final void gen(final String content, final File destFile,
                                 final File logoFile) throws Exception {
        gen(content, destFile, logoFile, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 生成二维码
     *
     * @param content  二维码文本内容
     * @param destFile 目的文件
     * @param logoFile 中间logo文件
     * @param width    宽度
     * @param height   高度
     */
    public static final void gen(final String content, final File destFile,
                                 final File logoFile, int width, int height) throws Exception {

        try {

            gen(content, destFile, logoFile, width, height, ErrorCorrectionLevel.M);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


    /**
     * 生成二维码
     *
     * @param content              二维码文本内容
     * @param outputFile               输出流
     * @param logoFile            中间logo输入流，为空时中间无logo
     * @param width                宽度
     * @param height               高度
     * @param errorCorrectionLevel 容错级别
     */
    public static final void gen(final String content,
                                  File outputFile,  File logoFile, int width,
                                 int height, ErrorCorrectionLevel errorCorrectionLevel)
            throws Exception {
        if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("qr code content cannot be empty.");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("qr code output stream cannot be null.");
        }

         BitMatrix matrix = MatrixToImageWriterEx.createQRCode(content, width, height, errorCorrectionLevel);

        if (logoFile == null) {
            try {
                MatrixToImageWriter.writeToFile(matrix, DEFAULT_FORMAT, outputFile);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }

        final MatrixToLogoImageConfig logoConfig = new MatrixToLogoImageConfig(Color.BLUE, 4);
        try {
            MatrixToImageWriterEx.writeToFile(matrix, DEFAULT_FORMAT, outputFile, logoFile, logoConfig);

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


    /**
     * 生成二维码
     *
     * @param content  二维码文本内容
     * @param destPath 输出文件路径
     */
    public static final void gen(final String content, final String destPath)
            throws Exception {
        gen(content, destPath,null, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 生成二维码
     *
     * @param content  二维码文本内容
     * @param destPath 输出文件路径
     * @param width    宽度
     * @param height   高度
     */
    public static final void gen(final String content, final String destPath,
                                 int width, int height) throws Exception {
        gen(content, destPath,null, width, height);
    }

    /**
     * 生成二维码
     *
     * @param content  二维码文本内容
     * @param destPath 目的文件路径
     * @param logoPath 中间logo文件路径
     */
    public static final void gen(final String content, final String destPath,
                                 final String logoPath) throws Exception {
        gen(content, destPath, logoPath, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 生成二维码
     *
     * @param content  二维码文本内容
     * @param destPath 目的文件路径
     * @param logoPath 中间logo文件路径
     * @param width    宽度
     * @param height   高度
     */
    public static final void gen(final String content, final String destPath,
                                 final String logoPath, int width, int height) throws Exception {
        File foo = new File(destPath);
        File bar = null;
        if(StringUtils.isNotBlank(logoPath)){
            bar=new File(logoPath);
        }
        gen(content, foo, bar, width, height);
    }

    /**
     * 解析二维码
     *
     * @param input 二维码输入流
     */
    public static final String parse(InputStream input) throws Exception {
        Reader reader = null;
        BufferedImage image;
        try {
            image = ImageIO.read(input);
            if (image == null) {
                throw new Exception("cannot read image from inputstream.");
            }
            final LuminanceSource source = new BufferedImageLuminanceSource(image);
            final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            final Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            // 解码设置编码方式为：utf-8，
            reader = new MultiFormatReader();
            return reader.decode(bitmap, hints).getText();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("parse QR code error: ", e);
        } catch (ReaderException e) {
            e.printStackTrace();
            throw new Exception("parse QR code error: ", e);
        }
    }

    /**
     * 解析二维码
     *
     * @param url 二维码url
     */
    public static final String parse(URL url) throws Exception {
        InputStream in = null;
        try {
            in = url.openStream();
            return parse(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("parse QR code error: ", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片文件
     */
    public static final String parse(File file) throws Exception {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            return parse(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception("parse QR code error: ", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 解析二维码
     *
     * @param filePath 二维码图片文件路径
     */
    public static final String parse(String filePath) throws Exception {
        return parse(new File(filePath));
    }

    public static void main(String[] args) {
        try {
            String text = "http://www.baidu.com";
            File outputFile = new File("/tmp/test.png");
            QrcodeUtils.gen(text, "/tmp/test.jpg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
