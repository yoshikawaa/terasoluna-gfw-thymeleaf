/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import java.util.Map.Entry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.thymeleaf.Arguments;
import org.thymeleaf.util.StringUtils;

import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class ThymeleafPaginationInfo extends PaginationInfo {

    private final Arguments arguments;
    private final String expression;

    private final int pageSize;
    private final Sort pageSort;

    public ThymeleafPaginationInfo(Arguments arguments, Page<?> page, String expression, int maxDisplayCount) {
        this(arguments, page, expression, null, false, maxDisplayCount);
    }

    public ThymeleafPaginationInfo(Arguments arguments, Page<?> page, String expression, String criteriaQuery,
            boolean disableHtmlEscapeOfCriteriaQuery, int maxDisplayCount) {
        super(page, PaginationInfo.DEFAULT_PATH_TEMPLATE, PaginationInfo.DEFAULT_QUERY_TEMPLATE,
                (ExpressionUtils.isExpression(criteriaQuery))
                        ? ExpressionUtils.execute(arguments, criteriaQuery, String.class) : criteriaQuery,
                disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);

        this.arguments = arguments;
        this.expression = expression;
        this.pageSize = page.getSize();
        this.pageSort = page.getSort();
    }

    public String getPageUrl(int pageIndex) {
        if (StringUtils.isEmptyOrWhitespace(expression)) {
            return super.getPageUrl(pageIndex);
        }
        return getPageUrlUsingExpression(pageIndex);
    }

    private String getPageUrlUsingExpression(int pageIndex) {

        StringBuilder pageUrlBuilder = new StringBuilder(expression);

        // resolve variables
        for (Entry<String, Object> a : createAttributeMap(pageIndex, pageSize, pageSort).entrySet()) {
            String replacementKey = "${" + a.getKey() + "}";
            int point = pageUrlBuilder.indexOf(replacementKey);
            if (point >= 0) {
                pageUrlBuilder.replace(point, point + replacementKey.length(), "'" + a.getValue() + "'");
            }
        }

        // resolve expression
        String pageUrl = ExpressionUtils.execute(arguments, pageUrlBuilder.toString(), String.class);

        // append criteria query
        String criteriaQuery = getCriteriaQuery();
        if (!StringUtils.isEmptyOrWhitespace(criteriaQuery)) {
            return (pageUrl.contains("?")) ? pageUrl + "&" + criteriaQuery : pageUrl + "?" + criteriaQuery;
        }
        return pageUrl;
    }

}
