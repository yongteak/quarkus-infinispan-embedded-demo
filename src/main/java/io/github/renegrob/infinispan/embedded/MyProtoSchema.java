package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = {MyCacheEntry.class, MyCacheEntryProducerAdapter.class}, schemaPackageName = "emb")
public interface MyProtoSchema extends GeneratedSchema {
}