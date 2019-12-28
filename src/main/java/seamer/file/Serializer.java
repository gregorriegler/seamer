package seamer.file;

import com.esotericsoftware.kryo.io.Output;
import seamer.core.Seam;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {
    Output serialize(Seam<?> seam, OutputStream outputStream);

    Object deserialize(InputStream inputStream);
}
