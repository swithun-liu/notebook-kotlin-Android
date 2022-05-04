// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compiler/ir/serialization.common/src/KotlinPirCarriers.proto

package org.jetbrains.kotlin.backend.common.serialization.proto;

public interface PirTypeAliasCarrierOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jetbrains.kotlin.backend.common.serialization.proto.PirTypeAliasCarrier)
    org.jetbrains.kotlin.protobuf.MessageLiteOrBuilder {

  /**
   * <code>required int32 lastModified = 1;</code>
   */
  boolean hasLastModified();
  /**
   * <code>required int32 lastModified = 1;</code>
   */
  int getLastModified();

  /**
   * <code>optional int64 parentSymbol = 2;</code>
   */
  boolean hasParentSymbol();
  /**
   * <code>optional int64 parentSymbol = 2;</code>
   */
  long getParentSymbol();

  /**
   * <code>optional int32 origin = 3;</code>
   */
  boolean hasOrigin();
  /**
   * <code>optional int32 origin = 3;</code>
   */
  int getOrigin();

  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall annotation = 4;</code>
   */
  java.util.List<org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall> 
      getAnnotationList();
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall annotation = 4;</code>
   */
  org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall getAnnotation(int index);
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall annotation = 4;</code>
   */
  int getAnnotationCount();

  /**
   * <code>repeated int64 typeParameters = 5;</code>
   */
  java.util.List<java.lang.Long> getTypeParametersList();
  /**
   * <code>repeated int64 typeParameters = 5;</code>
   */
  int getTypeParametersCount();
  /**
   * <code>repeated int64 typeParameters = 5;</code>
   */
  long getTypeParameters(int index);

  /**
   * <code>required int32 expandedType = 6;</code>
   */
  boolean hasExpandedType();
  /**
   * <code>required int32 expandedType = 6;</code>
   */
  int getExpandedType();
}