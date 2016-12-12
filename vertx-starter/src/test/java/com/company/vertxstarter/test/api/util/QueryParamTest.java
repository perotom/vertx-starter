/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.test.api.util;

import com.company.vertxstarter.api.util.FilterExpression;
import com.company.vertxstarter.api.util.FilterExpression.Operator;
import com.company.vertxstarter.api.util.QueryParam;
import com.company.vertxstarter.api.util.SortExpression;
import com.company.vertxstarter.api.util.SortExpression.Order;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thomasperoutka
 */
public class QueryParamTest {

    @Test
    public void all() {
        QueryParam param = new QueryParam("sort=firstname&filter=person.firstname[nq]sample");

        SortExpression[] sortExp = new SortExpression[param.getSortExpressions().size()];
        sortExp = param.getSortExpressions().toArray(sortExp);

        assertEquals(sortExp[0].getField(), "firstname");
        assertEquals(sortExp[0].getOrder(), Order.ASC);

        FilterExpression[] filterExp = new FilterExpression[param.getFilterExpressions().size()];
        filterExp = param.getFilterExpressions().toArray(filterExp);

        assertEquals(filterExp[0].getField(), "person.firstname");
        assertEquals(filterExp[0].getOperator(), Operator.NOT_EQUALS);
        assertEquals(filterExp[0].getValue(), "sample");
    }

    @Test
    public void sort() {
        QueryParam param = new QueryParam("sort=firstname,-lastname,person.name");
        SortExpression exp[] = new SortExpression[param.getSortExpressions().size()];
        exp = param.getSortExpressions().toArray(exp);

        assertEquals(exp[0].getField(), "firstname");
        assertEquals(exp[0].getOrder(), Order.ASC);

        assertEquals(exp[1].getField(), "lastname");
        assertEquals(exp[1].getOrder(), Order.DESC);

        assertEquals(exp[2].getField(), "person.name");
        assertEquals(exp[2].getOrder(), Order.ASC);
    }

    @Test
    public void filter() {
        QueryParam param = new QueryParam("filter=person.firstname[eq]sa\\,m\\&ple,lastname[ct]ex\\\\t");
        FilterExpression exp[] = new FilterExpression[param.getFilterExpressions().size()];
        exp = param.getFilterExpressions().toArray(exp);

        for (FilterExpression ex : exp) {
            if (ex.getField().equals("person.firstname")) {
                assertEquals(ex.getOperator(), Operator.EQUALS);
                assertEquals(ex.getValue(), "sa,m&ple");
            } else if (ex.getField().equals("lastname")) {
                assertEquals(ex.getOperator(), Operator.CONTAINS);
                assertEquals(ex.getValue(), "ex\\t");
            }
        }
    }

    @Test
    public void empty() {
        QueryParam param = new QueryParam("");
        assertTrue(param.isValid());
        assertTrue(param.getFilterExpressions().isEmpty());
        assertTrue(param.getSortExpressions().isEmpty());
    }

    @Test
    public void invalid() {
        QueryParam param = new QueryParam("dgfsdas");
        assertFalse(param.isValid());

        param = new QueryParam("sfd?!sad:;,");
        assertFalse(param.isValid());
    }
}
