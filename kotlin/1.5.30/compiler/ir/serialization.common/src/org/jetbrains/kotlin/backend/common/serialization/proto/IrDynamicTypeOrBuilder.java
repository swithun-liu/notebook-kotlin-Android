// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compiler/ir/serialization.common/src/KotlinIr.proto

package org.jetbrains.kotlin.backend.common.serialization.proto;

public interface IrDynamicTypeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jetbrains.kotlin.backend.common.serialization.proto.IrDynamicType)
    org.jetbrains.kotlin.protobuf.MessageLiteOrBuilder {

  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall annotation = 1;</code>
   */
  java.util.List<org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall> 
      getAnnotationList();
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall annotation = 1;</code>
   */
  org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall getAnnotation(int index);
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrConstructorCall annotation = 1;</code>
   */
  int getAnnotationCount();
}