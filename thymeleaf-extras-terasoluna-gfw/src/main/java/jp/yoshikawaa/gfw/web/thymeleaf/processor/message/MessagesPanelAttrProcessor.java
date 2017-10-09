package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;
import org.thymeleaf.util.StringUtils;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class MessagesPanelAttrProcessor extends AbstractMarkupRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "messages-panel";
    private static final int PRECEDENCE = 1200;

    private static final String CLASS_ATTR_NAME = "class";

    private final String dialectPrefix;
    private final MessageSource messageSource;

    public MessagesPanelAttrProcessor(String dialectPrefix, MessageSource messageSource) {
        super(ATTRIBUTE_NAME);
        if (messageSource == null) {
            throw new IllegalArgumentException("messageSource must not be null.");
        }
        this.dialectPrefix = dialectPrefix;
        this.messageSource = messageSource;
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE;
    }

    @Override
    protected RemovalType getRemovalType(final Arguments arguments, final Element element, final String attributeName) {

        // find relative attributes.
        MessagesPanelAttrAccessor attrs = new MessagesPanelAttrAccessor(element, dialectPrefix);
        attrs.removeAttributes(element);

        // find messages.
        final String attributeValue = element.getAttributeValue(attributeName);
        final Object messages = getResultMessages(arguments, attributeValue);
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return RemovalType.ELEMENT;
        }

        // build element.
        buildElement(element, messages, attrs);
        buildBody(arguments, element, messages, attrs);

        return RemovalType.NONE;
    }

    private Object getResultMessages(Arguments arguments, String attributeValue) {
        return StringUtils.isEmptyOrWhitespace(attributeValue)
                ? ContextUtils.getAttribute(arguments, ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME)
                : ExpressionUtils.execute(arguments, attributeValue);
    }

    private void buildElement(Element element, Object messages, MessagesPanelAttrAccessor attrs) {

        final String panelClassName = attrs.getPanelClassName();
        final String panelTypeClass = attrs.getPanelTypeClass(messages);

        element.setAttribute(CLASS_ATTR_NAME, StringUtils.isEmptyOrWhitespace(panelClassName) ? panelTypeClass
                : panelClassName + " " + panelTypeClass);
    }

    private void buildBody(Arguments arguments, Element element, Object messages, MessagesPanelAttrAccessor attrs) {

        final String outerElement = attrs.getOuterElement();
        final String innerElement = attrs.getInnerElement();
        final boolean disableHtmlEscape = attrs.isDisableHtmlEscape();

        List<Element> inner = buildInnerElements(arguments, messages, innerElement, disableHtmlEscape);

        if (StringUtils.isEmptyOrWhitespace(outerElement)) {
            inner.forEach(n -> element.addChild(n));
        } else {
            Element outer = new Element(outerElement);
            inner.forEach(n -> outer.addChild(n));
            element.addChild(outer);
        }
    }

    private List<Element> buildInnerElements(Arguments arguments, Object messages, String innerElement,
            boolean disableHtmlEscape) {

        if (messages instanceof Iterable<?>) {
            return StreamSupport.stream(((Iterable<?>) messages).spliterator(), false)
                    .map(m -> buildInnerElement(arguments, m, innerElement, disableHtmlEscape))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<Element>(
                    Arrays.asList(buildInnerElement(arguments, messages, innerElement, disableHtmlEscape)));
        }
    }

    private Element buildInnerElement(Arguments arguments, Object message, String innerElement,
            boolean disableHtmlEscape) {

        final Locale locale = arguments.getContext().getLocale();

        Element element = new Element(innerElement);
        element.addChild(disableHtmlEscape ? new Macro(resolveMessage(message, locale))
                : new Text(resolveMessage(message, locale)));
        return element;
    }

    private String resolveMessage(Object message, Locale locale) {

        if (message instanceof ResultMessage) {
            return ResultMessageUtils.resolveMessage((ResultMessage) message, messageSource, locale);
        } else if (message instanceof String) {
            return (String) message;
        } else if (message instanceof Throwable) {
            return ((Throwable) message).getMessage();
        }
        return message.toString();
    }

}
