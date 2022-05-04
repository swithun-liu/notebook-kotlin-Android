// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compiler/ir/serialization.common/src/KotlinIr.proto

package org.jetbrains.kotlin.backend.common.serialization.proto;

public interface IrTypeAbbreviationOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jetbrains.kotlin.backend.common.serialization.proto.IrTypeAbbreviation)
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

  /**
   * <code>required int64 type_alias = 2;</code>
   */
  boolean hasTypeAlias();
  /**
   * <code>required int64 type_alias = 2;</code>
   */
  long getTypeAlias();

  /**
   * <code>required bool has_question_mark = 3;</code>
   */
  boolean hasHasQuestionMark();
  /**
   * <code>required bool has_question_mark = 3;</code>
   */
  boolean getHasQuestionMark();

  /**
   * <code>repeated int64 argument = 4 [packed = true];</code>
   *
   * <pre>
   * 0 - STAR, otherwise [63..2 - IrType index | 1..0 - Variance]
   * </pre>
   */
  java.util.List<java.lang.Long> getArgumentList();
  /**
   * <code>repeated int64 argument = 4 [packed = true];</code>
   *
   * <pre>
   * 0 - STAR, otherwise [63..2 - IrType index | 1..0 - Variance]
   * </pre>
   */
  int getArgumentCount();
  /**
   * <code>repeated int64 argument = 4 [packed = true];</code>
   *
   * <pre>
   * 0 - STAR, otherwise [63..2 - IrType index | 1..0 - Variance]
   * </pre>
   */
  long getArgument(int index);
}