package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Plugin(name = "ExtentTestAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class ExtentTestAppender extends AbstractAppender {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss.SSS"));

    protected ExtentTestAppender(String name, Filter filter,
                                  Layout<? extends Serializable> layout,
                                  boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static ExtentTestAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {
        return new ExtentTestAppender(name, filter, layout, true, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
        ExtentTest test = TestContext.getTest();
        if (test == null) return;

        String timestamp = DATE_FORMAT.get().format(new Date(event.getTimeMillis()));
        String message = "[" + timestamp + "] " + event.getMessage().getFormattedMessage();

        int levelInt = event.getLevel().intLevel();
        if (levelInt <= Level.ERROR.intLevel()) {
            test.log(Status.FAIL, message);
        } else if (levelInt <= Level.WARN.intLevel()) {
            test.log(Status.WARNING, message);
        } else if (levelInt <= Level.INFO.intLevel()) {
            test.log(Status.INFO, message);
        } else {
            test.log(Status.INFO, message);
        }
    }
}
