package jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;

public class TransactionTokenAttrProcessor extends AbstractMarkupRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTokenAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "transaction-token";
    private static final int PRECEDENCE = 1200;

    private static final String TYPE_ATTR_NAME = "type";
    private static final String NAME_ATTR_NAME = "name";
    private static final String VALUE_ATTR_NAME = "value";

    public TransactionTokenAttrProcessor() {
        super(ATTRIBUTE_NAME);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE;
    }

    @Override
    protected RemovalType getRemovalType(final Arguments arguments, final Element element, final String attributeName) {

        // find token.
        TransactionToken nextToken = getTransactionToken(arguments);
        if (nextToken == null) {
            logger.debug("cannot found TransactionToken.");
            return RemovalType.ELEMENT;
        }

        // build element.
        buildElement(element, nextToken);

        return RemovalType.NONE;
    }

    private TransactionToken getTransactionToken(Arguments arguments) {
        return ContextUtils.getAttribute(arguments, TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                TransactionToken.class);
    }

    private void buildElement(Element element, TransactionToken nextToken) {

        element.setAttribute(TYPE_ATTR_NAME, "hidden");
        element.setAttribute(NAME_ATTR_NAME, TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
        element.setAttribute(VALUE_ATTR_NAME, nextToken.getTokenString());
    }

}