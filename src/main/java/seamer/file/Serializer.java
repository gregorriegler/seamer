package seamer.file;

import seamer.core.Invocation;
import seamer.core.Seam;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Serializer {
    void serialize(Seam<?> seam, OutputStream outputStream);

    void serialize(Invocation invocation, OutputStream outputStream);

    Object deserialize(InputStream inputStream);

    List<Invocation> deserializeInvocations(InputStream inputStream);
}
