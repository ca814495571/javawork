/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.cqfc.protocol.businesscontroller;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SportDetail implements org.apache.thrift.TBase<SportDetail, SportDetail._Fields>, java.io.Serializable, Cloneable, Comparable<SportDetail> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SportDetail");

  private static final org.apache.thrift.protocol.TField MATCH_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("matchId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField TRANSFER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("transferId", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField MATCH_NO_FIELD_DESC = new org.apache.thrift.protocol.TField("matchNo", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField RQ_FIELD_DESC = new org.apache.thrift.protocol.TField("rq", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField ORDER_CONTENT_FIELD_DESC = new org.apache.thrift.protocol.TField("orderContent", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new SportDetailStandardSchemeFactory());
    schemes.put(TupleScheme.class, new SportDetailTupleSchemeFactory());
  }

  public String matchId; // required
  public String transferId; // required
  public String matchNo; // required
  public String rq; // required
  public String orderContent; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    MATCH_ID((short)1, "matchId"),
    TRANSFER_ID((short)2, "transferId"),
    MATCH_NO((short)3, "matchNo"),
    RQ((short)4, "rq"),
    ORDER_CONTENT((short)5, "orderContent");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // MATCH_ID
          return MATCH_ID;
        case 2: // TRANSFER_ID
          return TRANSFER_ID;
        case 3: // MATCH_NO
          return MATCH_NO;
        case 4: // RQ
          return RQ;
        case 5: // ORDER_CONTENT
          return ORDER_CONTENT;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.MATCH_ID, new org.apache.thrift.meta_data.FieldMetaData("matchId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TRANSFER_ID, new org.apache.thrift.meta_data.FieldMetaData("transferId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MATCH_NO, new org.apache.thrift.meta_data.FieldMetaData("matchNo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.RQ, new org.apache.thrift.meta_data.FieldMetaData("rq", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ORDER_CONTENT, new org.apache.thrift.meta_data.FieldMetaData("orderContent", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SportDetail.class, metaDataMap);
  }

  public SportDetail() {
  }

  public SportDetail(
    String matchId,
    String transferId,
    String matchNo,
    String rq,
    String orderContent)
  {
    this();
    this.matchId = matchId;
    this.transferId = transferId;
    this.matchNo = matchNo;
    this.rq = rq;
    this.orderContent = orderContent;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SportDetail(SportDetail other) {
    if (other.isSetMatchId()) {
      this.matchId = other.matchId;
    }
    if (other.isSetTransferId()) {
      this.transferId = other.transferId;
    }
    if (other.isSetMatchNo()) {
      this.matchNo = other.matchNo;
    }
    if (other.isSetRq()) {
      this.rq = other.rq;
    }
    if (other.isSetOrderContent()) {
      this.orderContent = other.orderContent;
    }
  }

  public SportDetail deepCopy() {
    return new SportDetail(this);
  }

  @Override
  public void clear() {
    this.matchId = null;
    this.transferId = null;
    this.matchNo = null;
    this.rq = null;
    this.orderContent = null;
  }

  public String getMatchId() {
    return this.matchId;
  }

  public SportDetail setMatchId(String matchId) {
    this.matchId = matchId;
    return this;
  }

  public void unsetMatchId() {
    this.matchId = null;
  }

  /** Returns true if field matchId is set (has been assigned a value) and false otherwise */
  public boolean isSetMatchId() {
    return this.matchId != null;
  }

  public void setMatchIdIsSet(boolean value) {
    if (!value) {
      this.matchId = null;
    }
  }

  public String getTransferId() {
    return this.transferId;
  }

  public SportDetail setTransferId(String transferId) {
    this.transferId = transferId;
    return this;
  }

  public void unsetTransferId() {
    this.transferId = null;
  }

  /** Returns true if field transferId is set (has been assigned a value) and false otherwise */
  public boolean isSetTransferId() {
    return this.transferId != null;
  }

  public void setTransferIdIsSet(boolean value) {
    if (!value) {
      this.transferId = null;
    }
  }

  public String getMatchNo() {
    return this.matchNo;
  }

  public SportDetail setMatchNo(String matchNo) {
    this.matchNo = matchNo;
    return this;
  }

  public void unsetMatchNo() {
    this.matchNo = null;
  }

  /** Returns true if field matchNo is set (has been assigned a value) and false otherwise */
  public boolean isSetMatchNo() {
    return this.matchNo != null;
  }

  public void setMatchNoIsSet(boolean value) {
    if (!value) {
      this.matchNo = null;
    }
  }

  public String getRq() {
    return this.rq;
  }

  public SportDetail setRq(String rq) {
    this.rq = rq;
    return this;
  }

  public void unsetRq() {
    this.rq = null;
  }

  /** Returns true if field rq is set (has been assigned a value) and false otherwise */
  public boolean isSetRq() {
    return this.rq != null;
  }

  public void setRqIsSet(boolean value) {
    if (!value) {
      this.rq = null;
    }
  }

  public String getOrderContent() {
    return this.orderContent;
  }

  public SportDetail setOrderContent(String orderContent) {
    this.orderContent = orderContent;
    return this;
  }

  public void unsetOrderContent() {
    this.orderContent = null;
  }

  /** Returns true if field orderContent is set (has been assigned a value) and false otherwise */
  public boolean isSetOrderContent() {
    return this.orderContent != null;
  }

  public void setOrderContentIsSet(boolean value) {
    if (!value) {
      this.orderContent = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case MATCH_ID:
      if (value == null) {
        unsetMatchId();
      } else {
        setMatchId((String)value);
      }
      break;

    case TRANSFER_ID:
      if (value == null) {
        unsetTransferId();
      } else {
        setTransferId((String)value);
      }
      break;

    case MATCH_NO:
      if (value == null) {
        unsetMatchNo();
      } else {
        setMatchNo((String)value);
      }
      break;

    case RQ:
      if (value == null) {
        unsetRq();
      } else {
        setRq((String)value);
      }
      break;

    case ORDER_CONTENT:
      if (value == null) {
        unsetOrderContent();
      } else {
        setOrderContent((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case MATCH_ID:
      return getMatchId();

    case TRANSFER_ID:
      return getTransferId();

    case MATCH_NO:
      return getMatchNo();

    case RQ:
      return getRq();

    case ORDER_CONTENT:
      return getOrderContent();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case MATCH_ID:
      return isSetMatchId();
    case TRANSFER_ID:
      return isSetTransferId();
    case MATCH_NO:
      return isSetMatchNo();
    case RQ:
      return isSetRq();
    case ORDER_CONTENT:
      return isSetOrderContent();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof SportDetail)
      return this.equals((SportDetail)that);
    return false;
  }

  public boolean equals(SportDetail that) {
    if (that == null)
      return false;

    boolean this_present_matchId = true && this.isSetMatchId();
    boolean that_present_matchId = true && that.isSetMatchId();
    if (this_present_matchId || that_present_matchId) {
      if (!(this_present_matchId && that_present_matchId))
        return false;
      if (!this.matchId.equals(that.matchId))
        return false;
    }

    boolean this_present_transferId = true && this.isSetTransferId();
    boolean that_present_transferId = true && that.isSetTransferId();
    if (this_present_transferId || that_present_transferId) {
      if (!(this_present_transferId && that_present_transferId))
        return false;
      if (!this.transferId.equals(that.transferId))
        return false;
    }

    boolean this_present_matchNo = true && this.isSetMatchNo();
    boolean that_present_matchNo = true && that.isSetMatchNo();
    if (this_present_matchNo || that_present_matchNo) {
      if (!(this_present_matchNo && that_present_matchNo))
        return false;
      if (!this.matchNo.equals(that.matchNo))
        return false;
    }

    boolean this_present_rq = true && this.isSetRq();
    boolean that_present_rq = true && that.isSetRq();
    if (this_present_rq || that_present_rq) {
      if (!(this_present_rq && that_present_rq))
        return false;
      if (!this.rq.equals(that.rq))
        return false;
    }

    boolean this_present_orderContent = true && this.isSetOrderContent();
    boolean that_present_orderContent = true && that.isSetOrderContent();
    if (this_present_orderContent || that_present_orderContent) {
      if (!(this_present_orderContent && that_present_orderContent))
        return false;
      if (!this.orderContent.equals(that.orderContent))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(SportDetail other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMatchId()).compareTo(other.isSetMatchId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMatchId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.matchId, other.matchId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTransferId()).compareTo(other.isSetTransferId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTransferId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.transferId, other.transferId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMatchNo()).compareTo(other.isSetMatchNo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMatchNo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.matchNo, other.matchNo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRq()).compareTo(other.isSetRq());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRq()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.rq, other.rq);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOrderContent()).compareTo(other.isSetOrderContent());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOrderContent()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.orderContent, other.orderContent);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("SportDetail(");
    boolean first = true;

    sb.append("matchId:");
    if (this.matchId == null) {
      sb.append("null");
    } else {
      sb.append(this.matchId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("transferId:");
    if (this.transferId == null) {
      sb.append("null");
    } else {
      sb.append(this.transferId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("matchNo:");
    if (this.matchNo == null) {
      sb.append("null");
    } else {
      sb.append(this.matchNo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("rq:");
    if (this.rq == null) {
      sb.append("null");
    } else {
      sb.append(this.rq);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("orderContent:");
    if (this.orderContent == null) {
      sb.append("null");
    } else {
      sb.append(this.orderContent);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class SportDetailStandardSchemeFactory implements SchemeFactory {
    public SportDetailStandardScheme getScheme() {
      return new SportDetailStandardScheme();
    }
  }

  private static class SportDetailStandardScheme extends StandardScheme<SportDetail> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SportDetail struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // MATCH_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.matchId = iprot.readString();
              struct.setMatchIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TRANSFER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.transferId = iprot.readString();
              struct.setTransferIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // MATCH_NO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.matchNo = iprot.readString();
              struct.setMatchNoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // RQ
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.rq = iprot.readString();
              struct.setRqIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // ORDER_CONTENT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.orderContent = iprot.readString();
              struct.setOrderContentIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, SportDetail struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.matchId != null) {
        oprot.writeFieldBegin(MATCH_ID_FIELD_DESC);
        oprot.writeString(struct.matchId);
        oprot.writeFieldEnd();
      }
      if (struct.transferId != null) {
        oprot.writeFieldBegin(TRANSFER_ID_FIELD_DESC);
        oprot.writeString(struct.transferId);
        oprot.writeFieldEnd();
      }
      if (struct.matchNo != null) {
        oprot.writeFieldBegin(MATCH_NO_FIELD_DESC);
        oprot.writeString(struct.matchNo);
        oprot.writeFieldEnd();
      }
      if (struct.rq != null) {
        oprot.writeFieldBegin(RQ_FIELD_DESC);
        oprot.writeString(struct.rq);
        oprot.writeFieldEnd();
      }
      if (struct.orderContent != null) {
        oprot.writeFieldBegin(ORDER_CONTENT_FIELD_DESC);
        oprot.writeString(struct.orderContent);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SportDetailTupleSchemeFactory implements SchemeFactory {
    public SportDetailTupleScheme getScheme() {
      return new SportDetailTupleScheme();
    }
  }

  private static class SportDetailTupleScheme extends TupleScheme<SportDetail> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SportDetail struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMatchId()) {
        optionals.set(0);
      }
      if (struct.isSetTransferId()) {
        optionals.set(1);
      }
      if (struct.isSetMatchNo()) {
        optionals.set(2);
      }
      if (struct.isSetRq()) {
        optionals.set(3);
      }
      if (struct.isSetOrderContent()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetMatchId()) {
        oprot.writeString(struct.matchId);
      }
      if (struct.isSetTransferId()) {
        oprot.writeString(struct.transferId);
      }
      if (struct.isSetMatchNo()) {
        oprot.writeString(struct.matchNo);
      }
      if (struct.isSetRq()) {
        oprot.writeString(struct.rq);
      }
      if (struct.isSetOrderContent()) {
        oprot.writeString(struct.orderContent);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SportDetail struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.matchId = iprot.readString();
        struct.setMatchIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.transferId = iprot.readString();
        struct.setTransferIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.matchNo = iprot.readString();
        struct.setMatchNoIsSet(true);
      }
      if (incoming.get(3)) {
        struct.rq = iprot.readString();
        struct.setRqIsSet(true);
      }
      if (incoming.get(4)) {
        struct.orderContent = iprot.readString();
        struct.setOrderContentIsSet(true);
      }
    }
  }

}

