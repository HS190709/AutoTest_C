package com.duliday.minato;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Minato
 * @description 公司算税
 * @create 2022/3/11 11:21
 */
public class DuLiDayTax {
    BigDecimal tax = new BigDecimal("0"); //应缴个税
    BigDecimal paidTax = new BigDecimal("0"); //已缴个税
    BigDecimal cumulativeTax = new BigDecimal("0"); //累计个税
    BigDecimal accumulatedIncome = new BigDecimal("0");//累计收入
    BigDecimal totalAccumulatedIncome = new BigDecimal("0");//累计收入(含公积金)
    BigDecimal aggregateIncome = new BigDecimal("0");//综合所得收入额
    BigDecimal preSalary = new BigDecimal("21000");//税前工资
    BigDecimal afterSalary = new BigDecimal("0");//税后工资
    BigDecimal totalAfterSalary = new BigDecimal("0");//税后工资（含公积金）
    BigDecimal thresholdTax = new BigDecimal("5000");//个税起征点
    BigDecimal specialDeduction = new BigDecimal("1500");//专项扣除
    BigDecimal socialSecurity = new BigDecimal("0");//社保
    BigDecimal healthInsurance = new BigDecimal("0");//医保
    BigDecimal housingFund = new BigDecimal("0");//公积金
    BigDecimal[] mealAllowance = {
            new BigDecimal("740"),
            new BigDecimal("480"),
            new BigDecimal("300")/*,
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300"),
            new BigDecimal("300")*/};//餐补
    BigDecimal[] absenteeismSalary = {
            new BigDecimal("0"),
            new BigDecimal("125.52"),
            new BigDecimal("0")/*,
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0")*/};//缺勤薪资
    BigDecimal[] backPay = {
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0")};//补发工资
    BigDecimal[] performanceBonus = {
            new BigDecimal("6069.32"),
            new BigDecimal("0"),
            new BigDecimal("0")/*,
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0")*/};//绩效奖金
    BigDecimal[] cumulativeIncome = {
            new BigDecimal("0"),
            new BigDecimal("36000"),
            new BigDecimal("144000"),
            new BigDecimal("300000"),
            new BigDecimal("420000"),
            new BigDecimal("660000"),
            new BigDecimal("960000")
    }; //收入范围
    BigDecimal[] taxRate = {
            new BigDecimal("0.03"),
            new BigDecimal("0.1"),
            new BigDecimal("0.2"),
            new BigDecimal("0.25"),
            new BigDecimal("0.3"),
            new BigDecimal("0.35"),
            new BigDecimal("0.45")
    };//税率
    BigDecimal[] quickDeduction = {
            new BigDecimal("0"),
            new BigDecimal("2520"),
            new BigDecimal("16920"),
            new BigDecimal("31920"),
            new BigDecimal("52920"),
            new BigDecimal("85920"),
            new BigDecimal("181920")
    };//速算扣除数

    public static void main(String[] args) {
        DuLiDayTax duLiDayTax = new DuLiDayTax();
        for (int i = 0; i < 3; i++) {
            duLiDayTax.count(i + 1, duLiDayTax.mealAllowance[i], duLiDayTax.absenteeismSalary[i], duLiDayTax.performanceBonus[i]);
        }
    }

    /**
     * 社保公积金初始化
     */
    public void setDate(BigDecimal preSalary) {
        socialSecurity = preSalary.multiply(new BigDecimal("85")).divide(new BigDecimal("1000"));//社保
        healthInsurance = preSalary.multiply(new BigDecimal("20")).divide(new BigDecimal("1000"));//医保
        housingFund = preSalary.multiply(new BigDecimal("50")).divide(new BigDecimal("1000"));//公积金
    }

    /**
     * 累计个税计算
     */
    public BigDecimal calcCumulativeTax(BigDecimal aggregateIncome) {
        if (cumulativeIncome[1].compareTo(aggregateIncome) >= 0 && cumulativeIncome[0].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[0]).subtract(quickDeduction[0]);
        } else if (cumulativeIncome[2].compareTo(aggregateIncome) >= 0 && cumulativeIncome[1].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[1]).subtract(quickDeduction[1]);
        } else if (cumulativeIncome[3].compareTo(aggregateIncome) >= 0 && cumulativeIncome[2].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[2]).subtract(quickDeduction[2]);
        } else if (cumulativeIncome[4].compareTo(aggregateIncome) >= 0 && cumulativeIncome[3].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[3]).subtract(quickDeduction[3]);
        } else if (cumulativeIncome[5].compareTo(aggregateIncome) >= 0 && cumulativeIncome[4].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[4]).subtract(quickDeduction[4]);
        } else if (cumulativeIncome[6].compareTo(aggregateIncome) >= 0 && cumulativeIncome[5].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[5]).subtract(quickDeduction[5]);
        } else if (cumulativeIncome[6].compareTo(aggregateIncome) < 0) {
            cumulativeTax = aggregateIncome.multiply(taxRate[6]).subtract(quickDeduction[6]);
        } else {
            System.out.println("收入：" + aggregateIncome + "元，收入过低暂不扣税");
        }
        return cumulativeTax;
    }

    /**
     * 个税计算
     */
    public void count(Integer month, BigDecimal mealAllowance, BigDecimal absenteeismSalary, BigDecimal performanceBonus) {
        setDate(preSalary);
        aggregateIncome = preSalary.subtract(socialSecurity).add(mealAllowance).subtract(healthInsurance).subtract(housingFund).subtract(absenteeismSalary).subtract(thresholdTax).subtract(specialDeduction).add(aggregateIncome);
        cumulativeTax = calcCumulativeTax(aggregateIncome);
        tax = cumulativeTax.subtract(paidTax).setScale(2, RoundingMode.DOWN);
        if (new BigDecimal("0").compareTo(tax) > 0) {
            tax = new BigDecimal("0");
        } else {
            paidTax = cumulativeTax;
        }
        afterSalary = preSalary.subtract(socialSecurity).subtract(healthInsurance).subtract(housingFund).subtract(tax).add(mealAllowance).subtract(absenteeismSalary).add(performanceBonus);
        totalAfterSalary = afterSalary.add(housingFund.multiply(new BigDecimal("2")));//包含公积金
        accumulatedIncome = accumulatedIncome.add(afterSalary);
        totalAccumulatedIncome = totalAccumulatedIncome.add(totalAfterSalary);
        System.out.println("2022年" + month + "月应缴纳个税：" + tax + "，累计缴纳：" + cumulativeTax + ",综合所得收入额：" + aggregateIncome + "，税后薪资：" + afterSalary + ",税后薪资(含公积金)：" + totalAfterSalary + ",累计收入：" + accumulatedIncome + ",累计收入（含公积金）：" + totalAccumulatedIncome);

    }
}
