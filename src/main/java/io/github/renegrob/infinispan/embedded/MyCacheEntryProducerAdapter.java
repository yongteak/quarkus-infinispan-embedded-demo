package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoName;

@ProtoAdapter(MyCacheEntryProducer.class)
@ProtoName("MyCacheEntryProducer")
public class MyCacheEntryProducerAdapter {
    @ProtoFactory
    MyCacheEntryProducer create() {
        return MyCacheEntryProducer.INSTANCE;
    }
}
