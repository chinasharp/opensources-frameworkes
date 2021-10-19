package org.opensourceframework.starter.encdec.resolver;

import org.opensourceframework.starter.encdec.EncryptablePropertyDetector;
import org.opensourceframework.starter.encdec.EncryptablePropertyResolver;
import org.opensourceframework.starter.encdec.detector.DefaultPropertyDetector;
import org.opensourceframework.starter.encdec.exception.DecryptionException;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.util.Assert;

/**
 * @author Ulises Bocchio
 */
public class DefaultPropertyResolver implements EncryptablePropertyResolver {

    private final StringEncryptor encryptor;
    private final EncryptablePropertyDetector detector;

    public DefaultPropertyResolver(StringEncryptor encryptor) {
        this(encryptor, new DefaultPropertyDetector());
    }

    public DefaultPropertyResolver(StringEncryptor encryptor, EncryptablePropertyDetector detector) {
        Assert.notNull(encryptor, "String encryptor can't be null");
        Assert.notNull(detector, "Encryptable Property detector can't be null");
        this.encryptor = encryptor;
        this.detector = detector;
    }

    @Override
    public String resolvePropertyValue(String value) {
        String actualValue = value;
        if (detector.isEncrypted(value)) {
            try {
                actualValue = encryptor.decrypt(detector.unwrapEncryptedValue(value.trim()));
            } catch (EncryptionOperationNotPossibleException e) {
                throw new DecryptionException("Decryption of Properties failed,  make sure encryption/decryption " +
                        "passwords match", e);
            }
        }
        return actualValue;
    }
}
