package oracle.webcenter.sites.framework.analytics.model.google;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class ServiceAccountPrivateKey implements Serializable {

    public ServiceAccountPrivateKey(String keyFilePath) throws IOException {
        file = null;
        file = new File(keyFilePath);
    }

    public File getFile() {
        return file;
    }

    private static final long serialVersionUID = 1L;
    private File file;
}
