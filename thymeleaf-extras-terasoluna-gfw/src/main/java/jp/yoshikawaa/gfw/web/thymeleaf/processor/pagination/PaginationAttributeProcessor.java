package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.unbescape.html.HtmlEscape;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractHtmlAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class PaginationAttributeProcessor extends AbstractHtmlAttributeProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PaginationAttributeProcessor.class);

    private static final String ATTRIBUTE_NAME = "pagination";
    private static final int PRECEDENCE = 12000;

    private static final String DEFAULT_PAGE_EXPRESSION = "${page}";

    public PaginationAttributeProcessor(String dialectPrefix) {
        super(dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find page.
        Page<?> page = getPage(context, attributeValue);

        // find relative attributes.
        PaginationAttributeAccessor attrs = new PaginationAttributeAccessor(tag, dialectPrefix);
        attrs.removeAttributes(structureHandler);

        // exist page?
        if (page == null) {
            logger.debug("cannot found page.");
            return;
        }

        // build element.
        structureHandler.setBody(buildBody(context, page, attrs), false);
    }

    private Page<?> getPage(ITemplateContext context, String attributeValue) {

        return ExpressionUtils.execute(context,
                (StringUtils.hasText(attributeValue)) ? attributeValue : DEFAULT_PAGE_EXPRESSION, Page.class);
    }

    private IModel buildBody(ITemplateContext context, Page<?> page, PaginationAttributeAccessor attrs) {

        final String innerElement = attrs.getInnerElement();
        final String disabledClass = attrs.getDisabledClass();
        final String activeClass = attrs.getActiveClass();
        final String firstLinkText = attrs.getFirstLinkText();
        final String previousLinkText = attrs.getPreviousLinkText();
        final String nextLinkText = attrs.getNextLinkText();
        final String lastLinkText = attrs.getLastLinkText();
        final int maxDisplayCount = attrs.getMaxDisplayCount();
        final String disabledHref = attrs.getDisabledHref();
        final String hrefTmpl = attrs.getHrefTmpl();
        final String criteriaQuery = attrs.getCriteriaQuery();
        final boolean disableHtmlEscapeOfCriteriaQuery = attrs.isDisableHtmlEscapeOfCriteriaQuery();
        final boolean enableLinkOfCurrentPage = attrs.isEnableLinkOfCurrentPage();

        final ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, page, hrefTmpl, criteriaQuery,
                disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        if (info.isFirstPage()) {
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, firstLinkText));
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, previousLinkText));
        } else {
            model.addModel(buildInnerElement(context, innerElement, activeClass, info.getFirstUrl(), firstLinkText));
            model.addModel(
                    buildInnerElement(context, innerElement, activeClass, info.getPreviousUrl(), previousLinkText));
        }

        model.addModel(buildPageNumberElements(context, info, innerElement, activeClass, disabledClass, disabledHref,
                enableLinkOfCurrentPage));

        if (info.isLastPage()) {
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, nextLinkText));
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, lastLinkText));
        } else {
            model.addModel(buildInnerElement(context, innerElement, activeClass, info.getNextUrl(), nextLinkText));
            model.addModel(buildInnerElement(context, innerElement, activeClass, info.getLastUrl(), lastLinkText));
        }

        return model;
    }

    private IModel buildPageNumberElements(ITemplateContext context, ThymeleafPaginationInfo info, String innerElement,
            String activeClass, String disabledClass, String disabledHref, boolean enableLinkOfCurrentPage) {

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        BeginAndEnd be = info.getBeginAndEnd();
        IntStream.rangeClosed(be.getBegin(), be.getEnd()).forEachOrdered(i -> {
            if (info.isCurrent(i) && !enableLinkOfCurrentPage) {
                model.addModel(
                        buildInnerElement(context, innerElement, disabledClass, disabledHref, String.valueOf(i + 1)));
            } else {
                model.addModel(buildInnerElement(context, innerElement, activeClass, info.getPageUrl(i),
                        String.valueOf(i + 1)));
            }
        });

        return model;
    }

    private IModel buildInnerElement(ITemplateContext context, String innerElement, String activeOrDisabled,
            String href, String text) {

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        model.add(modelFactory.createOpenElementTag(innerElement, PaginationInfo.CLASS_ATTR, activeOrDisabled));

        if (StringUtils.hasText(href) && StringUtils.hasText(text)) {
            model.add(modelFactory.createOpenElementTag(PaginationInfo.A_ELM, PaginationInfo.HREF_ATTR,
                    HtmlEscape.escapeHtml5(href)));
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(text)));
            model.add(modelFactory.createCloseElementTag(PaginationInfo.A_ELM));
        } else {
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(text)));
        }

        model.add(modelFactory.createCloseElementTag(innerElement));

        return model;
    }

}
