package jscloud.test.demo.template2pdf.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import javax.servlet.http.HttpServletResponse;
import jscloud.common.base.exception.BusinessException;

/**
 * description 导出PDF文件转换工具
 * 创建时间 2018/9/21
 *
 * @author 仇兴洲
 */
public class HtmlToPdf {
    public static void createPdf(List<HashMap<String, String>> orderList, HttpServletResponse response) {
        try {
            // 设置返回格式
            response.setHeader("content-Type", "application/pdf");
            response.setCharacterEncoding("UTF-8");

            // 创建pdf
            Document document = new Document(PageSize.A4, 10, 10, 20, 20);
            // 定义pdf写入
            PdfWriter mPdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            // 循环订单号集合
            for (int i = 0; i < orderList.size(); i++) {
                HashMap<String, String> getMap = orderList.get(i);
                // 循环获取单号
                String orderCode = getMap.get("orderCode");

                // 装箱单打印测试
                String orderHtml = getOrderPackageHtmlByOrderCode(orderCode);

//                // 批次拣货单打印测试,测试装箱单打印时,注释此处
//                String batchCode = getMap.get("orderCode");
//                // 根据批次编码生成html内容
//                orderHtml = getPickListHtmlByBatchCode(batchCode);
//                // 批次拣货单,一次只打印一个批次,正式代码不用循环处理,所以这个地方处理,直接退出循环
//                i = orderList.size();

                // 输出单张单据内容到pdf
                document.newPage();
                String pdfcss = "pdfcss";
                InputStream is = new ByteArrayInputStream(pdfcss.getBytes());
                ByteArrayInputStream bin = new ByteArrayInputStream(orderHtml.getBytes());
                XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document, bin, is, Charset.forName("UTF-8"), new ChinaFontProvide());
            }
            // 关闭文档
            document.close();
            // 关闭pdf写入
            mPdfWriter.close();
        } catch (Exception e) {
            throw new BusinessException(201, "HTML转PDF异常");
        }
    }

    /**
     * 根据订单号生成单张装箱清单
     * @param orderCode 订单编码
     * @return 单张装箱清单html
     */
    private static String getOrderPackageHtmlByOrderCode(String orderCode) {
        String str = "";
        try {
            // 装箱清单模板
            String contextPath = "C:\\workspace\\template2pdf\\src\\main\\java\\template\\template-packagelist.html";

            FileInputStream is = new FileInputStream(contextPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tempStr = "";
            while ((tempStr = br.readLine()) != null) {
                str = str + tempStr + "\n";
            }
            br.close();
            is.close();

            str = str.replace("template-logo", "http://jscloud-shop.oss-cn-shanghai.aliyuncs.com/201909/a743ef0fac076378a5d6a706fa7a8e56.JPEG?Expires=1884467782&OSSAccessKeyId=LTAIeUAWRhBQ20Xn&Signature=Jjm6WxmLr7d98Bbo2Rvag4vbKoE%3D");


            str = str.replace("template-barcode", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568833580087&di=8f296651d38f45f90dbcf0d64d510057&imgtype=0&src=http%3A%2F%2Fn4.hdfimg.com%2Fg2%2FM03%2FBA%2F44%2Fw4YBAF1Sdm6AUuvWAAADzIJNX7A745.png%3F_ms_%3Da9e3");

            str = str.replace("templatePickBathchNo", "20190918001");
            str = str.replace("templatePickOrderSeq", "1");
            str = str.replace("templateOrderNo", "ORDER:123444421321");
            str = str.replace("templateLinkPhone", "1381234567890");
            str = str.replace("templateLinkPerson", "测试会员");
            str = str.replace("templateDetailAddress", "上海市闵行区浦锦路2049弄37号楼309");
            str = str.replace("templatePickOrderComments", "测试订单pdf格式转换");

            // 根据订单内产品明细,循环产生订单明细的html内容
            String orderItemHtml = "";
            for (int i = 0; i < 8; i++) {
                // 订单产品序号赋值
                String itemIndex = String.valueOf(i + 1);
                // 订单产品编码赋值
                String itemNo = "20190918000" + "-" + String.valueOf(i + 1);
                // 订单产品标题赋值
                String itemTitle = String.valueOf(i + 1) + " - 测试产品标题字段";
                // 订单产品描述赋值 (测试数据中 <br/> 是用于换行,获取实际产品描述时,需根据字段分割符合进行预先替换 )
                String itemDescription = "组合产品100ml x 1;组合产品(6片/盒) x 1;组合产品300ml x 1;组合产品500ml x 1;组合产品100ml x 1;组合产品(6片/盒) x 1;组合产品300ml x 1;组合产品500ml x 1;";
                itemDescription = itemDescription.replace(";","<br/>");
                // 订单产品数量赋值
                String itemQuantity = String.valueOf((i + 1) * 10);

                StringBuilder builderItem = new StringBuilder();
                // 产品序号
                builderItem.append("<tr>");
                builderItem.append("<td class='orderDetailCol1 centerTd'>");
                builderItem.append(itemIndex);
                builderItem.append("</td>");
                // 产品编码
                builderItem.append("<td class='orderDetailCol2 leftTd'>");
                builderItem.append(itemNo);
                builderItem.append("</td>");
                // 产品标题及产品描述
                builderItem.append("<td class='orderDetailCol3 leftTd'>");
                builderItem.append(itemTitle);
                builderItem.append("<br/><br/>");
                builderItem.append(itemDescription);
                builderItem.append("</td>");
                // 产品数量列
                builderItem.append("<td class='orderDetailCol4 centerTd'>");
                builderItem.append(itemQuantity);
                builderItem.append("</td>");
                builderItem.append("</tr>");

                // 产品分割线
                builderItem.append("<tr>");
                builderItem.append("<td></td>");
                builderItem.append("<td></td>");
                builderItem.append("<td colspan='2' style='padding-top: 10pt;'>");
                builderItem.append("<div><hr/></div>");
                builderItem.append("</td>");
                builderItem.append("</tr>");

                orderItemHtml += builderItem.toString();
            }

            str = str.replace("templateOrderItemHtml", orderItemHtml);

//            str = str.replace("templateItemIndex", "1");
//            str = str.replace("templateItemNo", "20190918000");
//            str = str.replace("templateItemTitle", "测试产品PDF标题字段");
//            str = str.replace("templateItemQuantity", "10");
//            str = str.replace("templateItemDescription", "测试产品描述字段<br/>测试产品描述字段<br/>测试产品描述字段<br/>测试产品描述字段<br/>");


        } catch (Exception e) {

        }
        return str;
    }

    /**
     * 根据拣货批次编码生成批次html-拣货单
     * @param batchCode 拣货批次编码
     * @return 拣货批次html
     */
    private static String getPickListHtmlByBatchCode(String batchCode) {
        String str = "";
        try {
            // 拣货单模板
            String contexPath = "C:\\workspace\\template2pdf\\src\\main\\java\\template\\template-picklist.html";

            FileInputStream is = new FileInputStream(contexPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tempStr = "";
            while ((tempStr = br.readLine()) != null) {
                str = str + tempStr + "\n";
            }
            br.close();
            is.close();

            str = str.replace("template-logo", "http://jscloud-shop.oss-cn-shanghai.aliyuncs.com/201909/a743ef0fac076378a5d6a706fa7a8e56.JPEG?Expires=1884467782&OSSAccessKeyId=LTAIeUAWRhBQ20Xn&Signature=Jjm6WxmLr7d98Bbo2Rvag4vbKoE%3D");
            str = str.replace("templatePickBathchNo", "20190918001");
            str = str.replace("templateBathchTime", "2019-09-19 07:43:30");

            // 根据订单内产品明细,循环产生订单明细的html内容
            String orderItemHtml = "";
            for (int i = 0; i < 50; i++) {
                // 订单产品序号赋值
                String itemIndex = String.valueOf(i + 1);
                // 订单产品编码赋值
                String itemNo = "20190918000" + "-" + String.valueOf(i + 1);
                // 订单产品标题赋值
                String itemTitle = String.valueOf(i + 1) + " - MANA黑白面膜(6片/盒)";
                // 订单产品描述赋值 (测试数据中 <br/> 是用于换行,获取实际产品描述时,需根据字段分割符合进行预先替换 )
                String itemDescription = "组合产品100ml x 1;组合产品(6片/盒) x 1;组合产品300ml x 1;组合产品500ml x 1;组合产品100ml x 1;组合产品(6片/盒) x 1;组合产品300ml x 1;组合产品500ml x 1;";
                itemDescription = itemDescription.replace(";","<br/>");
                itemDescription = "";
                // 订单产品数量赋值
                String itemQuantity = String.valueOf((i + 1) * 10);

                StringBuilder builderItem = new StringBuilder();
                // 产品序号
                builderItem.append("<tr>");
                builderItem.append("<td class='orderDetailCol1 centerTd'>");
                builderItem.append(itemIndex);
                builderItem.append("</td>");
                // 产品编码
                builderItem.append("<td class='orderDetailCol2 leftTd'>");
                builderItem.append(itemNo);
                builderItem.append("</td>");
                // 产品标题及产品描述
                builderItem.append("<td class='orderDetailCol3 leftTd'>");
                builderItem.append(itemTitle);
                builderItem.append("<br/><br/>");
                builderItem.append(itemDescription);
                builderItem.append("</td>");
                // 产品数量列
                builderItem.append("<td class='orderDetailCol4 centerTd'>");
                builderItem.append(itemQuantity);
                builderItem.append("</td>");
                builderItem.append("</tr>");

                // 产品分割线
                builderItem.append("<tr>");
                builderItem.append("<td></td>");
                builderItem.append("<td></td>");
                builderItem.append("<td colspan='2' style='padding-top: 10pt;'>");
                builderItem.append("<div><hr/></div>");
                builderItem.append("</td>");
                builderItem.append("</tr>");

                orderItemHtml += builderItem.toString();
            }

            str = str.replace("templateOrderItemHtml", orderItemHtml);

        } catch (Exception e) {

        }
        return str;
    }

    /**
     * 提供中文
     */
    public static final class ChinaFontProvide implements FontProvider {

        @Override
        public Font getFont(final String fontname, final String encoding, final boolean embedded, final float size, final int style, final BaseColor color) {
            BaseFont bfChinese = null;
            try {
                bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                        BaseFont.NOT_EMBEDDED);

//                String fontPath = "/fonts/MicrosoftYaHei.ttf";
//                bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Font FontChinese = new Font(bfChinese, size, style, color);
            return FontChinese;
        }

        @Override
        public boolean isRegistered(String arg0) {
            return false;
        }
    }


}
