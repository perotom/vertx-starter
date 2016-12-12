/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thomasperoutka
 */
public class SortExpression {

    private static final Pattern PATTERN = Pattern.compile("^([-])*([a-zA-Z.]+)$");

    public enum Order {
        ASC, DESC, NONE;
    }

    private String field;
    private Order order;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
    private static Order parseOrder(String op) {
        if (StringUtils.isEmpty(op)) {
            return Order.ASC;
        } else if (op.equals("-")) {
            return Order.DESC;
        }
        return Order.NONE;
    }

    public static SortExpression parse(String str) {
        Matcher m = PATTERN.matcher(str);
        if (!m.find()) {
            return null;
        }
        
        SortExpression exp = new SortExpression();
        exp.setOrder(parseOrder(m.group(1)));
        if (exp.getOrder() == Order.NONE) {
            return null;
        }
        exp.setField(m.group(2));
        return exp;
    }

    @Override
    public String toString() {
        return "SortExpression{" + "field=" + field + ", order=" + order + '}';
    }

}
