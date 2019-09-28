package jscloud.test.demo.template2pdf.controller;

import jscloud.test.demo.template2pdf.util.HtmlToPdf;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * description 功能描述
 *
 * @author 杨丁辉
 * @date 2019-09-18
 */
@RestController
public class Controller {
    @RequestMapping(value = "/pdf")
    public void pdf(@RequestBody Map<String, String> map, HttpServletResponse response){

        List<HashMap<String, String>> orderList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> mapOrder = new HashMap<>();
            mapOrder.put("orderCode", String.valueOf(i));
            orderList.add(mapOrder);
        }
        HtmlToPdf.createPdf(orderList, response);
    }


}
