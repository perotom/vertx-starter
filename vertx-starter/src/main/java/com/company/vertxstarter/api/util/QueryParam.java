/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.api.util;

import com.company.vertxstarter.dto.Failure;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thomasperoutka
 */
public class QueryParam {

    private static final String QUERY_SORT_NAME = "sort";
    private static final String QUERY_FILTER_NAME = "filter";
    private static final String QUERY_PARAM_DELIMITER = "&";
    private static final String QUERY_PARAM_PAIR_DELIMITER = "=";
    private static final String QUERY_PARAM_FIELD_DELIMITER = ",";
    private static final String QUERY_PARAM_DELIMITER_ESCAPE = "\\";
    private static final String QUERY_PARAM_DELIMITER_ESCAPE_REPLAX_REGEX = "\\\\(.)";
    private static final String QUERY_PARAM_DELIMITER_REGEX = "(?<!" + Pattern.quote(QUERY_PARAM_DELIMITER_ESCAPE) + ")" + Pattern.quote(QUERY_PARAM_DELIMITER);
    private static final String QUERY_PARAM_PAIR_DELIMITER_REGEX = "(?<!" + Pattern.quote(QUERY_PARAM_DELIMITER_ESCAPE) + ")" + Pattern.quote(QUERY_PARAM_PAIR_DELIMITER);
    private static final String QUERY_PARAM_FIELD_DELIMITER_REGEX = "(?<!" + Pattern.quote(QUERY_PARAM_DELIMITER_ESCAPE) + ")" + Pattern.quote(QUERY_PARAM_FIELD_DELIMITER);

    private final Set<SortExpression> sortExpressions = new LinkedHashSet<>();
    private final Set<FilterExpression> filterExpressions = new HashSet<>();
    private Failure fail;

    public QueryParam(String query) {
        if (StringUtils.isEmpty(query)) {
            return;
        }
        
        String encoded = query;
        try {
            encoded = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(QueryParam.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean sortParsed = false;
        boolean filterParsed = false;

        for (String token : encoded.split(QUERY_PARAM_DELIMITER_REGEX)) {
            String pair[] = token.split(QUERY_PARAM_PAIR_DELIMITER_REGEX);
            if (pair.length != 2) {
                fail = new Failure("Bad query string format", 400);
                break;
            }

            if (pair[0].equalsIgnoreCase(QUERY_SORT_NAME)) { //sort field
                if (sortParsed) {
                    fail = new Failure("Sort field must be used only once", 400);
                    break;
                }
                if (!parseSortExpressions(pair[1])) {
                    fail = new Failure("Bad sort field format", 400);
                    break;
                }
                sortParsed = true;
            } else if (pair[0].equalsIgnoreCase(QUERY_FILTER_NAME)) { //filter field
                if (filterParsed) {
                    fail = new Failure("Filter field must be used only once", 400);
                    break;
                }
                if (!parseFilterExpressions(pair[1])) {
                    fail = new Failure("Bad filter field format", 400);
                    break;
                }
                filterParsed = true;
            }
        }
    }

    /**
     * Parses the query and sets the sort expressions
     *
     * @param query The query of the sort field.
     * @return True if expression is valid. Otherwise false.
     */
    private boolean parseSortExpressions(String query) {
        for (String token : query.split(QUERY_PARAM_FIELD_DELIMITER_REGEX)) {
            token = token.replaceAll(QUERY_PARAM_DELIMITER_ESCAPE_REPLAX_REGEX, "$1");
            SortExpression exp = SortExpression.parse(token);
            if (exp == null) { //error
                return false;
            } else {
                sortExpressions.add(exp);
            }
        }
        return true;
    }

    /**
     *
     * @param query The query of the filter field. Value must not contain the
     * delimiter.
     * @return True if expression is valid. Otherwise false.
     */
    private boolean parseFilterExpressions(String query) {
        for (String token : query.split(QUERY_PARAM_FIELD_DELIMITER_REGEX)) {
            token = token.replaceAll(QUERY_PARAM_DELIMITER_ESCAPE_REPLAX_REGEX, "$1");
            FilterExpression exp = FilterExpression.parse(token);
            if (exp == null) { //error
                return false;
            } else {
                filterExpressions.add(exp);
            }
        }
        return true;
    }

    /**
     *
     * @return true if no error occurred. Otherwise false
     */
    public boolean isValid() {
        return fail == null;
    }

    public Failure error() {
        return fail;
    }

    public Set<SortExpression> getSortExpressions() {
        return sortExpressions;
    }

    public Set<FilterExpression> getFilterExpressions() {
        return filterExpressions;
    }

}
