/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.test.api.util;

import com.company.vertxstarter.api.util.FilterExpression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thomasperoutka
 */
public class FilterExpressionTest {
    
    @Test
    public void equalTest() {
        FilterExpression exp = FilterExpression.parse("firstname[eq]sample");
        assertEquals(exp.getField(), "firstname");
        assertEquals(exp.getOperator(), FilterExpression.Operator.EQUALS);
        assertEquals(exp.getValue(), "sample");
    }
    
    @Test
    public void notEqualTest() {
        FilterExpression exp = FilterExpression.parse("firstname[nq]sample");
        assertEquals(exp.getField(), "firstname");
        assertEquals(exp.getOperator(), FilterExpression.Operator.NOT_EQUALS);
        assertEquals(exp.getValue(), "sample");
    }
    
    @Test
    public void containsTest() {
        FilterExpression exp = FilterExpression.parse("firstname[ct]sample");
        assertEquals(exp.getField(), "firstname");
        assertEquals(exp.getOperator(), FilterExpression.Operator.CONTAINS);
        assertEquals(exp.getValue(), "sample");
    }
    
    @Test
    public void startsWithTest() {
        FilterExpression exp = FilterExpression.parse("firstname[sw]sample");
        assertEquals(exp.getField(), "firstname");
        assertEquals(exp.getOperator(), FilterExpression.Operator.STARTS_WITH);
        assertEquals(exp.getValue(), "sample");
    }
    
    @Test
    public void endsWithTest() {
        FilterExpression exp = FilterExpression.parse("firstname[ew]sample");
        assertEquals(exp.getField(), "firstname");
        assertEquals(exp.getOperator(), FilterExpression.Operator.ENDS_WITH);
        assertEquals(exp.getValue(), "sample");
    }
    
    @Test
    public void invalidTest() {
        FilterExpression exp = FilterExpression.parse("firstname[ww]sample");
        assertNull(exp);
        
        exp = FilterExpression.parse("first,name[eq]samp:le");
        assertNull(exp);
    }
    
}
