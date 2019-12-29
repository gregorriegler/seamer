package seamer;

import seamer.core.Seam;
import seamer.core.Seamer;
import seamer.file.FileInvocationRepository;
import seamer.file.FileResetter;
import seamer.file.FileSeamRepository;
import seamer.kryo.KryoSerializer;

public class SeamerFactory {

    public static <T> Seamer<T> load(Class<?> carrierClass, final String seamId) {
        return new FileSeamRepository<T>(new KryoSerializer(carrierClass))
            .load(seamId)
            .map(seam -> create(seam, seamId))
            .orElseThrow(FailedToLoad::new);
    }

    public static <T> Seamer<T> persist(Seam<T> seam, Class<?> carrierClass, final String seamId) {
        reset(seamId);
        return intercept(seam, carrierClass, seamId);
    }

    public static <T> Seamer<T> intercept(Seam<T> seam, Class<?> carrierClass, final String seamId) {
        Seamer<T> seamer = create(seam, seamId);
        new FileSeamRepository<T>(new KryoSerializer(carrierClass)).persist(seam, seamId);
        return seamer;
    }

    public static <T> Seamer<T> create(Seam<T> seam, final String seamId) {
        return new Seamer<>(
            seam,
            new FileInvocationRepository(new KryoSerializer(), seamId)
        );
    }

    public static void reset(String seamId) {
        new FileResetter().reset(seamId);
    }

    public static class FailedToLoad extends RuntimeException {
    }
}
