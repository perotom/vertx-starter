/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.test.api.util;

import com.company.vertxstarter.api.util.SortExpression;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author thomasperoutka
 */
public class SortExpressionTest {
    
    @Test
    public void ascOrderTest() {
        SortExpression exp = SortExpression.parse("firstname");
        assertEquals(exp.getOrder(), SortExpression.Order.ASC);
        assertEquals(exp.getField(), "firstname");
    }
    
    @Test
    public void descOrderTest() {
        SortExpression exp = SortExpression.parse("-lastname");
        assertEquals(exp.getOrder(), SortExpression.Order.DESC);
        assertEquals(exp.getField(), "lastname");
    }
    
    @Test
    public void invalidTest() {
        SortExpression exp = SortExpression.parse("+name");
        assertNull(exp);
        
        exp = SortExpression.parse("?name");
        assertNull(exp);
    }
    
}
