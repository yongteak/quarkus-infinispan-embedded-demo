package io.github.renegrob.infinispan.embedded;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

import io.quarkus.runtime.annotations.RegisterForReflection;

@AutoProtoSchemaBuilder(includeClasses = {MyCacheEntry.class, MyCacheEntryProducerAdapter.class}, schemaPackageName = "emb")
@RegisterForReflection
public interface MyProtoSchema extends GeneratedSchema {
}