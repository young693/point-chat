package com.point.chat.pointcommon.utils;


import cn.hutool.core.lang.Validator;
import com.point.chat.pointcommon.em.ExceptionCodeEm;
import com.point.chat.pointcommon.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 群头像生成
 */
@Slf4j
public class MakeGroupHeadPicUtil {

    // 围边使用的灰色
    private final static int[] COLOR_GREY_BGR = new int[]{230, 230, 230};

    public static void main(String[] args) {
        // 添加测试图片
        String destPath = "/Users/daoyang/Desktop/";
        List<String> sourcePics = new ArrayList<>();
        // sourcePics.add("https://dev.dot.cn:8443/media/image/chat-msg/2024-11-04/聊天室logo-5XiIvrPWI2xV.png");
        // sourcePics.add("https://dev.dot.cn:8443/media/image/chat-msg/2024-11-04/聊天logo-9hxvrhVjHzy5.png");
        sourcePics.add("https://dev.dot.cn:8443/media/image/chat-msg/2024-11-04/聊天logo-9hxvrhVjHzy5.png");
        sourcePics.add("https://dev.dot.cn:8443/media/image/chat-msg/2024-11-04/聊天室logo-5XiIvrPWI2xV.png");
        sourcePics.add("https://dev.dot.cn:8443/media/image/chat-msg/2024-11-04/Snipaste_2024-08-21_10-27-44-lXVrwXGpq6nL.jpg");
        // sourcePics.add("http://h.hiphotos.baidu.com/zhidao/pic/item/c8177f3e6709c93d0f5f00e79b3df8dcd1005474.jpg");

        // 注意 存储位置最后记得加“/”
        InputStream inputStream = MakeGroupHeadPicUtil.getCombinationOfhead(sourcePics);
        FileUtil.saveToFile(inputStream, destPath + "test.png");
    }

    /**
     * @param pics     图片列表
     * @return 成功 OR 失败
     * 思考笔记  可以跳过
     * 2 和 4 的区别  向下偏移量          0.5 LUMP_WIDTH+  0.5 PIC_SPACE
     * 3 和 4 的区别 第一张图向右偏移量   0.5 LUMP_WIDTH + 0.5 PIC_SPACE
     * 5 和 6 的区别 第一张图向右偏移量   0.5 LUMP_WIDTH+ 0.5 PIC_SPACE
     * 6 和 9 的区别 向下偏移量          0.5 LUMP_WIDTH+ 0.5 PIC_SPACE
     * 7 和 9 的区别 第一张图向右偏移量  LUMP_WIDTH+PIC_SPACE
     * 8 和 9 的区别 第一张图向右偏移量  0.5 LUMP_WIDTH+ 0.5 PIC_SPACE
     */
    public static InputStream getCombinationOfhead(List<String> pics) {
        List<BufferedImage> bufferedImages = new ArrayList<>();

        // BufferedImage.TYPE_INT_RGB可以自己定义可查看API
        // 图片宽度
        int PIC_WIDTH = 422;
        // 图片高度
        int PIC_HEIGHT = 422;
        BufferedImage outImage = new BufferedImage(PIC_WIDTH, PIC_HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics2D gra = outImage.createGraphics();
        // 设置背景为蓝灰色
        gra.setColor(toColor(COLOR_GREY_BGR));
        // 填满图片
        gra.fillRect(0, 0, PIC_WIDTH, PIC_HEIGHT);

        // 开始拼凑 根据图片的数量判断该生成哪种样式组合头像

        int size = pics.size();// 图片数量
        int sqrt = (int) Math.ceil(Math.sqrt(size));// 宽度  一行几张图片
        // 计算出 单张图片宽度
        // 空白宽度
        int PIC_SPACE = 14;
        // 小图片宽度
        Double LUMP_WIDTH = (PIC_WIDTH - ((sqrt + 1.0) * PIC_SPACE)) / sqrt;

        System.out.println(LUMP_WIDTH);

        // 压缩图片所有的图片生成尺寸同意的 为 125*125
        for (String pic : pics) {
            BufferedImage resize2 = resize2(pic, LUMP_WIDTH.intValue(), LUMP_WIDTH.intValue(), true);
            bufferedImages.add(resize2);
        }

        // 小图片起始点横坐标
        double LUMP_POINT_X = 0D;
        // 小图片起始点纵坐标
        double LUMP_POINT_Y = 0D;
        // 缺几个满伍
        int lack = 0;
        // 计算起始点坐标
        if (size < sqrt * (sqrt - 1)) {// 少一行 不满伍
            // 缺几个满伍
            lack = sqrt * (sqrt - 1) - size;
            // 向右边偏移量
            LUMP_POINT_X = (double) PIC_SPACE + lack * (LUMP_WIDTH + PIC_SPACE) / 2;
            // 向下偏移量
            LUMP_POINT_Y = (double) PIC_SPACE + LUMP_WIDTH / 2.;
        } else if (size == sqrt * (sqrt - 1)) {// 满伍少一行
            // 向右边偏移量
            LUMP_POINT_X = PIC_SPACE;
            // 向下偏移量
            LUMP_POINT_Y = (double) PIC_SPACE + LUMP_WIDTH / 2.;
        } else if (size < sqrt * sqrt) {// 不满伍
            // 缺几个满伍
            lack = sqrt * sqrt - size;
            // 向右边偏移量
            LUMP_POINT_X = (double) PIC_SPACE + lack * (LUMP_WIDTH + PIC_SPACE) / 2;
            LUMP_POINT_Y = PIC_SPACE;
        } else if (size == sqrt * sqrt) {// 满伍
            LUMP_POINT_X = PIC_SPACE;
            LUMP_POINT_Y = PIC_SPACE;
        }

        int line = lack == 0 ? -1 : 0; // 第几行图片
        int row = 0; // 第几列图片
        for (int i = 0; i < bufferedImages.size(); i++) {
            if ((i + lack) % sqrt == 0) {
                line++;
                row = 0;
            }
            if (line == 0) {
                gra.drawImage(bufferedImages.get(i), (int) LUMP_POINT_X + (row++ * (PIC_SPACE + LUMP_WIDTH.intValue()))
                        , (int) LUMP_POINT_Y, null);
            } else {
                gra.drawImage(bufferedImages.get(i), PIC_SPACE + (row++ * (PIC_SPACE + LUMP_WIDTH.intValue()))
                        , (int) LUMP_POINT_Y + (line * (PIC_SPACE + LUMP_WIDTH.intValue())), null);
            }
        }
        try {
            // 创建一个ByteArrayOutputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 把BufferedImage写入ByteArrayOutputStream
            ImageIO.write(outImage, "png", os);
            // ByteArrayOutputStream转成InputStream
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ApiException(ExceptionCodeEm.SYSTEM_ERROR, "生成群头像异常,pics:" + pics);
    }


    /**
     * 图片缩放
     *
     * @param picPath 本地或网络图片路径
     * @param height  缩放后高度
     * @param width   缩放后宽度
     * @param fill    是否填充灰色
     * @return BufferedImage
     */
    public static BufferedImage resize2(String picPath, Integer height, Integer width, boolean fill) {
        try {
            if (Validator.hasChinese(picPath)){
                picPath = FileUtil.encodeChineseUrl(picPath);
            }
            BufferedImage imageBuff;
            if (picPath.indexOf("https://") == 0 || picPath.indexOf("http://") == 0) { // 简单判断是网络图片还是本地图片
                imageBuff = ImageIO.read(new URL(picPath));
            } else {
                imageBuff = ImageIO.read(new File(picPath));
            }

            Image itemp = imageBuff.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            double ratio = 0; // 缩放比例
            // 计算比例
            if ((imageBuff.getHeight() > height) || (imageBuff.getWidth() > width)) {
                if (imageBuff.getHeight() > imageBuff.getWidth()) {
                    ratio = height.doubleValue() / imageBuff.getHeight();
                } else {
                    ratio = width.doubleValue() / imageBuff.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);

                itemp = op.filter(imageBuff, null);
            }

            if (fill) {
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);

                Graphics2D g = image.createGraphics();

                g.setColor(toColor(COLOR_GREY_BGR));

                g.fillRect(0, 0, width, height);

                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            return (BufferedImage) itemp;
        } catch (IOException e) {
            log.error("图片缩放失败,picPath:{}", picPath);
            log.error("图片缩放失败",e);
        }
        return null;
    }

    /**
     * @param colorRoot 颜色索引
     * @return 颜色
     * @toColor 颜色索引转为颜色
     */
    private static Color toColor(int[] colorRoot) {
        if (colorRoot.length >= 3) {
            return new Color(colorRoot[0], colorRoot[1], colorRoot[2]);
        } else {
            return null;
        }
    }
}

