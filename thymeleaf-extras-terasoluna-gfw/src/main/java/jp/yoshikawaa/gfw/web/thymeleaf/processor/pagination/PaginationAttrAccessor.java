package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.thymeleaf.dom.Element;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.IAttrAccessor;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ProcessorUtils;

public class PaginationAttrAccessor implements IAttrAccessor {

    private static final String ATTRIBUTE_INNER_ELEMENT = "inner-element";
    private static final String ATTRIBUTE_DISABLED_CLASS = "disabled-class";
    private static final String ATTRIBUTE_ACTIVE_CLASS = "active-class";
    private static final String ATTRIBUTE_FIRST_LINK_TEXT = "first-link-text";
    private static final String ATTRIBUTE_PREVIOUS_LINK_TEXT = "previous-link-text";
    private static final String ATTRIBUTE_NEXT_LINK_TEXT = "next-link-text";
    private static final String ATTRIBUTE_LAST_LINK_TEXT = "last-link-text";
    private static final String ATTRIBUTE_MAX_DISPLAY_COUNT = "max-display-count";
    private static final String ATTRIBUTE_DISABLED_HREF = "disabled-href";
    private static final String ATTRIBUTE_HREF_TMPL = "href-tmpl";
    private static final String ATTRIBUTE_CRITERIA_QUERY = "criteria-query";
    private static final String ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY = "disable-html-escape-of-criteria-query";
    private static final String ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE = "enable-link-of-current-page";

    private static final String DEFAULT_FIRST_LINK_TEXT = "<<";
    private static final String DEFAULT_PREVIOUS_LINK_TEXT = "<";
    private static final String DEFAULT_NEXT_LINK_TEXT = ">";
    private static final String DEFAULT_LAST_LINK_TEXT = ">>";
    private static final String DEFAULT_HREF_TMPL = null;
    private static final String DEFAULT_CRITERIA_QUERY = null;
    private static final boolean DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY = false;
    private static final boolean DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE = false;

    private final String dialectPrefix;

    private final String innerElement;
    private final String disabledClass;
    private final String activeClass;
    private final String firstLinkText;
    private final String previousLinkText;
    private final String nextLinkText;
    private final String lastLinkText;
    private final int maxDisplayCount;
    private final String disabledHref;
    private final String hrefTmpl;
    private final String criteriaQuery;
    private final boolean disableHtmlEscapeOfCriteriaQuery;
    private final boolean enableLinkOfCurrentPage;

    public PaginationAttrAccessor(Element element, String dialectPrefix) {

        this.dialectPrefix = dialectPrefix;

        this.innerElement = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                PaginationInfo.DEFAULT_INNER_ELM);
        this.disabledClass = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_DISABLED_CLASS,
                PaginationInfo.DEFAULT_DISABLED_CLASS);
        this.activeClass = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_ACTIVE_CLASS,
                PaginationInfo.DEFAULT_ACTIVE_CLASS);
        this.firstLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT,
                DEFAULT_FIRST_LINK_TEXT);
        this.previousLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_PREVIOUS_LINK_TEXT,
                DEFAULT_PREVIOUS_LINK_TEXT);
        this.nextLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT,
                DEFAULT_NEXT_LINK_TEXT);
        this.lastLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT,
                DEFAULT_LAST_LINK_TEXT);
        this.maxDisplayCount = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_MAX_DISPLAY_COUNT,
                PaginationInfo.DEFAULT_MAX_DISPLAY_COUNT);
        this.disabledHref = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_DISABLED_HREF,
                PaginationInfo.DEFAULT_DISABLED_HREF);
        this.hrefTmpl = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_HREF_TMPL,
                DEFAULT_HREF_TMPL);
        this.criteriaQuery = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_CRITERIA_QUERY,
                DEFAULT_CRITERIA_QUERY);
        this.disableHtmlEscapeOfCriteriaQuery = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY, DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        this.enableLinkOfCurrentPage = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE, DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE);
    }

    public void removeAttributes(Element element) {

        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLED_CLASS);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_ACTIVE_CLASS);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_PREVIOUS_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_MAX_DISPLAY_COUNT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLED_HREF);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_HREF_TMPL);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_CRITERIA_QUERY);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE);
    }

    public String getInnerElement() {
        return innerElement;
    }

    public String getDisabledClass() {
        return disabledClass;
    }

    public String getActiveClass() {
        return activeClass;
    }

    public String getFirstLinkText() {
        return firstLinkText;
    }

    public String getPreviousLinkText() {
        return previousLinkText;
    }

    public String getNextLinkText() {
        return nextLinkText;
    }

    public String getLastLinkText() {
        return lastLinkText;
    }

    public int getMaxDisplayCount() {
        return maxDisplayCount;
    }

    public String getDisabledHref() {
        return disabledHref;
    }

    public String getHrefTmpl() {
        return hrefTmpl;
    }

    public String getCriteriaQuery() {
        return criteriaQuery;
    }

    public boolean isDisableHtmlEscapeOfCriteriaQuery() {
        return disableHtmlEscapeOfCriteriaQuery;
    }

    public boolean isEnableLinkOfCurrentPage() {
        return enableLinkOfCurrentPage;
    }
}
