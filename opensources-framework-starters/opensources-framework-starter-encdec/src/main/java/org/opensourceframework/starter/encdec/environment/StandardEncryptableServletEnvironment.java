package org.opensourceframework.starter.encdec.environment;

import org.opensourceframework.starter.encdec.EncryptablePropertyDetector;
import org.opensourceframework.starter.encdec.EncryptablePropertyResolver;
import org.opensourceframework.starter.encdec.EncryptablePropertySourceConverter;
import org.opensourceframework.starter.encdec.InterceptionMode;
import org.opensourceframework.starter.encdec.detector.DefaultPropertyDetector;
import org.opensourceframework.starter.encdec.encryptor.DefaultLazyEncryptor;
import org.opensourceframework.starter.encdec.resolver.DefaultPropertyResolver;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

public class StandardEncryptableServletEnvironment extends StandardServletEnvironment implements ConfigurableEnvironment {

    private final EncryptablePropertyResolver resolver;
    private final InterceptionMode interceptionMode;
    private MutablePropertySources encryptablePropertySources;
    private MutablePropertySources originalPropertySources;

    public StandardEncryptableServletEnvironment() {
        this(InterceptionMode.WRAPPER);
    }

    public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode) {
        this.interceptionMode = interceptionMode;
        this.resolver = new DefaultPropertyResolver(new DefaultLazyEncryptor(this), new DefaultPropertyDetector());
        actuallyCustomizePropertySources();
    }

    public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, EncryptablePropertyDetector detector) {
        this.interceptionMode = interceptionMode;
        this.resolver = new DefaultPropertyResolver(new DefaultLazyEncryptor(this), detector);
        actuallyCustomizePropertySources();
    }

    public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, StringEncryptor encryptor) {
        this.interceptionMode = interceptionMode;
        this.resolver = new DefaultPropertyResolver(encryptor, new DefaultPropertyDetector());
        actuallyCustomizePropertySources();
    }

    public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, StringEncryptor encryptor, EncryptablePropertyDetector detector) {
        this.interceptionMode = interceptionMode;
        this.resolver = new DefaultPropertyResolver(encryptor, detector);
        actuallyCustomizePropertySources();
    }

    public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, EncryptablePropertyResolver resolver) {
        this.interceptionMode = interceptionMode;
        this.resolver = resolver;
        actuallyCustomizePropertySources();
    }

    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        super.customizePropertySources(propertySources);
        this.originalPropertySources = propertySources;
    }

    protected void actuallyCustomizePropertySources() {
        EncryptablePropertySourceConverter.convertPropertySources(interceptionMode, resolver, originalPropertySources);
        encryptablePropertySources = EncryptablePropertySourceConverter.proxyPropertySources(interceptionMode, resolver, originalPropertySources);
    }

    @Override
    public MutablePropertySources getPropertySources() {
        return encryptablePropertySources;
    }
}
