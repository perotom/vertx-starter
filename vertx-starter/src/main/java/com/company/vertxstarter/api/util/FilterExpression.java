/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thomasperoutka
 */
public class FilterExpression {

    //TODO: change pattern to support positive/negative look ahead. queries which contain the delimiter cause problems!
    private static final Pattern PATTERN = Pattern.compile("^([a-zA-Z.]+)\\[([a-zA-Z]{2})\\](.+)$");

    private static final Map<String, Operator> OPERATOR_MAPPING;

    static {
        OPERATOR_MAPPING = new HashMap<>();
        OPERATOR_MAPPING.put("eq", Operator.EQUALS);
        OPERATOR_MAPPING.put("nq", Operator.NOT_EQUALS);
        OPERATOR_MAPPING.put("sw", Operator.STARTS_WITH);
        OPERATOR_MAPPING.put("ew", Operator.ENDS_WITH);
        OPERATOR_MAPPING.put("ct", Operator.CONTAINS);
    }

    public enum Operator {
        EQUALS, NOT_EQUALS, STARTS_WITH, ENDS_WITH, CONTAINS, NONE;
    }
    
    private String field;
    private Operator operator;
    private String value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private static Operator parseOperator(String str) {
        if (OPERATOR_MAPPING.containsKey(str.toLowerCase())) {
            return OPERATOR_MAPPING.get(str.toLowerCase());
        } else {
            return Operator.NONE;
        }
    }

    public static FilterExpression parse(String str) {
        Matcher m = PATTERN.matcher(str);
        if (!m.find()) {
            return null;
        }
        
        FilterExpression exp = new FilterExpression();
        exp.setOperator(parseOperator(m.group(2)));
        if (exp.getOperator() == Operator.NONE) {
            return null;
        }
        exp.setField(m.group(1));
        exp.setValue(m.group(3));
        return exp;
    }
    
    @Override
    public String toString() {
        return "FilterExpression{" + "field=" + field + ", operator=" + operator + ", value=" + value + '}';
    }

}
