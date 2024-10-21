package org.example.until.verification;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * 图片验证码生成
 *
 * @author
 */
@Slf4j
public class ImageVerificationCode {
    // 图片的宽度。
    private int width = 180;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 20;
    // 验证码
    private String code = null;
    // 验证码图片Buffer
    private BufferedImage buffImg = null;
    Random random = new Random();

    public ImageVerificationCode() {
        creatImage();
    }

    public ImageVerificationCode(int width, int height) {
        this.width = width;
        this.height = height;
        creatImage();
    }

    public ImageVerificationCode(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        creatImage();
    }

    public ImageVerificationCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        creatImage();
    }

    // 生成图片
    private void creatImage() {
        int fontWidth = width / codeCount - 4;// 字体的宽度
        int fontHeight = height - 8;// 字体的高度
        int codeY = height - 8;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Graphics g = buffImg.getGraphics();
        Graphics2D g = (Graphics2D) buffImg.getGraphics();
        // 设置背景色
        g.setColor(getRandColor(235, 250));
        g.fillRect(0, 0, width, height);

        // 设置字体
        Font font = new Font("Default", Font.PLAIN, fontHeight);
        g.setFont(font);

        // 设置干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 220));
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加噪点
        float yawpRate = 0.01f;// 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            buffImg.setRGB(x, y, random.nextInt(255));
        }

        String str1 = randomStr(codeCount);// 得到随机字符
        this.code = str1;

        for (int i = 0; i < codeCount; i++) {
            String strRand = str1.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            // g.drawString(a,x,y);
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
            int jiaodu = random.nextInt(60) - 30;// 角度正负30度
            double hudu = jiaodu * Math.PI / 180;// 弧度
            //
            g.rotate(hudu, i * fontWidth, codeY - 8);
            // 画字符串
            g.drawString(strRand, i * fontWidth + 2, codeY);
            // 会累计旋转 所以要转回去
            g.rotate(-hudu, i * fontWidth, codeY - 8);

        }
        // 释放资源
        g.dispose();
    }

    // 得到随机字符
    private String randomStr(int n) {
        String str1 = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz123456789";
        String str2 = "";
        int len = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }

    // 得到随机颜色
    private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code.toLowerCase();
    }

    public String getBase64() {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(buffImg, "png", bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = new String(Base64.getEncoder().encode(imageBytes));
            bos.close();
        } catch (IOException e) {
            log.error("获取验证码getBase64：" + e);
        }
        return imageString;
    }

    public void output(BufferedImage image, ServletOutputStream outputStream) {
    }
}