package seamer.file;

import seamer.core.Seam;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {
    void serialize(Seam<?> seam, OutputStream outputStream);

    Object deserialize(InputStream inputStream);
}
