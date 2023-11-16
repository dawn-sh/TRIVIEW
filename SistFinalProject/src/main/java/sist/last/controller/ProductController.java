package sist.last.controller;

import java.util.Calendar;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @GetMapping("/product/search-main")
    public String searchMainForm(@RequestParam String keyword, Model model,
                                 @RequestParam(required = false) String selDate1,
                                 @RequestParam(required = false) String selDate2) {
        model.addAttribute("keyword", keyword);
        if (selDate1 != null) {
            int[] splitDate1 = splitIntegerDay(selDate1);
            int[] splitDate2 = splitIntegerDay(selDate2);
            int sleep = calculateSleep(splitDate1, splitDate2);
            model.addAttribute("firstYear", splitDate1[0]);
            model.addAttribute("firstMonth", splitDate1[1]);
            model.addAttribute("firstDay", splitDate1[2]);
            model.addAttribute("secondYear", splitDate2[0]);
            model.addAttribute("secondMonth", splitDate2[1]);
            model.addAttribute("secondDay", splitDate2[2]);
            model.addAttribute("sleep", sleep);
        }
        return "/product/searchMainPage";
    }

    private int[] splitIntegerDay(String selDate) {
        String[] splitDay = selDate.split("-");
        int[] integerDay = new int[splitDay.length];
        for (int i = 0; i < integerDay.length; i++) {
            integerDay[i] = Integer.parseInt(splitDay[i]);
        }
        return integerDay;
    }

    private int calculateSleep(int[] first, int[] second) {
        int sleep = 0;
        return compareYear(first, second);
    }

    private int compareYear(int[] first, int[] second) {
        if (first[0] == second[0]) {
            return compareMonth(first, second);
        }
        return divideYear(first, second);
    }

    private int compareMonth(int[] first, int[] second) {
        if (first[1] == second[1]) {
            return compareDay(first, second);
        }
        return divideMonth(first, second);
    }

    private int compareDay(int[] first, int[] second) {
        return second[2] - first[2];
    }

    private int divideYear(int[] first, int[] second) {
        return 31 - first[2] + second[2];
    }

    private int divideMonth(int[] first, int[] second) {
        int firstDate = getDate(first[1], first[0]);

        return second[2] + firstDate - first[2];
    }

    private int getDate(int month, int year) {
        Calendar cal = Calendar.getInstance();

        cal.set(year, (month - 1), 1);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


}
